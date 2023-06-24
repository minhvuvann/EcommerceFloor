package vn.mellow.ecom.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vn.mellow.ecom.manager.MessageManager;
import vn.mellow.ecom.model.websocket.Message;
import vn.mellow.ecom.model.websocket.MessageDTO;

import java.io.IOException;
import java.util.*;

/**
 * @author : Vũ Văn Minh
 * @mailto : duanemellow19@gmail.com
 * @created : 04/06/2023, Chủ Nhật
 **/
@Slf4j
public class ChatHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    @Autowired
    private MessageManager messageManager;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String currentId = extractUserId(session);
        String[] parts = currentId.split("_");
        String userId = "";
        String senderId = "";
        if (parts.length == 2) {
            userId = parts[0];
            senderId = parts[1];
            sendUserChatHistory(userId, senderId, session);
        }
        sessions.put(userId, session);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String currentId = extractUserId(session);
        String[] parts = currentId.split("_");
        String userId = "";
        String senderId = "";
        if (parts.length == 2) {
            userId = parts[0];
            senderId = parts[1];
        }
        String content = message.getPayload();
        if (!content.equals("MESSAGES_LIST")) {
            Message addMess = new Message();
            addMess.setUserId(userId);
            addMess.setContent(content);
            addMess.setSenderId(senderId);
            //Tạo message
            messageManager.createMessage(addMess);
            log.info("SEND_MESSAGE", "Send from :" + userId + " to " + senderId + " with content :" + content);
            sendMessageToOtherUser(userId, senderId);
        }
        sendUserChatHistory(userId, senderId, session);

    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = extractUserId(session);
        sessions.remove(userId);
    }

    private String extractUserId(WebSocketSession session) {
        String uri = session.getUri().getPath();
        String[] parts = uri.split("/");
        return parts[parts.length - 1]; // Lấy userId từ phần tử cuối cùng của mảng parts
    }

    private void sendMessageToOtherUser(String userId, String senderId) throws IOException {
        sessions.forEach((sessionId, session) -> {
            if (sessionId.equals(senderId)) {
                try {
                    List<Message> chatHistory = messageManager.getMessages(userId, senderId);
                    session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(chatHistory)));
                    log.info(session.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendUserChatHistory(String userId, String senderId, WebSocketSession session) throws IOException {
        List<Message> chatHistory = messageManager.getMessages(userId, senderId);
        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(chatHistory)));

    }

}

