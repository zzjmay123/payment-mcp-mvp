package com.payment.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 查询余额工具（Mock）
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@Slf4j
@Component
public class QueryBalanceTool {

    /**
     * 获取工具元数据
     */
    public Map<String, Object> getToolMetadata() {
        return Map.of(
            "name", "query_balance",
            "description", "查询用户账户余额",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "user_id", Map.of(
                        "type", "string",
                        "description", "用户 ID"
                    ),
                    "account_type", Map.of(
                        "type", "string",
                        "description", "账户类型",
                        "enum", new String[]{"BALANCE", "BANK_CARD", "CREDIT"}
                    )
                ),
                "required", new String[]{"user_id"}
            )
        );
    }

    /**
     * 执行工具
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> call(Map<String, Object> arguments) {
        String userId = (String) arguments.get("user_id");
        String accountType = (String) arguments.getOrDefault("account_type", "BALANCE");

        log.info("Query balance for user: {}, account: {}", userId, accountType);

        // Mock 余额数据
        BigDecimal balance = new BigDecimal("8888.88");
        BigDecimal frozen = new BigDecimal("100.00");
        BigDecimal available = balance.subtract(frozen);

        return Map.of(
            "user_id", userId,
            "account_type", accountType,
            "balance", balance,
            "frozen", frozen,
            "available", available,
            "currency", "CNY",
            "update_time", System.currentTimeMillis()
        );
    }
}
