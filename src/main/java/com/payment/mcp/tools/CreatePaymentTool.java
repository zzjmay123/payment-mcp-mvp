package com.payment.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建支付工具（Mock）
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@Slf4j
@Component
public class CreatePaymentTool {

    /**
     * 获取工具元数据
     */
    public Map<String, Object> getToolMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", "create_payment");
        metadata.put("description", "创建支付订单并发起支付");
        
        Map<String, Object> inputSchema = new HashMap<>();
        inputSchema.put("type", "object");
        
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, Object> amountSchema = new HashMap<>();
        amountSchema.put("type", "number");
        amountSchema.put("description", "支付金额（元）");
        amountSchema.put("minimum", 0.01);
        properties.put("amount", amountSchema);
        
        Map<String, Object> currencySchema = new HashMap<>();
        currencySchema.put("type", "string");
        currencySchema.put("description", "币种");
        currencySchema.put("enum", new String[]{"CNY", "USD", "EUR", "HKD"});
        properties.put("currency", currencySchema);
        
        Map<String, Object> orderIdSchema = new HashMap<>();
        orderIdSchema.put("type", "string");
        orderIdSchema.put("description", "商户订单号");
        properties.put("order_id", orderIdSchema);
        
        Map<String, Object> paymentMethodSchema = new HashMap<>();
        paymentMethodSchema.put("type", "string");
        paymentMethodSchema.put("description", "支付方式");
        paymentMethodSchema.put("enum", new String[]{"alipay", "wechat", "unionpay", "balance"});
        properties.put("payment_method", paymentMethodSchema);
        
        Map<String, Object> userIdSchema = new HashMap<>();
        userIdSchema.put("type", "string");
        userIdSchema.put("description", "用户 ID");
        properties.put("user_id", userIdSchema);
        
        Map<String, Object> descriptionSchema = new HashMap<>();
        descriptionSchema.put("type", "string");
        descriptionSchema.put("description", "支付描述");
        properties.put("description", descriptionSchema);
        
        inputSchema.put("properties", properties);
        inputSchema.put("required", new String[]{"amount", "order_id", "payment_method", "user_id"});
        metadata.put("inputSchema", inputSchema);
        
        return metadata;
    }

    /**
     * 执行工具
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> call(Map<String, Object> arguments) {
        BigDecimal amount = new BigDecimal(arguments.get("amount").toString());
        String orderId = (String) arguments.get("order_id");
        String paymentMethod = (String) arguments.get("payment_method");
        String userId = (String) arguments.get("user_id");
        String currency = (String) arguments.getOrDefault("currency", "CNY");
        String description = (String) arguments.getOrDefault("description", "订单支付");

        log.info("Create payment: order={}, amount={}, method={}, user={}", 
            orderId, amount, paymentMethod, userId);

        // Mock 支付结果
        String transactionId = "TXN" + System.currentTimeMillis();
        String payUrl = "https://pay.example.com/h5/" + transactionId;
        long expireTime = System.currentTimeMillis() + 900000; // 15 分钟后过期

        Map<String, Object> result = new HashMap<>();
        result.put("transaction_id", transactionId);
        result.put("order_id", orderId);
        result.put("status", "PENDING");
        result.put("amount", amount);
        result.put("currency", currency);
        result.put("payment_method", paymentMethod);
        result.put("user_id", userId);
        result.put("description", description);
        result.put("pay_url", payUrl);
        result.put("expire_time", expireTime);
        result.put("create_time", System.currentTimeMillis());
        return result;
    }
}
