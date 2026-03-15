#!/bin/bash

# Payment MCP Server MVP - 快速测试脚本
# 用法：./test-api.sh [base_url]

BASE_URL=${1:-"http://localhost:8080"}

echo "╔══════════════════════════════════════════════════════════╗"
echo "║       Payment MCP Server MVP - API 测试脚本               ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo ""
echo "📍 测试地址：$BASE_URL"
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试计数器
PASS=0
FAIL=0

# 测试函数
test_api() {
    local name=$1
    local method=$2
    local endpoint=$3
    local data=$4
    
    echo -n "🧪 测试：$name ... "
    
    if [ "$method" == "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n-1)
    
    if [ "$http_code" == "200" ] || [ "$http_code" == "202" ]; then
        echo -e "${GREEN}✅ PASS${NC}"
        ((PASS++))
        echo "   响应：${body:0:200}..."
    else
        echo -e "${RED}❌ FAIL (HTTP $http_code)${NC}"
        ((FAIL++))
        echo "   响应：$body"
    fi
    echo ""
}

echo "═══════════════════════════════════════════════════════════"
echo "1️⃣  健康检查测试"
echo "═══════════════════════════════════════════════════════════"
test_api "健康检查" "GET" "/health" ""

echo "═══════════════════════════════════════════════════════════"
2️⃣  MCP 协议测试"
echo "═══════════════════════════════════════════════════════════"
test_api "Initialize" "POST" "/mcp?session=test1" \
    '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}'

test_api "Tools List" "POST" "/mcp?session=test2" \
    '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}'

test_api "Resources List" "POST" "/mcp?session=test3" \
    '{"jsonrpc":"2.0","id":3,"method":"resources/list","params":{}}'

echo "═══════════════════════════════════════════════════════════"
3️⃣  工具调用测试"
echo "═══════════════════════════════════════════════════════════"
test_api "Query Balance" "POST" "/mcp?session=test4" \
    '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"query_balance","arguments":{"user_id":"user_123","account_type":"BALANCE"}}}'

test_api "Create Payment" "POST" "/mcp?session=test5" \
    '{"jsonrpc":"2.0","id":5,"method":"tools/call","params":{"name":"create_payment","arguments":{"amount":100.00,"order_id":"ORD20260311001","payment_method":"alipay","user_id":"user_123","currency":"CNY"}}}'

test_api "Query Order" "POST" "/mcp?session=test6" \
    '{"jsonrpc":"2.0","id":6,"method":"tools/call","params":{"name":"query_order","arguments":{"order_id":"ORD20260311001"}}}'

echo "═══════════════════════════════════════════════════════════"
4️⃣  错误处理测试"
echo "═══════════════════════════════════════════════════════════"
test_api "Invalid Method" "POST" "/mcp?session=test7" \
    '{"jsonrpc":"2.0","id":7,"method":"invalid_method","params":{}}'

test_api "Invalid Tool" "POST" "/mcp?session=test8" \
    '{"jsonrpc":"2.0","id":8,"method":"tools/call","params":{"name":"unknown_tool","arguments":{}}}'

echo "═══════════════════════════════════════════════════════════"
echo "📊 测试结果汇总"
echo "═══════════════════════════════════════════════════════════"
echo -e "✅ 通过：${GREEN}$PASS${NC}"
echo -e "❌ 失败：${RED}$FAIL${NC}"
echo ""

if [ $FAIL -eq 0 ]; then
    echo -e "${GREEN}🎉 所有测试通过！${NC}"
    exit 0
else
    echo -e "${YELLOW}⚠️  部分测试失败，请检查日志${NC}"
    exit 1
fi
