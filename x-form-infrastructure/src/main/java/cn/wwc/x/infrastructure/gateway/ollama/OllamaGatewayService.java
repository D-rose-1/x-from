package cn.wwc.x.infrastructure.gateway.ollama;

import cn.wwc.x.infrastructure.gateway.ollama.dto.OllamaChatRequestDTO;
import cn.wwc.x.infrastructure.gateway.ollama.dto.OllamaChatResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;

/**
 * Ollama API 网关服务 — 封装对 Ollama 的 HTTP 调用
 */
@Slf4j
@Component
public class OllamaGatewayService {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 调用 Ollama /api/chat 接口
     *
     * @param baseUrl Ollama 服务地址
     * @param request 聊天请求
     * @return 聊天响应
     */
    public OllamaChatResponseDTO chat(String baseUrl, OllamaChatRequestDTO request) {
        String url = baseUrl + "/api/chat";
        log.debug("调用 Ollama API: url={}, model={}, messages={}", url, request.getModel(), request.getMessages().size());

        try {
            OllamaChatResponseDTO response = restTemplate.postForObject(url, request, OllamaChatResponseDTO.class);
            if (response == null) {
                throw new RuntimeException("Ollama 返回为空");
            }
            log.debug("Ollama 响应: done={}, tokens={}, duration={}ms",
                    response.isDone(),
                    response.getEvalCount(),
                    response.getEvalDuration() != null ? response.getEvalDuration() / 1_000_000 : 0);
            return response;
        } catch (Exception e) {
            log.error("调用 Ollama API 失败: url={}", url, e);
            throw new RuntimeException("智能客服暂时不可用，请稍后再试: " + e.getMessage(), e);
        }
    }

}
