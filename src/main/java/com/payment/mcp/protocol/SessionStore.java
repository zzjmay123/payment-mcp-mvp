package com.payment.mcp.protocol;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session 存储（内存版）
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
public class SessionStore {

    private static final Map<String, SseEmitter> sessions = new ConcurrentHashMap<>();

    public static void put(String sessionId, SseEmitter emitter) {
        sessions.put(sessionId, emitter);
    }

    public static SseEmitter get(String sessionId) {
        return sessions.get(sessionId);
    }

    public static SseEmitter remove(String sessionId) {
        return sessions.remove(sessionId);
    }

    public static int size() {
        return sessions.size();
    }

    public static boolean contains(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}
