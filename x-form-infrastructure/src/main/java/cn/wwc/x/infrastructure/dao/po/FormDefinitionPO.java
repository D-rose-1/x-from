package cn.wwc.x.infrastructure.dao.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FormDefinitionPO {
    private Long id;
    private String formKey;
    private String title;
    private String description;
    private Integer status;
    private String shareLink;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
