package cn.wwc.x.api.request;

import lombok.Data;
import java.util.Map;

@Data
public class FormSubmitRequest {
    private String formKey;
    private Map<String, Object> data;
    private String submitterName;
    private String submitterPhone;
}
