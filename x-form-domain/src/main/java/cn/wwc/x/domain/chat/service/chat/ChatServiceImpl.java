package cn.wwc.x.domain.chat.service.chat;

import cn.wwc.x.domain.chat.adapter.port.IOllamaPort;
import cn.wwc.x.domain.chat.model.entity.ChatMessageEntity;
import cn.wwc.x.domain.chat.model.entity.ChatSessionEntity;
import cn.wwc.x.domain.chat.model.valobj.MessageRoleEnumVO;
import cn.wwc.x.domain.chat.service.IChatService;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 智能客服对话服务实现
 *
 * @author xiaofuge
 */
@Slf4j
@Service
public class ChatServiceImpl implements IChatService {

    @Resource
    private IOllamaPort ollamaPort;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Value("${ollama.model:qwen2.5:0.5b}")
    private String modelName;

    private static final String CACHE_SESSION_PREFIX = "chat:session:";
    private static final long SESSION_TTL = 60; // 60 分钟

    /** 系统提示词，引导模型扮演客服角色并了解 x-form 平台 */
    private static final String SYSTEM_PROMPT = "你是一个友好、专业、热情的智能客服助手，你服务的平台叫「x-form」—— 一个在线表单搭建与数据收集平台。"
            + "请用简洁清晰的中文回答用户的问题，不要使用 markdown 格式。"
            + "如果遇到不了解的问题，请诚实告知并建议用户联系人工客服。"
            + "\n\n"
            + "【x-form 平台核心能力】\n"
            + "1. 创建自定义表单：支持单行文本、多行文本、数字、下拉选择、单选框、多选框、日期、邮箱等多种字段类型。每个字段可设置必填或非必填。\n"
            + "2. 表单发布与分享：创建后自动生成唯一的分享链接（格式：/user/?key=FORM_XXXXXXXXXXXX），用户通过链接即可填写表单。\n"
            + "3. 表单数据收集：用户提交后，管理员可以在管理端查看所有提交数据，包括提交人姓名、手机号、提交内容和提交时间。\n"
            + "4. 管理端功能：支持表单列表查看、创建新表单、查看提交数据。访问地址为 /admin/。\n"
            + "5. 用户端功能：通过分享链接打开表单页面，填写并提交表单。访问地址为 /user/?key=表单KEY。\n"
            + "\n"
            + "【回复规范】\n"
            + "- 用户询问如何创建表单时：引导到管理端 /admin/ 的「创建表单」页签，简要说明添加字段、发布即可获得分享链接。\n"
            + "- 用户询问如何填写表单时：说明需要管理员提供分享链接（包含 key 参数），点击链接即可填写。\n"
            + "- 用户询问表单有什么字段类型时：列出 8 种类型（单行文本、多行文本、数字、下拉选择、单选框、多选框、日期、邮箱），简要说明每种适用场景。\n"
            + "- 用户询问如何查看提交数据时：引导到管理端表单列表，点击「数据」按钮即可查看。\n"
            + "- 用户遇到技术问题时：提供排查建议，如检查链接是否正确、表单是否已发布等。\n"
            + "- 首次对话主动打招呼：简要介绍 x-form 平台和自己能提供的帮助。"
            + "回复时保持礼貌热情。";

    @Override
    public ChatSessionEntity chat(String sessionId, String message) {
        // 1. 获取或创建会话
        ChatSessionEntity session = loadSession(sessionId);

        // 2. 构造消息列表（系统提示词 + 历史 + 当前）
        List<ChatMessageEntity> messages = buildMessageList(session, message);

        // 3. 调用 Ollama 大模型
        ChatMessageEntity reply = ollamaPort.chat(modelName, messages);
        log.info("Ollama 回复: sessionId={}, tokens≈{}", session.getSessionId(),
                reply.getContent() != null ? reply.getContent().length() : 0);

        // 4. 保存用户消息和 AI 回复到会话
        session.addMessage(ChatMessageEntity.builder()
                .role(MessageRoleEnumVO.USER.getCode())
                .content(message).build());
        session.addMessage(reply);

        // 5. 缓存会话到 Redis
        saveSession(session);

        return session;
    }

    @Override
    public ChatSessionEntity getSession(String sessionId) {
        return loadSession(sessionId);
    }

    // ==================== 私有方法 ====================

    /**
     * 构建消息列表：系统提示词 + 历史消息 + 当前用户消息
     */
    private List<ChatMessageEntity> buildMessageList(ChatSessionEntity session, String currentMessage) {
        List<ChatMessageEntity> messages = new ArrayList<>();

        // 系统提示词（只在首次对话时添加）
        if (session.getMessages().isEmpty()) {
            messages.add(ChatMessageEntity.builder()
                    .role(MessageRoleEnumVO.SYSTEM.getCode())
                    .content(SYSTEM_PROMPT).build());
        }

        // 历史消息（最近 20 轮，防止 token 溢出）
        List<ChatMessageEntity> history = session.getMessages();
        int maxHistory = Math.min(history.size(), 20);
        if (maxHistory > 0) {
            messages.addAll(history.subList(history.size() - maxHistory, history.size()));
        }

        // 当前用户消息
        messages.add(ChatMessageEntity.builder()
                .role(MessageRoleEnumVO.USER.getCode())
                .content(currentMessage).build());

        return messages;
    }

    /**
     * 从 Redis 加载会话，不存在则创建新会话
     */
    private ChatSessionEntity loadSession(String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            String cached = redisTemplate.opsForValue().get(CACHE_SESSION_PREFIX + sessionId);
            if (cached != null) {
                return JSON.parseObject(cached, ChatSessionEntity.class);
            }
        }
        // 创建新会话
        return ChatSessionEntity.builder()
                .sessionId("CS_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase())
                .messages(new ArrayList<>())
                .build();
    }

    /**
     * 保存会话到 Redis
     */
    private void saveSession(ChatSessionEntity session) {
        redisTemplate.opsForValue().set(
                CACHE_SESSION_PREFIX + session.getSessionId(),
                JSON.toJSONString(session),
                SESSION_TTL,
                TimeUnit.MINUTES
        );
    }

}
