package cn.wwc.x.domain.form.service;

import cn.wwc.x.domain.form.adapter.repository.IFormRepository;
import cn.wwc.x.domain.form.model.entity.FormDefinitionEntity;
import cn.wwc.x.domain.form.model.entity.FormFieldEntity;
import cn.wwc.x.domain.form.model.entity.FormSubmissionEntity;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FormServiceImpl implements IFormService {

    @Resource
    private IFormRepository formRepository;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    private static final String CACHE_FORM_LIST = "form:list";
    private static final String CACHE_FORM_DETAIL = "form:detail:";
    private static final long CACHE_TTL = 60; // 60 seconds

    @Override
    public FormDefinitionEntity createForm(FormDefinitionEntity form, List<FormFieldEntity> fields) {
        form.setFormKey("FORM_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        form.setStatus(1);
        form.setShareLink("/form/" + form.getFormKey());

        Long formId = formRepository.saveFormDefinition(form);
        form.setId(formId);

        if (fields != null && !fields.isEmpty()) {
            for (int i = 0; i < fields.size(); i++) {
                fields.get(i).setFormId(formId);
                fields.get(i).setSortOrder(i + 1);
            }
            formRepository.saveFormFields(fields);
        }

        // 清理列表缓存
        redisTemplate.delete(CACHE_FORM_LIST);
        log.info("表单创建成功: key={}, title={}", form.getFormKey(), form.getTitle());
        return form;
    }

    @Override
    public void publishForm(String formKey) {
        FormDefinitionEntity form = formRepository.queryFormDefinitionByKey(formKey);
        if (form == null) throw new RuntimeException("表单不存在: " + formKey);
        formRepository.updateFormStatus(form.getId(), 2);
        redisTemplate.delete(CACHE_FORM_LIST);
        redisTemplate.delete(CACHE_FORM_DETAIL + formKey);
        log.info("表单已发布: key={}", formKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FormDefinitionEntity> listForms() {
        // 先从 Redis 取
        String cached = redisTemplate.opsForValue().get(CACHE_FORM_LIST);
        if (cached != null) {
            return JSON.parseArray(cached, FormDefinitionEntity.class);
        }
        List<FormDefinitionEntity> list = formRepository.queryFormDefinitionList();
        redisTemplate.opsForValue().set(CACHE_FORM_LIST, JSON.toJSONString(list), CACHE_TTL, TimeUnit.SECONDS);
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getFormDetail(String formKey) {
        // 先从 Redis 取
        String cached = redisTemplate.opsForValue().get(CACHE_FORM_DETAIL + formKey);
        if (cached != null) {
            return JSON.parseObject(cached, Map.class);
        }

        FormDefinitionEntity form = formRepository.queryFormDefinitionByKey(formKey);
        if (form == null) throw new RuntimeException("表单不存在: " + formKey);

        List<FormFieldEntity> fields = formRepository.queryFormFields(form.getId());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("form", form);
        result.put("fields", fields);

        // 缓存60秒
        Map<String, Object> cacheData = new LinkedHashMap<>(result);
        redisTemplate.opsForValue().set(CACHE_FORM_DETAIL + formKey, JSON.toJSONString(cacheData), CACHE_TTL, TimeUnit.SECONDS);
        return result;
    }

    @Override
    public void submitForm(String formKey, Map<String, Object> data, String name, String phone, String ip) {
        FormDefinitionEntity form = formRepository.queryFormDefinitionByKey(formKey);
        if (form == null) throw new RuntimeException("表单不存在: " + formKey);
        if (form.getStatus() != 2) throw new RuntimeException("表单未发布，无法提交");

        FormSubmissionEntity submission = FormSubmissionEntity.builder()
                .formId(form.getId()).formKey(formKey)
                .submitData(JSON.toJSONString(data))
                .submitterName(name).submitterPhone(phone).ipAddress(ip).build();
        formRepository.saveSubmission(submission);
        log.info("表单提交成功: formKey={}, name={}", formKey, name);
    }

    @Override
    public List<FormSubmissionEntity> listSubmissions(Long formId) {
        return formRepository.querySubmissions(formId);
    }
}
