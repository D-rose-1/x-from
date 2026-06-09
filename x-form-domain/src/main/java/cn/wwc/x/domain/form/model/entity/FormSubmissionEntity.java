package cn.wwc.x.domain.form.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionEntity {
    private Long id;
    private Long formId;
    private String formKey;
    private String submitData; // JSON string
    private String submitterName;
    private String submitterPhone;
    private String ipAddress;
    private LocalDateTime createdAt;
}
