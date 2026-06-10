package cn.wwc.x.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 智能客服对话响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    /** 会话ID */
    private String sessionId;

    /** AI 回复内容 */
    private String reply;

    /** 历史消息（可选，用于前端展示） */
    private List<MessageVO> history;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageVO {
        private String role;
        private String content;
    }

}
