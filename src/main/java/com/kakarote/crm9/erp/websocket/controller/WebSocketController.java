package com.kakarote.crm9.erp.websocket.controller;

import BP.Tools.StringUtils;
import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@ServerEndpoint(value="/websocket.ws/{userId}")
public class WebSocketController extends Controller{

    private static final Map<String, Session> userSocketSessionMap;

    static {
        userSocketSessionMap = new HashMap<String, Session>();
    }

    /**
     * 给单个用户发送消息
     */
    public synchronized void sendMsg(Session session,String userId, String message) {
        try {
            if(session==null){
                session = userSocketSessionMap.get(userId);
            }
            if (session != null && session.isOpen()) {
                //回复消息
                session.getAsyncRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("WebSocket:用户[" + userId+ "]发送消息异常", e);
        }
    }

    /**
     * 接收消息
     * @param message
     * @param session
     */
    @OnMessage
    public void message(String message, Session session) {
        //这里可以接收到前台JS发送的数据(接收消息后回复)
        //if("在吗？".equals(message)) {
        //	session.getAsyncRemote().sendText("在的");
        //}
        //也可建立连接后主动发消息
        //session.getAsyncRemote().sendText(message);
        //群发消息
        ArrayList<Session> list = new ArrayList<>(userSocketSessionMap.values());
        for(int i=0;i<list.size();i++){
            Session temp = list.get(i);
            if(session.getId()!=temp.getId()){
                sendMsg(temp,null, message);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, EndpointConfig config) throws Exception{
        if(StringUtils.isNotBlank(userId)) {
            userSocketSessionMap.put(userId,session);
            sendMsg(null,userId, "连接已建立");
        }else{
            log.error("WebSocket:用户未登录，无法建立连接");
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        log.info("WebSocket: " + session.getId() + "已经关闭");
        Iterator<Map.Entry<String, Session>> it = userSocketSessionMap.entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Map.Entry<String, Session> entry = it.next();
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                log.debug("WebSocket会话已经移除:用户ID " + entry.getKey());
                break;
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        Iterator<Map.Entry<String, Session>> it = userSocketSessionMap.entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Map.Entry<String, Session> entry = it.next();
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                log.debug("WebSocket会话已经移除:用户ID " + entry.getKey());
                break;
            }
        }
    }

}
