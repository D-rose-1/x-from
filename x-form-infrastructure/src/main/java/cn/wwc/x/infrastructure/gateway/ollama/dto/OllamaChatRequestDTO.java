package cn.wwc.x.infrastructure.gateway.ollama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Ollama Chat API 请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaChatRequestDTO {

    /** 模型名称 */
    private String model;

    /** 消息列表 */
    private List<MessageDTO> messages;

    /** 是否流式输出 */
    private boolean stream;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDTO {
        private String role;
        private String content;
    }

}
