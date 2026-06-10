package cn.wwc.x.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 智能客服对话请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    /** 会话ID（首次对话为空，后续对话必传） */
    private String sessionId;

    /** 用户消息内容 */
    private String message;

}
