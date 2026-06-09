package cn.wwc.x.infrastructure.dao.po;

import lombok.Data;

@Data
public class FormFieldPO {
    private Long id;
    private Long formId;
    private String fieldKey;
    private String fieldLabel;
    private String fieldType;
    private Integer required;
    private Integer sortOrder;
    private String optionsJson;
    private String placeholder;
}
