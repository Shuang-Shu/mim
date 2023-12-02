package com.mdc.mim.netty.session;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// TODO 未来可实现分布式的session管理
@Component
public class ServerSessionManager {
    private ConcurrentMap<String, ServerSession> sessionMap = new ConcurrentHashMap<>();
    private ConcurrentMap<Long, List<ServerSession>> uidSessionMap = new ConcurrentHashMap<>();

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
        var userSessions = uidSessionMap.get(uid);
        if (userSessions == null) {
            return new ArrayList<>();
        }
        return userSessions;
    }

    /**
     * @description: 用户成功登录，在uidSessionMap中添加用户会话
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/11/26 12:01
     */
    public void userLogIn(ServerSession serverSession) {
        var uid = serverSession.getUser().getUid();
        var sessions = uidSessionMap.get(uid);
        if (sessions == null) {
            uidSessionMap.put(uid, new ArrayList<>());
            sessions = uidSessionMap.get(uid);
        }
        sessions.add(serverSession);
    }

    public void userLogOut(ServerSession serverSession) {
        var uid = serverSession.getUser().getUid();
        var sessions = uidSessionMap.get(uid);
        if (sessions == null) {
            return;
        }
        sessions.remove(serverSession);
        if (sessions.isEmpty()) {
            uidSessionMap.remove(uid);
        }
    }
}
