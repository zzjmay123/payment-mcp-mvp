#!/usr/bin/env python3
"""
Payment MCP 简单测试 - 直接调用（绕过 SSE 问题）
"""

import requests
import json

BASE_URL = "http://localhost:8080"

print("="*60)
print("💳 Payment MCP 测试 - 直接调用模式")
print("="*60)

# 1. 健康检查
print("\n1️⃣  健康检查...")
health = requests.get(f"{BASE_URL}/health")
print(f"   状态：{health.json()}")

# 2. 直接调用工具（使用临时 session）
print("\n2️⃣  创建支付订单...")
session_id = "direct_test_session"

payload = {
    "jsonrpc": "2.0",
    "id": 1,
    "method": "tools/call",
    "params": {
        "name": "create_payment",
        "arguments": {
            "amount": 20.00,
            "order_id": "test-zzj",
            "payment_method": "alipay",
            "user_id": "user_zzj",
            "currency": "CNY"
        }
    }
}

try:
    # 先创建一个 SSE 连接（不保持）
    sse = requests.get(f"{BASE_URL}/sse", timeout=1)
    
    # 然后调用
    response = requests.post(
        f"{BASE_URL}/mcp?session={session_id}",
        json=payload,
        timeout=5
    )
    
    print(f"   HTTP 状态：{response.status_code}")
    print(f"   响应：{response.text}")
    
except Exception as e:
    print(f"   ❌ 错误：{e}")

print("\n" + "="*60)
print("💡 提示：完整的 MCP 调用需要正确的 SSE 会话管理")
print("   CoPaw 已经处理好了这些细节，直接在飞书中使用即可！")
print("="*60)
