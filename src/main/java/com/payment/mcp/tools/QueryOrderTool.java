package com.payment.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询订单工具（Mock）
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@Slf4j
@Component
public class QueryOrderTool {

    /**
     * 获取工具元数据
     */
    public Map<String, Object> getToolMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", "query_order");
        metadata.put("description", "查询订单状态和详情");
        
        Map<String, Object> inputSchema = new HashMap<>();
        inputSchema.put("type", "object");
        
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, Object> orderIdSchema = new HashMap<>();
        orderIdSchema.put("type", "string");
        orderIdSchema.put("description", "商户订单号");
        properties.put("order_id", orderIdSchema);
        
        Map<String, Object> transactionIdSchema = new HashMap<>();
        transactionIdSchema.put("type", "string");
        transactionIdSchema.put("description", "交易 ID（与 order_id 二选一）");
        properties.put("transaction_id", transactionIdSchema);
        
        inputSchema.put("properties", properties);
        metadata.put("inputSchema", inputSchema);
        
        return metadata;
    }

    /**
     * 执行工具
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> call(Map<String, Object> arguments) {
        String orderId = (String) arguments.get("order_id");
        String transactionId = (String) arguments.get("transaction_id");

        log.info("Query order: orderId={}, transactionId={}", orderId, transactionId);

        // Mock 订单数据
        String mockTransactionId = transactionId != null ? transactionId : "TXN" + System.currentTimeMillis();
        String mockOrderId = orderId != null ? orderId : "ORD" + System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        result.put("transaction_id", mockTransactionId);
        result.put("order_id", mockOrderId);
        result.put("status", "SUCCESS");
        result.put("amount", new BigDecimal("100.00"));
        result.put("currency", "CNY");
        result.put("payment_method", "alipay");
        result.put("user_id", "user_***1234");
        result.put("merchant_id", "merchant_001");
        result.put("description", "测试订单");
        result.put("create_time", System.currentTimeMillis() - 3600000);
        result.put("finish_time", System.currentTimeMillis() - 3500000);
        result.put("refundable", true);
        result.put("refund_amount", new BigDecimal("0.00"));
        return result;
    }
}
