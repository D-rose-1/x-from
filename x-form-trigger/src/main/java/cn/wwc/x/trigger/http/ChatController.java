package cn.wwc.x.trigger.http;

import cn.wwc.x.api.request.ChatRequest;
import cn.wwc.x.api.response.ChatResponse;
import cn.wwc.x.api.response.Response;
import cn.wwc.x.domain.chat.model.entity.ChatMessageEntity;
import cn.wwc.x.domain.chat.model.entity.ChatSessionEntity;
import cn.wwc.x.domain.chat.service.IChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 智能客服对话接口
 *
 * @author xiaofuge
 */
@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Resource
    private IChatService chatService;

    /**
     * 发送消息
     *
     * @param request 包含 sessionId（可选）和 message
     * @return AI 回复 + 历史消息
     */
    @PostMapping("/send")
    public Response<ChatResponse> send(@RequestBody ChatRequest request) {
        try {
            // 参数校验
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return Response.<ChatResponse>builder()
                        .code("0001").info("消息不能为空").build();
            }

            // 调用领域服务
            ChatSessionEntity session = chatService.chat(request.getSessionId(), request.getMessage());

            // 获取 AI 最后一条回复
            String reply = session.getMessages().isEmpty() ? "" :
                    session.getMessages().get(session.getMessages().size() - 1).getContent();

            // 构建响应
            ChatResponse response = ChatResponse.builder()
                    .sessionId(session.getSessionId())
                    .reply(reply)
                    .history(session.getMessages().stream()
                            .map(m -> ChatResponse.MessageVO.builder()
                                    .role(m.getRole())
                                    .content(m.getContent())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            return Response.<ChatResponse>builder()
                    .code("0000").info("成功").data(response).build();
        } catch (Exception e) {
            log.error("智能客服对话异常", e);
            return Response.<ChatResponse>builder()
                    .code("0001").info(e.getMessage()).build();
        }
    }

    /**
     * 获取会话历史
     *
     * @param sessionId 会话ID
     * @return 历史消息
     */
    @GetMapping("/session/{sessionId}")
    public Response<ChatResponse> getSession(@PathVariable("sessionId") String sessionId) {
        try {
            ChatSessionEntity session = chatService.getSession(sessionId);
            if (session == null) {
                return Response.<ChatResponse>builder()
                        .code("0001").info("会话不存在或已过期").build();
            }

            ChatResponse response = ChatResponse.builder()
                    .sessionId(session.getSessionId())
                    .history(session.getMessages().stream()
                            .map(m -> ChatResponse.MessageVO.builder()
                                    .role(m.getRole())
                                    .content(m.getContent())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            return Response.<ChatResponse>builder()
                    .code("0000").info("成功").data(response).build();
        } catch (Exception e) {
            log.error("获取会话历史异常", e);
            return Response.<ChatResponse>builder()
                    .code("0001").info(e.getMessage()).build();
        }
    }

}
