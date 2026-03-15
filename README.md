# Payment MCP Server MVP

支付能力 MCP 服务 MVP 版本 - 快速演示用

## 🚀 快速开始

### 前置要求
- Java 17+
- Maven 3.8+

### 启动方式

**方式 1：使用启动脚本**
```bash
./start.sh
```

**方式 2：手动启动**
```bash
# 编译
mvn clean compile

# 测试
mvn test

# 运行
mvn spring-boot:run
```

### 访问端点

| 端点 | 说明 |
|------|------|
| `http://localhost:8080/sse` | SSE 连接端点 |
| `http://localhost:8080/mcp` | MCP 消息端点 |
| `http://localhost:8080/health` | 健康检查 |

## 📦 支持的工具

### 1. query_balance - 查询余额
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "query_balance",
    "arguments": {
      "user_id": "user_123",
      "account_type": "BALANCE"
    }
  }
}
```

### 2. create_payment - 创建支付
```json
{
  "jsonrpc": "2.0",
  "id": 2,
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
```

### 3. query_order - 查询订单
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "query_order",
    "arguments": {
      "order_id": "ORD20260311001"
    }
  }
}
```

## 🧪 测试示例

### 使用 curl 测试

**1. 获取工具列表**
```bash
curl -X POST http://localhost:8080/mcp?session=test \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "tools/list",
    "params": {}
  }'
```

**2. 调用查询余额工具**
```bash
curl -X POST http://localhost:8080/mcp?session=test \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/call",
    "params": {
      "name": "query_balance",
      "arguments": {
        "user_id": "user_123"
      }
    }
  }'
```

### 使用 Postman 测试

1. 创建 SSE 请求：`GET http://localhost:8080/sse`
2. 记录返回的 session ID
3. 创建 POST 请求：`POST http://localhost:8080/mcp?session={sessionId}`
4. Body 选择 raw JSON，发送 MCP 消息

## 📁 项目结构

```
payment-mcp-mvp/
├── src/main/java/com/payment/mcp/
│   ├── McpServerApplication.java      # 启动类
│   ├── controller/
│   │   └── SseController.java         # SSE 端点
│   ├── protocol/
│   │   ├── JsonRpcHandler.java        # JSON-RPC 处理
│   │   ├── JsonRpcRequest.java        # 请求模型
│   │   ├── JsonRpcResponse.java       # 响应模型
│   │   └── SessionStore.java          # Session 存储
│   ├── tools/
│   │   ├── QueryBalanceTool.java      # 查询余额
│   │   ├── CreatePaymentTool.java     # 创建支付
│   │   └── QueryOrderTool.java        # 查询订单
│   └── config/
│       └── AppConfig.java             # 配置类
├── src/main/resources/
│   └── application.yml                # 配置文件
├── src/test/java/
│   └── JsonRpcHandlerTest.java        # 单元测试
├── pom.xml                            # Maven 配置
├── start.sh                           # 启动脚本
└── README.md                          # 说明文档
```

## 🔧 配置说明

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `server.port` | 8080 | 服务端口 |
| `mcp.server.name` | payment-mcp-mvp | MCP 服务名称 |
| `mcp.server.version` | 1.0.0 | MCP 服务版本 |
| `mcp.server.mock` | true | Mock 模式 |

## ⚠️ 注意事项

1. **这是 MVP 版本**，仅用于演示和测试
2. **无认证** - 生产环境需添加 OAuth2.0 认证
3. **内存存储** - Session 数据重启后丢失
4. **Mock 数据** - 所有返回数据都是模拟的

## 📝 下一步

生产版本需补充：
- [ ] OAuth2.0 认证
- [ ] 数据库持久化
- [ ] 风险控制引擎
- [ ] 审计日志
- [ ] 监控告警
- [ ] 真实支付网关对接

## 📄 License

MIT License

---

**作者**: 小江同学  
**日期**: 2026-03-11  
**版本**: 1.0.0-SNAPSHOT
