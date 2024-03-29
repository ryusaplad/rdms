package svfc_rdms.rdms.config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class DataWebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void sendData(String data) {
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(data));
            } catch (IOException e) {
             System.out.println(e.getMessage());
            }
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        try {
            session.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
