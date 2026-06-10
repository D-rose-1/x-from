package cn.wwc.x.infrastructure.gateway.ollama.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Ollama Chat API 响应 DTO
 */
@Data
public class OllamaChatResponseDTO {

    private String model;

    @JSONField(name = "created_at")
    private String createdAt;

    private MessageDTO message;

    private boolean done;

    @JSONField(name = "done_reason")
    private String doneReason;

    @JSONField(name = "total_duration")
    private Long totalDuration;

    @JSONField(name = "eval_count")
    private Integer evalCount;

    @JSONField(name = "eval_duration")
    private Long evalDuration;

    @Data
    public static class MessageDTO {
        private String role;
        private String content;
    }

}
