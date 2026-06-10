package cn.wwc.x.domain.form.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormStatusEnumVO {
    DRAFT(1, "草稿"),
    PUBLISHED(2, "已发布"),
    CLOSED(3, "已关闭");

    private final Integer code;
    private final String desc;

    public static FormStatusEnumVO of(Integer code) {
        for (FormStatusEnumVO v : values()) {
            if (v.code.equals(code)) return v;
        }
        return DRAFT;
    }
}
