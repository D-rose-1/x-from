package cn.wwc.x.domain.chat.adapter.port;

import cn.wwc.x.domain.chat.model.entity.ChatMessageEntity;

import java.util.List;

/**
 * Ollama 模型调用端口接口
 */
public interface IOllamaPort {

    /**
     * 向大模型发送聊天请求
     *
     * @param model    模型名称
     * @param messages 消息列表（含历史）
     * @return AI 回复消息
     */
    ChatMessageEntity chat(String model, List<ChatMessageEntity> messages);

}
