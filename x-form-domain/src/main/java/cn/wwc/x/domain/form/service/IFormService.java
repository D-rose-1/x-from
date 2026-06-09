package cn.wwc.x.domain.form.service;

import cn.wwc.x.domain.form.model.entity.FormDefinitionEntity;
import cn.wwc.x.domain.form.model.entity.FormFieldEntity;
import cn.wwc.x.domain.form.model.entity.FormSubmissionEntity;

import java.util.List;
import java.util.Map;

public interface IFormService {

    /** 创建表单（含字段） */
    FormDefinitionEntity createForm(FormDefinitionEntity form, List<FormFieldEntity> fields);

    /** 发布表单 */
    void publishForm(String formKey);

    /** 查询表单列表 */
    List<FormDefinitionEntity> listForms();

    /** 查询表单详情（含字段） */
    Map<String, Object> getFormDetail(String formKey);

    /** 提交表单数据 */
    void submitForm(String formKey, Map<String, Object> data, String name, String phone, String ip);

    /** 查询表单提交列表 */
    List<FormSubmissionEntity> listSubmissions(Long formId);
}
