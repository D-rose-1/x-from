package cn.wwc.x.trigger.http;

import cn.wwc.x.api.request.FormCreateRequest;
import cn.wwc.x.api.request.FormSubmitRequest;
import cn.wwc.x.api.response.FormDetailResponse;
import cn.wwc.x.api.response.Response;
import cn.wwc.x.api.response.SubmissionVO;
import cn.wwc.x.domain.form.model.entity.FormDefinitionEntity;
import cn.wwc.x.domain.form.model.entity.FormFieldEntity;
import cn.wwc.x.domain.form.model.entity.FormSubmissionEntity;
import cn.wwc.x.domain.form.service.IFormService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/form")
public class FormController {

    @Resource
    private IFormService formService;

    /** 创建表单 */
    @PostMapping("/create")
    public Response<Map<String, String>> create(@RequestBody FormCreateRequest request) {
        try {
            FormDefinitionEntity form = FormDefinitionEntity.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .createdBy(request.getCreatedBy())
                    .build();

            List<FormFieldEntity> fields = new ArrayList<>();
            if (request.getFields() != null) {
                for (FormCreateRequest.FieldItem item : request.getFields()) {
                    fields.add(FormFieldEntity.builder()
                            .fieldKey(item.getFieldKey())
                            .fieldLabel(item.getFieldLabel())
                            .fieldType(item.getFieldType())
                            .required(item.getRequired() != null ? item.getRequired() : 0)
                            .optionsJson(item.getOptions())
                            .placeholder(item.getPlaceholder())
                            .build());
                }
            }

            FormDefinitionEntity result = formService.createForm(form, fields);
            formService.publishForm(result.getFormKey());

            Map<String, String> data = new HashMap<>();
            data.put("formKey", result.getFormKey());
            data.put("shareLink", result.getShareLink());
            return Response.<Map<String, String>>builder()
                    .code("0000").info("创建成功").data(data).build();
        } catch (Exception e) {
            log.error("创建表单失败", e);
            return Response.<Map<String, String>>builder().code("0001").info(e.getMessage()).build();
        }
    }

    /** 表单列表（支持分页） */
    @GetMapping("/list")
    public Response<Map<String, Object>> list(@RequestParam(name = "page", defaultValue = "1") int page,
                                               @RequestParam(name = "size", defaultValue = "20") int size) {
        try {
            List<FormDefinitionEntity> allForms = formService.listForms();
            int total = allForms.size();
            int from = Math.max(0, (page - 1) * size);
            int to = Math.min(from + size, total);
            List<FormDefinitionEntity> paged = from < total ? allForms.subList(from, to) : Collections.emptyList();

            List<Map<String, Object>> items = paged.stream().map(f -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", f.getId()); m.put("formKey", f.getFormKey());
                m.put("title", f.getTitle()); m.put("description", f.getDescription());
                m.put("status", f.getStatus()); m.put("shareLink", f.getShareLink());
                m.put("createdAt", f.getCreatedAt());
                return m;
            }).collect(Collectors.toList());

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("list", items);
            return Response.<Map<String, Object>>builder().code("0000").info("成功").data(result).build();
        } catch (Exception e) {
            return Response.<Map<String, Object>>builder().code("0001").info(e.getMessage()).build();
        }
    }

    /** 表单详情（用户端用） */
    @GetMapping("/detail/{formKey}")
    public Response<FormDetailResponse> detail(@PathVariable("formKey") String formKey) {
        try {
            Map<String, Object> detail = formService.getFormDetail(formKey);
            FormDefinitionEntity form = (FormDefinitionEntity) detail.get("form");
            @SuppressWarnings("unchecked")
            List<FormFieldEntity> fields = (List<FormFieldEntity>) detail.get("fields");

            FormDetailResponse resp = FormDetailResponse.builder()
                    .id(form.getId()).formKey(form.getFormKey())
                    .title(form.getTitle()).description(form.getDescription())
                    .status(form.getStatus()).shareLink(form.getShareLink())
                    .fields(fields.stream().map(f -> FormDetailResponse.FieldVO.builder()
                            .fieldKey(f.getFieldKey()).fieldLabel(f.getFieldLabel())
                            .fieldType(f.getFieldType()).required(f.getRequired())
                            .options(f.getOptionsJson()).placeholder(f.getPlaceholder())
                            .build()).collect(Collectors.toList()))
                    .build();
            return Response.<FormDetailResponse>builder().code("0000").info("成功").data(resp).build();
        } catch (Exception e) {
            return Response.<FormDetailResponse>builder().code("0001").info(e.getMessage()).build();
        }
    }

    /** 提交表单数据 */
    @PostMapping("/submit")
    public Response<String> submit(@RequestBody FormSubmitRequest request, HttpServletRequest req) {
        try {
            String ip = req.getRemoteAddr();
            formService.submitForm(request.getFormKey(), request.getData(),
                    request.getSubmitterName(), request.getSubmitterPhone(), ip);
            return Response.<String>builder().code("0000").info("提交成功").build();
        } catch (Exception e) {
            log.error("提交表单失败", e);
            return Response.<String>builder().code("0001").info(e.getMessage()).build();
        }
    }

    /** 查询提交数据列表 */
    @GetMapping("/submissions/{formId}")
    public Response<List<SubmissionVO>> submissions(@PathVariable("formId") Long formId) {
        try {
            List<FormSubmissionEntity> subs = formService.listSubmissions(formId);
            List<SubmissionVO> result = subs.stream().map(s -> SubmissionVO.builder()
                    .id(s.getId()).formId(s.getFormId()).formKey(s.getFormKey())
                    .submitData(s.getSubmitData()).submitterName(s.getSubmitterName())
                    .submitterPhone(s.getSubmitterPhone()).ipAddress(s.getIpAddress())
                    .createdAt(s.getCreatedAt()).build()).collect(Collectors.toList());
            return Response.<List<SubmissionVO>>builder().code("0000").info("成功").data(result).build();
        } catch (Exception e) {
            return Response.<List<SubmissionVO>>builder().code("0001").info(e.getMessage()).build();
        }
    }
}
