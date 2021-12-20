package com.project.CyShare.websockets;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@ServerEndpoint("/chat/{user}")
@Component
public class ChatWebSocket
{
    private static Map<Session, String> sessionUserMap = new Hashtable<>();
    private static Map<String, Session> userSessionMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("user") String userName) throws IOException
    {
        logger.info("Entered into onOpen.");
        sessionUserMap.put(session, userName);
        userSessionMap.put(userName, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException
    {
        logger.info("Entered into onMessage.");
        String username = sessionUserMap.get(session);
        String temp = username + ": " + message;
        broadcast(temp);
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        logger.info("Entered into onError.");
    }

    @OnClose
    public void onClose(Session session) throws IOException
    {
        logger.info("Entered into onClose.");
        String userName = sessionUserMap.get(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(userName);
        String temp = userName + " disconnected.";
        broadcast(temp);
    }

    private void broadcast(String message) {
        sessionUserMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }

}

