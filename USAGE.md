# Payment MCP 使用指南 🎉

## ✅ 当前状态

- **服务状态**: ✅ 运行中 (PID: 30534)
- **服务地址**: http://localhost:8080/sse
- **健康检查**: ✅ UP
- **CoPaw 配置**: ✅ 已配置

---

## 🚀 如何使用

### 方式 1：在 CoPaw 中直接使用（推荐）

你的 CoPaw 已经配置好了 `payment-mcp` 服务，**直接对话**即可！

#### 在飞书群中 @小江同学：

**1. 查询余额**
```
帮我查询 user_123 的余额
```

**2. 创建支付**
```
我要支付 100 元，订单号 ORD20260311001
```
或者
```
帮 user_123 创建一笔支付，金额 50 元，支付宝支付
```

**3. 查询订单**
```
查询订单 ORD20260311001 的状态
```

**4. 组合操作**
```
先帮我查一下 user_123 的余额，然后支付 200 元
```

---

### 方式 2：在控制台中使用

如果你在本地控制台使用 CoPaw，直接输入：

```bash
# 启动 CoPaw（如果还没启动）
copaw

# 然后输入自然语言指令
> 帮我查询 user_123 的余额
> 创建支付，订单号 TEST001，金额 100 元
> 查询订单 TEST001
```

---

## 📦 支持的工具

你的 Payment MCP 服务提供 3 个工具：

| 工具名称 | 功能 | 参数示例 |
|---------|------|---------|
| `query_balance` | 查询余额 | `user_id: "user_123"`, `account_type: "BALANCE"` |
| `create_payment` | 创建支付 | `amount: 100`, `order_id: "ORD001"`, `user_id: "user_123"` |
| `query_order` | 查询订单 | `order_id: "ORD001"` |

---

## 🧪 测试示例

### 测试 1：查询余额
**你说：**
```
查询用户 test_user 的余额
```

**预期返回：**
```
✅ 查询成功
用户 test_user 的余额为：1000.00 CNY
账户类型：BALANCE
```

### 测试 2：创建支付
**你说：**
```
支付 100 元，订单号 ORDER_123
```

**预期返回：**
```
✅ 支付成功
订单号：ORDER_123
金额：100.00 CNY
支付方式：alipay
交易流水号：TXN_20260315_001
```

### 测试 3：查询订单
**你说：**
```
帮我查一下订单 ORDER_123 的状态
```

**预期返回：**
```
✅ 订单查询成功
订单号：ORDER_123
状态：SUCCESS
金额：100.00 CNY
支付时间：2026-03-15 16:30:00
```

---

## ⚙️ 配置说明

### CoPaw 配置位置
`/Users/zhouzhenjiang/.copaw/config.json`

### 当前配置
```json
{
  "mcp": {
    "clients": {
      "payment-mcp": {
        "name": "payment-mcp",
        "description": "支付能力 MCP 服务 - 支持余额查询、支付、订单查询",
        "enabled": true,
        "transport": "sse",
        "url": "http://localhost:8080/sse"
      }
    }
  }
}
```

### 修改配置后需要重启 CoPaw
```bash
# 如果使用控制台
# Ctrl+C 停止，然后重新启动 copaw

# 如果是服务
# 重启服务
```

---

## 🔧 服务管理

### 查看服务状态
```bash
lsof -i :8080
curl http://localhost:8080/health
```

### 停止服务
```bash
# 找到进程 ID
lsof -i :8080

# 杀死进程
kill -9 <PID>
```

### 重启服务
```bash
cd /Users/zhouzhenjiang/workspace/payment-mcp-mvp
./start.sh
```

---

## ⚠️ 注意事项

1. **Mock 模式**: 当前是 MVP 版本，所有数据都是模拟的
2. **无认证**: 生产环境需添加 OAuth2.0 认证
3. **内存存储**: Session 数据重启后丢失
4. **本地访问**: 当前只能本地访问，公网访问需配置内网穿透

---

## 🛠️ 故障排查

### 问题 1：CoPaw 无法调用工具
**检查：**
```bash
# 1. 服务是否运行
curl http://localhost:8080/health

# 2. CoPaw 配置是否正确
cat /Users/zhouzhenjiang/.copaw/config.json | grep -A 10 "payment-mcp"

# 3. 重启 CoPaw
```

### 问题 2：返回 "Session not found"
**原因：** SSE 连接未正确建立  
**解决：** 重启 Payment MCP 服务
```bash
cd /Users/zhouzhenjiang/workspace/payment-mcp-mvp
./start.sh
```

### 问题 3：工具调用超时
**原因：** 网络问题或服务响应慢  
**解决：** 检查日志或增加超时时间

---

## 📞 需要帮助？

- 项目文档：`/Users/zhouzhenjiang/workspace/payment-mcp-mvp/README.md`
- 部署指南：`/Users/zhouzhenjiang/workspace/payment-mcp-mvp/DEPLOYMENT_GUIDE.md`
- 测试脚本：`/Users/zhouzhenjiang/workspace/payment-mcp-mvp/test-mcp.sh`

---

**现在就开始使用吧！** 🎉

试试在飞书群里说：**"帮我查询 user_123 的余额"**

最后更新：2026-03-15 16:50
