package com.payment.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * JSON-RPC 2.0 请求
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRpcRequest {

    private String jsonrpc;
    private Object id;
    private String method;
    private Map<String, Object> params;

    /**
     * 获取工具名称（从 tools/call 方法）
     */
    public String getToolName() {
        if ("tools/call".equals(method) && params != null) {
            return (String) params.get("name");
        }
        return method;
    }

    /**
     * 获取工具参数
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getArguments() {
        if ("tools/call".equals(method) && params != null) {
            Object args = params.get("arguments");
            if (args instanceof Map) {
                return (Map<String, Object>) args;
            }
        }
        return params;
    }
}
