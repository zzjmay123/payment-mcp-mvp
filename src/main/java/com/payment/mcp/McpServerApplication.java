package com.payment.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Payment MCP Server MVP - 启动类
 * 
 * @author 小江同学
 * @since 2026-03-11
 */
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       Payment MCP Server MVP 启动成功！                   ║");
        System.out.println("║                                                          ║");
        System.out.println("║  SSE 端点：http://localhost:8080/sse                      ║");
        System.out.println("║  消息端点：http://localhost:8080/mcp                      ║");
        System.out.println("║  健康检查：http://localhost:8080/actuator/health          ║");
        System.out.println("║                                                          ║");
        System.out.println("║  支持工具：                                               ║");
        System.out.println("║    - query_balance    查询余额                           ║");
        System.out.println("║    - create_payment   创建支付                           ║");
        System.out.println("║    - query_order      查询订单                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
}
