package cn.wwc.x.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Ollama 配置 — 提供 RestTemplate Bean
 */
@Configuration
public class OllamaConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 连接超时 10s
        factory.setConnectTimeout(10_000);
        // 读取超时 120s（大模型推理可能较慢）
        factory.setReadTimeout(120_000);
        return new RestTemplate(factory);
    }

}
