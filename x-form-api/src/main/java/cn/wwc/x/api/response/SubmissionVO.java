package cn.wwc.x.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionVO {
    private Long id;
    private Long formId;
    private String formKey;
    private String submitData;
    private String submitterName;
    private String submitterPhone;
    private String ipAddress;
    private LocalDateTime createdAt;
}
