package cn.wwc.x.infrastructure.adapter.repository;

import cn.wwc.x.domain.form.adapter.repository.IFormRepository;
import cn.wwc.x.domain.form.model.entity.FormDefinitionEntity;
import cn.wwc.x.domain.form.model.entity.FormFieldEntity;
import cn.wwc.x.domain.form.model.entity.FormSubmissionEntity;
import cn.wwc.x.infrastructure.dao.IFormDefinitionDao;
import cn.wwc.x.infrastructure.dao.IFormFieldDao;
import cn.wwc.x.infrastructure.dao.IFormSubmissionDao;
import cn.wwc.x.infrastructure.dao.po.FormDefinitionPO;
import cn.wwc.x.infrastructure.dao.po.FormFieldPO;
import cn.wwc.x.infrastructure.dao.po.FormSubmissionPO;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FormRepository implements IFormRepository {

    @Resource
    private IFormDefinitionDao formDefinitionDao;
    @Resource
    private IFormFieldDao formFieldDao;
    @Resource
    private IFormSubmissionDao formSubmissionDao;

    @Override
    public Long saveFormDefinition(FormDefinitionEntity entity) {
        FormDefinitionPO po = toPO(entity);
        formDefinitionDao.insert(po);
        return po.getId();
    }

    @Override
    public FormDefinitionEntity queryFormDefinitionByKey(String formKey) {
        FormDefinitionPO po = formDefinitionDao.selectByFormKey(formKey);
        return toEntity(po);
    }

    @Override
    public FormDefinitionEntity queryFormDefinitionById(Long id) {
        FormDefinitionPO po = formDefinitionDao.selectById(id);
        return toEntity(po);
    }

    @Override
    public List<FormDefinitionEntity> queryFormDefinitionList() {
        List<FormDefinitionPO> poList = formDefinitionDao.selectList();
        List<FormDefinitionEntity> list = new ArrayList<>();
        for (FormDefinitionPO po : poList) {
            list.add(toEntity(po));
        }
        return list;
    }

    @Override
    public void updateFormStatus(Long id, Integer status) {
        formDefinitionDao.updateStatus(id, status);
    }

    @Override
    public void saveFormFields(List<FormFieldEntity> fields) {
        List<FormFieldPO> poList = new ArrayList<>();
        for (FormFieldEntity f : fields) {
            FormFieldPO po = new FormFieldPO();
            po.setFormId(f.getFormId());
            po.setFieldKey(f.getFieldKey());
            po.setFieldLabel(f.getFieldLabel());
            po.setFieldType(f.getFieldType());
            po.setRequired(f.getRequired());
            po.setSortOrder(f.getSortOrder());
            po.setOptionsJson(f.getOptionsJson());
            po.setPlaceholder(f.getPlaceholder());
            poList.add(po);
        }
        formFieldDao.insertBatch(poList);
    }

    @Override
    public List<FormFieldEntity> queryFormFields(Long formId) {
        List<FormFieldPO> poList = formFieldDao.selectByFormId(formId);
        List<FormFieldEntity> list = new ArrayList<>();
        for (FormFieldPO po : poList) {
            FormFieldEntity e = new FormFieldEntity();
            e.setId(po.getId());
            e.setFormId(po.getFormId());
            e.setFieldKey(po.getFieldKey());
            e.setFieldLabel(po.getFieldLabel());
            e.setFieldType(po.getFieldType());
            e.setRequired(po.getRequired());
            e.setSortOrder(po.getSortOrder());
            e.setOptionsJson(po.getOptionsJson());
            e.setPlaceholder(po.getPlaceholder());
            list.add(e);
        }
        return list;
    }

    @Override
    public Long saveSubmission(FormSubmissionEntity entity) {
        FormSubmissionPO po = new FormSubmissionPO();
        po.setFormId(entity.getFormId());
        po.setFormKey(entity.getFormKey());
        po.setSubmitData(entity.getSubmitData());
        po.setSubmitterName(entity.getSubmitterName());
        po.setSubmitterPhone(entity.getSubmitterPhone());
        po.setIpAddress(entity.getIpAddress());
        formSubmissionDao.insert(po);
        return po.getId();
    }

    @Override
    public List<FormSubmissionEntity> querySubmissions(Long formId) {
        List<FormSubmissionPO> poList = formSubmissionDao.selectByFormId(formId);
        List<FormSubmissionEntity> list = new ArrayList<>();
        for (FormSubmissionPO po : poList) {
            FormSubmissionEntity e = FormSubmissionEntity.builder()
                    .id(po.getId()).formId(po.getFormId()).formKey(po.getFormKey())
                    .submitData(po.getSubmitData()).submitterName(po.getSubmitterName())
                    .submitterPhone(po.getSubmitterPhone()).ipAddress(po.getIpAddress())
                    .createdAt(po.getCreatedAt()).build();
            list.add(e);
        }
        return list;
    }

    @Override
    public Long countSubmissions(Long formId) {
        return formSubmissionDao.countByFormId(formId);
    }

    private FormDefinitionPO toPO(FormDefinitionEntity e) {
        FormDefinitionPO po = new FormDefinitionPO();
        po.setId(e.getId());
        po.setFormKey(e.getFormKey());
        po.setTitle(e.getTitle());
        po.setDescription(e.getDescription());
        po.setStatus(e.getStatus());
        po.setShareLink(e.getShareLink());
        po.setCreatedBy(e.getCreatedBy());
        return po;
    }

    private FormDefinitionEntity toEntity(FormDefinitionPO po) {
        if (po == null) return null;
        return FormDefinitionEntity.builder()
                .id(po.getId()).formKey(po.getFormKey()).title(po.getTitle())
                .description(po.getDescription()).status(po.getStatus())
                .shareLink(po.getShareLink()).createdBy(po.getCreatedBy())
                .createdAt(po.getCreatedAt()).updatedAt(po.getUpdatedAt()).build();
    }
}
