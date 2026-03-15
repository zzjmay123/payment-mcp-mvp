#!/usr/bin/env python3
"""
Payment MCP 客户端测试 - 正确的 MCP 协议调用方式
"""

import requests
import time
import json
from threading import Thread, Event

class McpClient:
    def __init__(self, base_url="http://localhost:8080"):
        self.base_url = base_url
        self.session_id = None
        self.sse_session = None
        self.stop_event = Event()
        self.received_messages = []
    
    def connect_sse(self):
        """建立 SSE 连接"""
        print("📡 正在建立 SSE 连接...")
        self.sse_session = requests.Session()
        
        try:
            response = self.sse_session.get(
                f"{self.base_url}/sse",
                stream=True,
                timeout=30
            )
            
            # 读取 SSE 事件直到获取 session ID
            for line in response.iter_lines():
                if line:
                    line_str = line.decode('utf-8')
                    print(f"  SSE: {line_str}")
                    
                    if "/mcp?session=" in line_str:
                        self.session_id = line_str.split("/mcp?session=")[1].strip()
                        print(f"✅ 获取到 Session ID: {self.session_id}")
                        return True
            
        except Exception as e:
            print(f"❌ SSE 连接失败：{e}")
            return False
        
        return False
    
    def listen_sse(self, duration=5):
        """监听 SSE 消息"""
        def listen():
            try:
                while not self.stop_event.is_set():
                    if self.sse_session:
                        try:
                            for line in self.sse_session.get(
                                f"{self.base_url}/sse",
                                stream=True,
                                timeout=1
                            ).iter_lines():
                                if line:
                                    line_str = line.decode('utf-8')
                                    print(f"  📨 SSE: {line_str}")
                                    if "message" in line_str or "result" in line_str:
                                        self.received_messages.append(line_str)
                        except:
                            pass
                    time.sleep(0.1)
            except Exception as e:
                print(f"监听线程错误：{e}")
        
        thread = Thread(target=listen, daemon=True)
        thread.start()
        time.sleep(duration)
        self.stop_event.set()
    
    def call_tool(self, tool_name, arguments):
        """调用 MCP 工具"""
        if not self.session_id:
            print("❌ 未建立 SSE 连接")
            return None
        
        print(f"\n🔧 调用工具：{tool_name}")
        print(f"   参数：{json.dumps(arguments, ensure_ascii=False, indent=2)}")
        
        payload = {
            "jsonrpc": "2.0",
            "id": 1,
            "method": "tools/call",
            "params": {
                "name": tool_name,
                "arguments": arguments
            }
        }
        
        try:
            response = self.sse_session.post(
                f"{self.base_url}/mcp?session={self.session_id}",
                json=payload,
                timeout=5
            )
            
            print(f"📄 HTTP 状态码：{response.status_code}")
            
            if response.status_code == 202:
                print("✅ 请求已接受，等待 SSE 响应...")
                time.sleep(2)  # 等待 SSE 响应
                
                if self.received_messages:
                    print("\n📨 收到 SSE 响应:")
                    for msg in self.received_messages:
                        try:
                            # 尝试解析 JSON
                            if "data:" in msg:
                                data_str = msg.split("data:")[1].strip()
                                result = json.loads(data_str)
                                print(json.dumps(result, ensure_ascii=False, indent=2))
                                return result
                        except:
                            print(f"  {msg}")
            else:
                print(f"❌ 响应：{response.text}")
                
        except Exception as e:
            print(f"❌ 调用失败：{e}")
        
        return None
    
    def close(self):
        """关闭连接"""
        if self.sse_session:
            self.sse_session.close()
        print("\n🔌 连接已关闭")


def main():
    print("="*60)
    print("💳 Payment MCP 客户端测试")
    print("="*60)
    
    client = McpClient()
    
    try:
        # 1. 建立 SSE 连接
        if not client.connect_sse():
            print("❌ 连接失败")
            return
        
        print(f"\n✅ SSE 连接成功！Session: {client.session_id}")
        
        # 2. 调用 create_payment 工具
        print("\n" + "-"*60)
        print("测试：创建支付订单")
        print("-"*60)
        
        result = client.call_tool("create_payment", {
            "amount": 20.00,
            "order_id": "test-zzj",
            "payment_method": "alipay",
            "user_id": "user_zzj",
            "currency": "CNY"
        })
        
        if result and "result" in result:
            res = result["result"]
            print("\n" + "="*60)
            print("✅ 支付成功！")
            print("="*60)
            print(f"   订单号：{res.get('order_id', 'N/A')}")
            print(f"   金额：¥{res.get('amount', 0):.2f}")
            print(f"   支付方式：{res.get('payment_method', 'N/A')}")
            print(f"   交易流水号：{res.get('transaction_id', 'N/A')}")
            print(f"   状态：{res.get('status', 'N/A')}")
            print(f"   支付链接：{res.get('pay_url', 'N/A')}")
            print("="*60)
        else:
            print("\n❌ 支付失败或未收到响应")
        
        # 3. 测试查询余额
        print("\n" + "-"*60)
        print("测试：查询余额")
        print("-"*60)
        
        result = client.call_tool("query_balance", {
            "user_id": "user_zzj",
            "account_type": "BALANCE"
        })
        
        if result and "result" in result:
            res = result["result"]
            print("\n" + "="*60)
            print("✅ 余额查询成功！")
            print("="*60)
            print(f"   用户：{res.get('user_id', 'N/A')}")
            print(f"   余额：¥{res.get('balance', 0):.2f}")
            print(f"   账户类型：{res.get('account_type', 'N/A')}")
            print("="*60)
        
    except KeyboardInterrupt:
        print("\n⚠️  用户中断")
    except Exception as e:
        print(f"\n❌ 错误：{e}")
    finally:
        client.close()
    
    print("\n✅ 测试完成！")


if __name__ == "__main__":
    main()
