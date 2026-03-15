package com.payment.mcp.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.mcp.tools.CreatePaymentTool;
import com.payment.mcp.tools.QueryBalanceTool;
import com.payment.mcp.tools.QueryOrderTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * JSON-RPC 2.0 处理器
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonRpcHandler {

    private final ObjectMapper objectMapper;
    private final QueryBalanceTool queryBalanceTool;
    private final CreatePaymentTool createPaymentTool;
    private final QueryOrderTool queryOrderTool;

    /**
     * 解析 JSON-RPC 请求
     */
    public JsonRpcRequest parseRequest(String json) throws Exception {
        return objectMapper.readValue(json, JsonRpcRequest.class);
    }

    /**
     * 对象转 JSON
     */
    public String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 处理请求
     */
    public JsonRpcResponse handleRequest(JsonRpcRequest request) {
        log.debug("Handling method: {}", request.getMethod());

        try {
            // 验证 JSON-RPC 版本
            if (!"2.0".equals(request.getJsonrpc())) {
                return JsonRpcResponse.error(
                    request.getId(),
                    -32600,
                    "Invalid Request",
                    "Unsupported JSON-RPC version: " + request.getJsonrpc()
                );
            }

            // 路由到对应方法
            return switch (request.getMethod()) {
                case "initialize" -> handleInitialize(request);
                case "tools/list" -> handleToolsList(request);
                case "tools/call" -> handleToolsCall(request);
                case "resources/list" -> handleResourcesList(request);
                default -> JsonRpcResponse.error(
                    request.getId(),
                    -32601,
                    "Method not found",
                    "Unknown method: " + request.getMethod()
                );
            };

        } catch (Exception e) {
            log.error("Error handling request", e);
            return JsonRpcResponse.error(
                request.getId(),
                -32603,
                "Internal error",
                e.getMessage()
            );
        }
    }

    /**
     * 处理 initialize
     */
    private JsonRpcResponse handleInitialize(JsonRpcRequest request) {
        log.info("Initialize request from client");
        
        Map<String, Object> serverInfo = Map.of(
            "name", "payment-mcp-mvp",
            "version", "1.0.0",
            "capabilities", Map.of(
                "tools", Map.of("listChanged", false),
                "resources", Map.of("listChanged", false)
            )
        );

        return JsonRpcResponse.success(request.getId(), Map.of(
            "protocolVersion", "2024-11-05",
            "serverInfo", serverInfo
        ));
    }

    /**
     * 处理 tools/list
     */
    private JsonRpcResponse handleToolsList(JsonRpcRequest request) {
        log.info("Tools list requested");

        List<Map<String, Object>> tools = List.of(
            queryBalanceTool.getToolMetadata(),
            createPaymentTool.getToolMetadata(),
            queryOrderTool.getToolMetadata()
        );

        return JsonRpcResponse.success(request.getId(), Map.of("tools", tools));
    }

    /**
     * 处理 tools/call
     */
    private JsonRpcResponse handleToolsCall(JsonRpcRequest request) throws Exception {
        String toolName = request.getToolName();
        Map<String, Object> arguments = request.getArguments();

        log.info("Tool call: {} with args: {}", toolName, arguments);

        Object result = switch (toolName) {
            case "query_balance" -> queryBalanceTool.call(arguments);
            case "create_payment" -> createPaymentTool.call(arguments);
            case "query_order" -> queryOrderTool.call(arguments);
            default -> throw new RuntimeException("Unknown tool: " + toolName);
        };

        // MCP 标准响应格式
        Map<String, Object> content = Map.of(
            "content", List.of(
                Map.of("type", "text", "text", toJson(result))
            )
        );

        return JsonRpcResponse.success(request.getId(), content);
    }

    /**
     * 处理 resources/list
     */
    private JsonRpcResponse handleResourcesList(JsonRpcRequest request) {
        log.info("Resources list requested");
        return JsonRpcResponse.success(request.getId(), Map.of("resources", List.of()));
    }
}
