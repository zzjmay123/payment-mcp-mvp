#!/bin/bash
# Payment MCP 服务测试脚本

echo "📡 测试 Payment MCP 服务..."
echo ""

# 1. 健康检查
echo "1️⃣  健康检查..."
curl -s http://localhost:8080/health | python3 -m json.tool
echo ""

# 2. 测试 SSE 连接（获取 session）
echo "2️⃣  建立 SSE 连接..."
SESSION_ID="test_session_$(date +%s)"
echo "Session ID: $SESSION_ID"
echo ""

# 3. 获取工具列表
echo "3️⃣  获取工具列表..."
curl -s -X POST "http://localhost:8080/mcp?session=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list","params":{}}' | python3 -m json.tool
echo ""

# 4. 查询余额
echo "4️⃣  测试查询余额..."
curl -s -X POST "http://localhost:8080/mcp?session=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/call",
    "params": {
      "name": "query_balance",
      "arguments": {
        "user_id": "user_123",
        "account_type": "BALANCE"
      }
    }
  }' | python3 -m json.tool
echo ""

# 5. 创建支付
echo "5️⃣  测试创建支付..."
curl -s -X POST "http://localhost:8080/mcp?session=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 3,
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
  }' | python3 -m json.tool
echo ""

# 6. 查询订单
echo "6️⃣  测试查询订单..."
curl -s -X POST "http://localhost:8080/mcp?session=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 4,
    "method": "tools/call",
    "params": {
      "name": "query_order",
      "arguments": {
        "order_id": "ORD20260311001"
      }
    }
  }' | python3 -m json.tool
echo ""

echo "✅ 测试完成！"
