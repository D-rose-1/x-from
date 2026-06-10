package cn.wwc.x.domain.form.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldTypeEnumVO {
    TEXT("text", "单行文本"),
    TEXTAREA("textarea", "多行文本"),
    NUMBER("number", "数字"),
    SELECT("select", "下拉选择"),
    RADIO("radio", "单选框"),
    CHECKBOX("checkbox", "多选框"),
    DATE("date", "日期"),
    EMAIL("email", "邮箱");

    private final String code;
    private final String desc;

    public static FieldTypeEnumVO of(String code) {
        for (FieldTypeEnumVO v : values()) {
            if (v.code.equals(code)) return v;
        }
        return TEXT;
    }
}
