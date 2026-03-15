package com.payment.mcp.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * JSON-RPC 2.0 响应
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRpcResponse {

    private String jsonrpc;
    private Object id;
    private Object result;
    private Error error;

    /**
     * 创建成功响应
     */
    public static JsonRpcResponse success(Object id, Object result) {
        return JsonRpcResponse.builder()
            .jsonrpc("2.0")
            .id(id)
            .result(result)
            .build();
    }

    /**
     * 创建错误响应
     */
    public static JsonRpcResponse error(Object id, int code, String message, Object data) {
        Error error = Error.builder()
            .code(code)
            .message(message)
            .data(data)
            .build();
        return JsonRpcResponse.builder()
            .jsonrpc("2.0")
            .id(id)
            .error(error)
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private int code;
        private String message;
        private Object data;
    }
}
