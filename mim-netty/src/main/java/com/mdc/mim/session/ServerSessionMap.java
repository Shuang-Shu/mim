package com.mdc.mim.session;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mdc.mim.entity.UserEntity;

public class ServerSessionMap {
    private ServerSessionMap() {
    }

    ConcurrentMap<String, ServerSession> sessionMap = new ConcurrentHashMap<>();

    private static ServerSessionMap instance = new ServerSessionMap();

    public static ServerSessionMap instance() {
        return instance;
    }

    public ServerSession getSession(String sessionId) {
        if (sessionMap.containsKey(sessionId)) {
            return sessionMap.get(sessionId);
        } else {
            return null;
        }
    }

    public void addSession(String sessionId, ServerSession session) {
        sessionMap.put(sessionId, session);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    /**
     * 获取user对应的所有会话
     * 
     * @param user
     * @return
     */
    public List<ServerSession> getSessionBy(UserEntity user) {
        return null;
    }
}
