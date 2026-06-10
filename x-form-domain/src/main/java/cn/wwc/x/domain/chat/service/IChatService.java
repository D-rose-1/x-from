package cn.wwc.x.domain.chat.service;

import cn.wwc.x.domain.chat.model.entity.ChatMessageEntity;
import cn.wwc.x.domain.chat.model.entity.ChatSessionEntity;

/**
 * 智能客服对话服务接口
 */
public interface IChatService {

    /**
     * 发送消息并获取回复
     *
     * @param sessionId 会话ID（首次为null则自动创建）
     * @param message   用户消息
     * @return 会话实体（含历史消息）
     */
    ChatSessionEntity chat(String sessionId, String message);

    /**
     * 获取会话历史
     *
     * @param sessionId 会话ID
     * @return 会话实体，不存在返回 null
     */
    ChatSessionEntity getSession(String sessionId);

}
