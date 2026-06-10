package cn.wwc.x.domain.form.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldEntity {
    private Long id;
    private Long formId;
    private String fieldKey;
    private String fieldLabel;
    private String fieldType; // text/textarea/number/select/radio/checkbox/date/email
    private Integer required; // 0-否 1-是
    private Integer sortOrder;
    private String optionsJson;
    private String placeholder;
}
