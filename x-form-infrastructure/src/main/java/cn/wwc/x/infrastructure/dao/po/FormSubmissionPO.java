package cn.wwc.x.infrastructure.dao.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FormSubmissionPO {
    private Long id;
    private Long formId;
    private String formKey;
    private String submitData;
    private String submitterName;
    private String submitterPhone;
    private String ipAddress;
    private LocalDateTime createdAt;
}
