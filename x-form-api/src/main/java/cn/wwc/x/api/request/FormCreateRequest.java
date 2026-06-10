package cn.wwc.x.api.request;

import lombok.Data;
import java.util.List;

@Data
public class FormCreateRequest {
    private String title;
    private String description;
    private String createdBy;
    private List<FieldItem> fields;

    @Data
    public static class FieldItem {
        private String fieldKey;
        private String fieldLabel;
        private String fieldType;
        private Integer required;
        private String options;
        private String placeholder;
    }
}
