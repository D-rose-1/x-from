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
public class FormDefinitionEntity {
    private Long id;
    private String formKey;
    private String title;
    private String description;
    private Integer status; // 1-草稿 2-已发布 3-已关闭
    private String shareLink;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
