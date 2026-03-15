package com.payment.mcp;

import com.payment.mcp.protocol.JsonRpcHandler;
import com.payment.mcp.protocol.JsonRpcRequest;
import com.payment.mcp.protocol.JsonRpcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

/**
 * JsonRpcHandler 测试
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@SpringBootTest
public class JsonRpcHandlerTest {

    @Autowired
    private JsonRpcHandler jsonRpcHandler;

    @Test
    public void testInitialize() throws Exception {
        String json = """
            {
                "jsonrpc": "2.0",
                "id": 1,
                "method": "initialize",
                "params": {}
            }
            """;

        JsonRpcRequest request = jsonRpcHandler.parseRequest(json);
        JsonRpcResponse response = jsonRpcHandler.handleRequest(request);

        assertNotNull(response);
        assertNull(response.getError());
        assertNotNull(response.getResult());
        assertEquals("2.0", response.getJsonrpc());
        assertEquals(1, response.getId());
    }

    @Test
    public void testToolsList() throws Exception {
        String json = """
            {
                "jsonrpc": "2.0",
                "id": 2,
                "method": "tools/list",
                "params": {}
            }
            """;

        JsonRpcRequest request = jsonRpcHandler.parseRequest(json);
        JsonRpcResponse response = jsonRpcHandler.handleRequest(request);

        assertNotNull(response);
        assertNull(response.getError());
        
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> result = (java.util.Map<String, Object>) response.getResult();
        assertTrue(result.containsKey("tools"));
        
        @SuppressWarnings("unchecked")
        java.util.List<Map<String, Object>> tools = (java.util.List<Map<String, Object>>) result.get("tools");
        assertEquals(3, tools.size());
    }

    @Test
    public void testQueryBalance() throws Exception {
        String json = """
            {
                "jsonrpc": "2.0",
                "id": 3,
                "method": "tools/call",
                "params": {
                    "name": "query_balance",
                    "arguments": {
                        "user_id": "user_123",
                        "account_type": "BALANCE"
                    }
                }
            }
            """;

        JsonRpcRequest request = jsonRpcHandler.parseRequest(json);
        JsonRpcResponse response = jsonRpcHandler.handleRequest(request);

        assertNotNull(response);
        assertNull(response.getError());
        assertNotNull(response.getResult());
    }

    @Test
    public void testCreatePayment() throws Exception {
        String json = """
            {
                "jsonrpc": "2.0",
                "id": 4,
                "method": "tools/call",
                "params": {
                    "name": "create_payment",
                    "arguments": {
                        "amount": 100.00,
                        "order_id": "ORD20260311001",
                        "payment_method": "alipay",
                        "user_id": "user_123",
                        "currency": "CNY"
                    }
                }
            }
            """;

        JsonRpcRequest request = jsonRpcHandler.parseRequest(json);
        JsonRpcResponse response = jsonRpcHandler.handleRequest(request);

        assertNotNull(response);
        assertNull(response.getError());
    }

    @Test
    public void testInvalidMethod() throws Exception {
        String json = """
            {
                "jsonrpc": "2.0",
                "id": 5,
                "method": "unknown_method",
                "params": {}
            }
            """;

        JsonRpcRequest request = jsonRpcHandler.parseRequest(json);
        JsonRpcResponse response = jsonRpcHandler.handleRequest(request);

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(-32601, response.getError().getCode());
    }

    @Test
    public void testInvalidJsonRpcVersion() throws Exception {
        String json = """
            {
                "jsonrpc": "1.0",
                "id": 6,
                "method": "initialize",
                "params": {}
            }
            """;

        JsonRpcRequest request = jsonRpcHandler.parseRequest(json);
        JsonRpcResponse response = jsonRpcHandler.handleRequest(request);

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(-32600, response.getError().getCode());
    }
}
