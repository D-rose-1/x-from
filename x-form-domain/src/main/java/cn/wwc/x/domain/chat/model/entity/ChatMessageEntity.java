package cn.wwc.x.domain.chat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天消息实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity {

    /** 角色：user / assistant */
    private String role;

    /** 消息内容 */
    private String content;

}
