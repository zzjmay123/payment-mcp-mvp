package com.payment.mcp.controller;

import com.payment.mcp.protocol.JsonRpcHandler;
import com.payment.mcp.protocol.JsonRpcRequest;
import com.payment.mcp.protocol.JsonRpcResponse;
import com.payment.mcp.protocol.SessionStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

/**
 * SSE 控制器 - MCP 协议入口
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SseController {

    private final JsonRpcHandler jsonRpcHandler;

    /**
     * 建立 SSE 连接
     * GET /sse
     */
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        String sessionId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(300000L); // 5 分钟超时

        // 存储到 Session
        SessionStore.put(sessionId, emitter);
        log.info("New SSE connection: {}", sessionId);

        // 连接完成回调
        emitter.onCompletion(() -> {
            log.info("SSE connection completed: {}", sessionId);
            SessionStore.remove(sessionId);
        });

        // 超时回调
        emitter.onTimeout(() -> {
            log.warn("SSE connection timeout: {}", sessionId);
            SessionStore.remove(sessionId);
            try {
                emitter.complete();
            } catch (Exception e) {
                // ignore
            }
        });

        // 错误回调
        emitter.onError(ex -> {
            log.error("SSE connection error: {}", sessionId, ex);
            SessionStore.remove(sessionId);
        });

        // 发送 endpoint 事件
        try {
            emitter.send(SseEmitter.event()
                .name("endpoint")
                .data("/mcp?session=" + sessionId));
            log.debug("Sent endpoint to session: {}", sessionId);
        } catch (IOException e) {
            log.error("Failed to send endpoint", e);
        }

        return emitter;
    }

    /**
     * 接收 MCP 消息（SSE 模式）
     * POST /mcp
     */
    @PostMapping(value = "/mcp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleMessage(
            @RequestParam String session,
            @RequestBody String jsonBody
    ) {
        log.debug("Received message for session {}: {}", session, jsonBody);

        // 检查 Session 是否存在
        SseEmitter emitter = SessionStore.get(session);
        if (emitter == null) {
            log.warn("Session not found: {}", session);
            return ResponseEntity.status(404).body("{\"error\":\"Session not found\"}");
        }

        try {
            // 解析 JSON-RPC 请求
            JsonRpcRequest request = jsonRpcHandler.parseRequest(jsonBody);
            
            // 处理请求
            JsonRpcResponse response = jsonRpcHandler.handleRequest(request);
            
            // 通过 SSE 发送响应
            emitter.send(SseEmitter.event()
                .name("message")
                .data(jsonRpcHandler.toJson(response)));
            
            log.debug("Sent response: {}", response);
            return ResponseEntity.accepted().build();
            
        } catch (Exception e) {
            log.error("Error handling message", e);
            JsonRpcResponse errorResponse = JsonRpcResponse.error(
                null,
                -32603,
                "Internal error",
                e.getMessage()
            );
            try {
                emitter.send(SseEmitter.event()
                    .name("message")
                    .data(jsonRpcHandler.toJson(errorResponse)));
            } catch (Exception ex) {
                // ignore
            }
            return ResponseEntity.status(500).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * 接收 MCP 消息（HTTP 短连接模式 - 推荐）
     * POST /api/mcp
     * 直接返回响应，不需要 SSE 会话
     */
    @PostMapping(value = "/api/mcp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleHttpMessage(
            @RequestBody String jsonBody
    ) {
        log.debug("Received HTTP message: {}", jsonBody);

        try {
            // 解析 JSON-RPC 请求
            JsonRpcRequest request = jsonRpcHandler.parseRequest(jsonBody);
            
            // 处理请求
            JsonRpcResponse response = jsonRpcHandler.handleRequest(request);
            
            log.debug("Sent HTTP response: {}", response);
            return ResponseEntity.ok(jsonRpcHandler.toJson(response));
            
        } catch (Exception e) {
            log.error("Error handling HTTP message", e);
            try {
                JsonRpcResponse errorResponse = JsonRpcResponse.error(
                    null,
                    -32603,
                    "Internal error",
                    e.getMessage()
                );
                return ResponseEntity.status(500).body(jsonRpcHandler.toJson(errorResponse));
            } catch (Exception ex) {
                return ResponseEntity.status(500).body("{\"error\":\"" + ex.getMessage() + "\"}");
            }
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"sessions\":" + SessionStore.size() + "}");
    }
}
