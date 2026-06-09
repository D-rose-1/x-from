package cn.wwc.x.domain.form.adapter.repository;

import cn.wwc.x.domain.form.model.entity.FormDefinitionEntity;
import cn.wwc.x.domain.form.model.entity.FormFieldEntity;
import cn.wwc.x.domain.form.model.entity.FormSubmissionEntity;

import java.util.List;

public interface IFormRepository {

    // 表单定义
    Long saveFormDefinition(FormDefinitionEntity entity);
    FormDefinitionEntity queryFormDefinitionByKey(String formKey);
    FormDefinitionEntity queryFormDefinitionById(Long id);
    List<FormDefinitionEntity> queryFormDefinitionList();
    void updateFormStatus(Long id, Integer status);

    // 表单字段
    void saveFormFields(List<FormFieldEntity> fields);
    List<FormFieldEntity> queryFormFields(Long formId);

    // 表单提交
    Long saveSubmission(FormSubmissionEntity entity);
    List<FormSubmissionEntity> querySubmissions(Long formId);
    Long countSubmissions(Long formId);
}
