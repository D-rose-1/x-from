package cn.wwc.x.domain.chat.model.valobj;

/**
 * 消息角色枚举值对象
 */
public enum MessageRoleEnumVO {

    USER("user", "用户"),
    ASSISTANT("assistant", "客服助手"),
    SYSTEM("system", "系统");

    private final String code;
    private final String desc;

    MessageRoleEnumVO(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
