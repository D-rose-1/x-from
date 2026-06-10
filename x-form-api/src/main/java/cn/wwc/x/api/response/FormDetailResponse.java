package cn.wwc.x.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormDetailResponse {
    private Long id;
    private String formKey;
    private String title;
    private String description;
    private Integer status;
    private String shareLink;
    private List<FieldVO> fields;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldVO {
        private String fieldKey;
        private String fieldLabel;
        private String fieldType;
        private Integer required;
        private String options;
        private String placeholder;
    }
}
