
package com.project.CyShare.websockets;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@ServerEndpoint("/websocket/{user}")
@Component
public class WebSocketServer
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
    public void onMessage(Session session, String response)
    {
        logger.info("Entered into onMessage.");
        String username = sessionUserMap.get(session);
        if (response.equals("1"))
            broadcast("1");
        else
            broadcast("0");
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
        try{
            session.getBasicRemote().sendText(userName + " disconnected.");
        }
        catch (IOException e)
        {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
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

