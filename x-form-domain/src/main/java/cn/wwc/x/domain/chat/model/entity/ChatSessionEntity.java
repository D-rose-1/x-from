package cn.wwc.x.domain.chat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天会话实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionEntity {

    /** 会话ID */
    private String sessionId;

    /** 历史消息列表 */
    @Builder.Default
    private List<ChatMessageEntity> messages = new ArrayList<>();

    /** 增加消息到历史 */
    public void addMessage(ChatMessageEntity message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }

}
