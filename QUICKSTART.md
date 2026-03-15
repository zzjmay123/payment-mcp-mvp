# ⚡ 5 分钟快速开始

## 前置条件

### 1. 检查 Java
```bash
java -version
```
需要 Java 17+，如果没有：
```bash
# macOS
brew install openjdk@17

# Ubuntu/Debian
sudo apt install openjdk-17-jdk

# CentOS
sudo yum install java-17-openjdk
```

### 2. 检查 Maven
```bash
mvn -version
```
需要 Maven 3.8+，如果没有：
```bash
# macOS
brew install maven

# Ubuntu/Debian
sudo apt install maven

# 或下载：https://maven.apache.org/download.cgi
```

---

## 启动服务

### 方式 1：一键启动（推荐）
```bash
cd /Users/zhouzhenjiang/workspace/payment-mcp-mvp
./start.sh
```

### 方式 2：分步启动
```bash
# 1. 编译
mvn clean compile

# 2. 测试
mvn test

# 3. 运行
mvn spring-boot:run
```

看到以下输出表示成功：
```
╔══════════════════════════════════════════════════════════╗
║       Payment MCP Server MVP 启动成功！                   ║
║                                                          ║
║  SSE 端点：http://localhost:8080/sse                      ║
║  消息端点：http://localhost:8080/mcp                      ║
║  健康检查：http://localhost:8080/actuator/health          ║
╚══════════════════════════════════════════════════════════╝
```

---

## 测试接口

### 方式 1：自动测试脚本
```bash
./test-api.sh
```

### 方式 2：手动测试

#### 1. 健康检查
```bash
curl http://localhost:8080/health
```

#### 2. 获取工具列表
```bash
curl -X POST "http://localhost:8080/mcp?session=test" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list","params":{}}'
```

#### 3. 查询余额
```bash
curl -X POST "http://localhost:8080/mcp?session=test" \
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
  }'
```

#### 4. 创建支付
```bash
curl -X POST "http://localhost:8080/mcp?session=test" \
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
  }'
```

---

## 使用 Postman 测试

### 1. 创建 SSE 请求
- Method: `GET`
- URL: `http://localhost:8080/sse`
- 点击 Send，保持连接

### 2. 创建 MCP 请求
- Method: `POST`
- URL: `http://localhost:8080/mcp?session={sessionId}`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/call",
  "params": {
    "name": "query_balance",
    "arguments": {
      "user_id": "user_123"
    }
  }
}
```

---

## 对接智能体平台

### Coze 平台
1. 登录 https://www.coze.com
2. 创建 Bot → 插件 → 添加 MCP 服务
3. 配置：
   - 名称：Payment MCP
   - SSE URL: `http://你的服务器IP:8080/sse`
   - 认证：无（MVP 版本）
4. 测试连接

### 智谱平台
1. 登录 https://open.bigmodel.cn
2. 创建智能体 → 能力配置 → MCP 服务
3. 配置：
   - Endpoint: `http://你的服务器IP:8080/sse`
   - API Version: `2024-11-05`
4. 保存并测试

---

## 常见问题

### Q1: 端口 8080 被占用
```bash
# 查看占用端口的进程
lsof -i :8080

# 杀死进程
kill -9 <PID>

# 或修改端口
# 编辑 src/main/resources/application.yml
# server.port: 8081
```

### Q2: Java 版本不对
```bash
# 查看安装的 Java 版本
/usr/libexec/java_home -V  # macOS

# 设置 JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

### Q3: Maven 下载依赖慢
```bash
# 使用阿里云镜像
# 编辑 ~/.m2/settings.xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

### Q4: 测试失败
```bash
# 查看详细日志
mvn test -X

# 跳过测试直接运行
mvn spring-boot:run -DskipTests
```

---

## 下一步

✅ 服务已启动  
✅ 接口测试通过  
👉 查看 README.md 了解完整文档  
👉 查看 DELIVERY.md 了解项目详情  
👉 对接你的智能体平台  

---

**祝你使用愉快！** 🎉
