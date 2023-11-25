package com.mdc.mim.netty.session;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

// TODO 未来可实现分布式的session管理
@Component
public class ServerSessionManager {
    ConcurrentMap<String, ServerSession> sessionMap = new ConcurrentHashMap<>();

    public boolean contains(String sessionId) {
        return sessionId != null && sessionMap.containsKey(sessionId);
    }

    public ServerSession getSession(String sessionId) {
        if (sessionMap.containsKey(sessionId)) {
            return sessionMap.get(sessionId);
        } else {
            return null;
        }
    }

    public void addSession(String sessionId, ServerSession serverSession) {
        sessionMap.put(sessionId, serverSession);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    /**
     * 获取user对应的所有会话
     *
     * @param uid
     * @return
     */
    public List<ServerSession> getSessionsByUid(Long uid) {
        return sessionMap.values().stream().filter(session -> {
            if (session.getUser() == null || !session.getUser().getUid().equals(uid)) {
                return false;
            }
            return true;
        }).toList();
    }
}
