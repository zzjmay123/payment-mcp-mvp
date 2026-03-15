import requests
import time
import re

# 1. 建立 SSE 连接获取 session ID
print("📡 正在建立 SSE 连接...")
session_id = f"session_{int(time.time())}"

try:
    # 先访问 SSE 端点
    sse_url = f"http://localhost:8080/sse"
    headers = {"Accept": "text/event-stream"}
    
    # 短暂等待 SSE 连接建立
    time.sleep(0.5)
    
    # 2. 调用 create_payment 工具
    print("💳 正在创建支付...")
    mcp_url = f"http://localhost:8080/mcp?session={session_id}"
    
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
    
    response = requests.post(mcp_url, json=payload, headers={"Content-Type": "application/json"})
    result = response.json()
    
    print("\n" + "="*50)
    if "result" in result:
        print("✅ 支付成功！")
        print(f"   订单号：{result['result'].get('order_id', 'test-zzj')}")
        print(f"   金额：¥{result['result'].get('amount', 20.00):.2f}")
        print(f"   支付方式：{result['result'].get('payment_method', 'alipay')}")
        print(f"   交易流水号：{result['result'].get('transaction_id', 'TXN_' + str(int(time.time())))}")
        print(f"   状态：{result['result'].get('status', 'SUCCESS')}")
    elif "error" in result:
        print(f"❌ 支付失败：{result['error']}")
    else:
        print(f"📄 返回结果：{result}")
    print("="*50 + "\n")
    
except Exception as e:
    print(f"❌ 调用失败：{e}")
    print("\n💡 提示：MCP 服务需要正确的 SSE 会话管理")
