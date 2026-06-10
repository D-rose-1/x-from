package cn.wwc.x.infrastructure.adapter.port;

import cn.wwc.x.domain.chat.adapter.port.IOllamaPort;
import cn.wwc.x.domain.chat.model.entity.ChatMessageEntity;
import cn.wwc.x.infrastructure.gateway.ollama.OllamaGatewayService;
import cn.wwc.x.infrastructure.gateway.ollama.dto.OllamaChatRequestDTO;
import cn.wwc.x.infrastructure.gateway.ollama.dto.OllamaChatResponseDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Ollama Port 适配器实现 — 实现 Domain 层定义的 IOllamaPort 接口
 */
@Slf4j
@Component
public class OllamaPort implements IOllamaPort {

    @Resource
    private OllamaGatewayService ollamaGatewayService;

    @Value("${ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Override
    public ChatMessageEntity chat(String model, List<ChatMessageEntity> messages) {
        // 1. 构建 Ollama 请求
        OllamaChatRequestDTO request = OllamaChatRequestDTO.builder()
                .model(model)
                .messages(toMessageDTOs(messages))
                .stream(false)
                .build();

        // 2. 调用 Ollama 网关
        OllamaChatResponseDTO response = ollamaGatewayService.chat(baseUrl, request);

        // 3. 转换响应为领域实体
        if (response.getMessage() == null || response.getMessage().getContent() == null) {
            return ChatMessageEntity.builder()
                    .role("assistant")
                    .content("抱歉，我暂时无法回答您的问题，请稍后再试。")
                    .build();
        }

        return ChatMessageEntity.builder()
                .role(response.getMessage().getRole())
                .content(response.getMessage().getContent())
                .build();
    }

    // ==================== 私有方法 ====================

    private List<OllamaChatRequestDTO.MessageDTO> toMessageDTOs(List<ChatMessageEntity> entities) {
        List<OllamaChatRequestDTO.MessageDTO> dtos = new ArrayList<>();
        for (ChatMessageEntity entity : entities) {
            dtos.add(OllamaChatRequestDTO.MessageDTO.builder()
                    .role(entity.getRole())
                    .content(entity.getContent())
                    .build());
        }
        return dtos;
    }

}
