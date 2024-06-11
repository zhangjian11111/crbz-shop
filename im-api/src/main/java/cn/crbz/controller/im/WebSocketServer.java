package cn.crbz.controller.im;

import cn.hutool.json.JSONUtil;
import cn.crbz.cache.Cache;
import cn.crbz.common.security.AuthUser;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.security.enums.UserEnums;
import cn.crbz.modules.im.config.CustomSpringConfigurator;
import cn.crbz.modules.im.entity.dos.ImMessage;
import cn.crbz.modules.im.entity.dos.ImTalk;
import cn.crbz.modules.im.entity.enums.MessageResultType;
import cn.crbz.modules.im.entity.vo.MessageOperation;
import cn.crbz.modules.im.entity.vo.MessageVO;
import cn.crbz.modules.im.service.ImMessageService;
import cn.crbz.modules.im.service.ImTalkService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author liushuai
 */
@Component
@ServerEndpoint(value = "/crbz/webSocket/{accessToken}", configurator = CustomSpringConfigurator.class)
@Scope("prototype")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSocketServer {
    /**
     * 在线人数 PS 注意，只能单节点，如果多节点部署需要自行寻找方案
     */
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();
    /**
     * 消息服务
     */
    private final ImMessageService imMessageService;
    private final ImTalkService imTalkService;
    private final Cache cache;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("accessToken") String accessToken, Session session) {

        AuthUser authUser = UserContext.getAuthUser(cache, accessToken);

        String sessionId = UserEnums.STORE.equals(authUser.getRole()) ? authUser.getStoreId() : authUser.getId();
        //如果已有会话，则进行下线提醒。
        if (sessionPools.containsKey(sessionId)) {
            log.info("用户重复登陆，旧用户下线");
            Session oldSession = sessionPools.get(sessionId);
            sendMessage(oldSession,
                    MessageVO.builder().messageResultType(MessageResultType.OFFLINE).result("用户异地登陆").build());
            try {
                oldSession.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sessionPools.put(sessionId, session);
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam("accessToken") String accessToken) {
        AuthUser authUser = UserContext.getAuthUser(accessToken);
        log.info("用户断开断开连接:{}", JSONUtil.toJsonStr(authUser));
        sessionPools.remove(authUser);
    }

    /**
     * 发送消息
     *
     * @param msg
     * @throws IOException
     */
    @OnMessage
    public void onMessage(@PathParam("accessToken") String accessToken, String msg) {
        log.info("发送消息：{}", msg);
        MessageOperation messageOperation = JSON.parseObject(msg, MessageOperation.class);
        operation(accessToken, messageOperation);
    }

    /**
     * IM操作
     *
     * @param accessToken
     * @param messageOperation
     */
    private void operation(String accessToken, MessageOperation messageOperation) {

        AuthUser authUser = UserContext.getAuthUser(accessToken);
        switch (messageOperation.getOperationType()) {
            case PING:
                break;
            case MESSAGE:
                //保存消息
                ImMessage imMessage = new ImMessage(messageOperation);
                imMessageService.save(imMessage);
                //修改最后消息信息
                imTalkService.update(new LambdaUpdateWrapper<ImTalk>().eq(ImTalk::getId, messageOperation.getTalkId())
                        .set(ImTalk::getLastTalkMessage, messageOperation.getContext())
                        .set(ImTalk::getLastTalkTime, imMessage.getCreateTime())
                        .set(ImTalk::getLastMessageType, imMessage.getMessageType()));
                //发送消息
                sendMessage(messageOperation.getTo(), new MessageVO(MessageResultType.MESSAGE, imMessage));
                break;
            case READ:
                if (!StringUtils.isEmpty(messageOperation.getContext())) {
                    imMessageService.read(messageOperation.getTalkId(), accessToken);
                }
                break;
            case UNREAD:
                sendMessage(authUser.getId(),
                        new MessageVO(MessageResultType.UN_READ, imMessageService.unReadMessages(accessToken)));
                break;
            case HISTORY:
                sendMessage(authUser.getId(), new MessageVO(MessageResultType.HISTORY,
                        imMessageService.historyMessage(accessToken, messageOperation.getTo())));
                break;
            default:
                break;
        }
    }

    /**
     * 发送消息
     *
     * @param sessionId sessionId
     * @param message   消息对象
     */
    private void sendMessage(String sessionId, MessageVO message) {
        Session session = sessionPools.get(sessionId);
        sendMessage(session, message);
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param message 消息对象
     */
    private void sendMessage(Session session, MessageVO message) {
        if (session != null) {
            try {
                session.getBasicRemote().sendText(JSON.toJSONString(message, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * socket exception
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("socket异常: {}", session.getId(), throwable);
    }

}
