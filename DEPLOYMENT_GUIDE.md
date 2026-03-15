# Payment MCP 服务发布指南

本指南帮助你将 Payment MCP Server 发布到**智谱 AI**和**Coze**平台。

---

## 📋 前置准备

### 1. 确保服务可公网访问

你的 MCP 服务需要公网 IP 才能被平台调用。有三种方案：

#### 方案 A：云服务器部署（推荐生产环境）
```bash
# 1. 购买云服务器（阿里云/腾讯云/AWS）
# 推荐配置：2 核 4G，带宽 3Mbps+
# 系统：Ubuntu 22.04 / CentOS 8

# 2. 上传项目
scp -r /Users/zhouzhenjiang/workspace/payment-mcp-mvp root@your-server-ip:/opt/

# 3. 安装 Java 17
sudo apt update
sudo apt install openjdk-17-jdk -y

# 4. 安装 Maven
sudo apt install maven -y

# 5. 编译打包
cd /opt/payment-mcp-mvp
mvn clean package -DskipTests

# 6. 后台运行
nohup java -jar target/payment-mcp-mvp-1.0.0-SNAPSHOT.jar --server.port=8080 &

# 7. 开放防火墙端口
sudo ufw allow 8080/tcp
```

#### 方案 B：本地开发 + 内网穿透（推荐测试）
```bash
# 使用 ngrok
brew install ngrok
ngrok http 8080

# 或使用 cpolar
# 访问 https://cpolar.com 下载客户端
./cpolar http 8080

# 获得公网 URL，如：https://abc123.ngrok.io
```

#### 方案 C：Docker 部署
```bash
# 1. 创建 Dockerfile
cat > Dockerfile << 'EOF'
FROM maven:3.9-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

# 2. 构建镜像
docker build -t payment-mcp:1.0.0 .

# 3. 运行容器
docker run -d -p 8080:8080 --name payment-mcp payment-mcp:1.0.0
```

---

### 2. 验证服务可访问

```bash
# 本地测试
curl http://localhost:8080/health

# 公网测试（替换为你的公网地址）
curl https://your-public-ip:8080/health
```

预期返回：
```json
{"status": "UP"}
```

---

## 🤖 智谱 AI 平台发布

### 步骤 1：登录智谱 AI 开放平台

访问：https://open.bigmodel.cn

### 步骤 2：创建智能体

1. 进入「控制台」→「我的应用」→「创建应用」
2. 选择「智能体」类型
3. 填写基本信息：
   - 名称：支付助手
   - 描述：支持余额查询、支付、订单查询的 AI 支付助手
   - 图标：上传支付相关图标

### 步骤 3：配置 MCP 服务

1. 在智能体配置页，找到「能力扩展」或「插件配置」
2. 点击「添加 MCP 服务」
3. 填写配置：

| 配置项 | 值 | 说明 |
|--------|-----|------|
| 服务名称 | Payment MCP Server | 自定义 |
| 服务描述 | 支付能力 MCP 服务 | 自定义 |
| Endpoint URL | `https://your-domain.com/sse` | 你的公网 SSE 地址 |
| 协议版本 | 2024-11-05 | 默认 |
| 认证方式 | 无 / Bearer Token | MVP 版本可选无 |

### 步骤 4：测试连接

1. 点击「测试连接」按钮
2. 平台会调用你的 `/sse` 端点建立连接
3. 调用 `tools/list` 获取工具列表
4. 确认显示 3 个工具：
   - `query_balance` - 查询余额
   - `create_payment` - 创建支付
   - `query_order` - 查询订单

### 步骤 5：发布智能体

1. 配置完成后点击「保存」
2. 点击「发布」按钮
3. 选择发布渠道：
   - 智谱 AI App
   - Web 嵌入
   - API 调用

### 步骤 6：测试智能体

在智谱 AI App 或 Web 端测试：
```
用户：帮我查询 user_123 的余额
AI：正在查询余额... 查询成功，余额为 1000.00 元

用户：我要支付 100 元，订单号 ORD20260311001
AI：正在创建支付... 支付成功，订单号：ORD20260311001
```

---

## 🧩 Coze 平台发布

### 步骤 1：登录 Coze 平台

访问：https://www.coze.com（国际版）或 https://www.coze.cn（国内版）

### 步骤 2：创建 Bot

1. 点击「Create Bot」
2. 填写基本信息：
   - Name: Payment Assistant
   - Description: AI payment assistant with balance query and payment capabilities
   - Icon: 上传图标

### 步骤 3：添加 MCP 插件

1. 在 Bot 配置页，找到「Plugins」标签
2. 点击「Add Plugin」→「MCP Server」
3. 填写配置：

| 配置项 | 值 | 说明 |
|--------|-----|------|
| Plugin Name | Payment MCP | 自定义 |
| Description | Payment tools via MCP | 自定义 |
| SSE Endpoint | `https://your-domain.com/sse` | 你的公网 SSE 地址 |
| Auth Type | None / Bearer Token | MVP 版本可选无 |

### 步骤 4：配置工具权限

1. 平台会自动发现 3 个工具
2. 为每个工具配置使用权限：
   - ✅ `query_balance` - 允许调用
   - ✅ `create_payment` - 允许调用（建议添加二次确认）
   - ✅ `query_order` - 允许调用

### 步骤 5：设置 Prompt

在 Bot 的「Persona & Prompt」中添加：
```
你是一个支付助手，可以帮助用户：
1. 查询账户余额
2. 创建支付订单
3. 查询订单状态

调用支付相关工具前，请确认用户意图。
涉及金额操作时，请用户确认后再执行。
```

### 步骤 6：测试 Bot

1. 在右侧预览窗口测试
2. 测试用例：
   ```
   User: Check my balance
   Bot: Your current balance is 1000.00 CNY
   
   User: Pay 100 CNY for order ORD20260311001
   Bot: Confirming payment of 100.00 CNY for order ORD20260311001. Proceed?
   ```

### 步骤 7：发布 Bot

1. 点击「Publish」按钮
2. 选择发布渠道：
   - Coze App
   - Discord
   - Telegram
   - Web Embed
   - API

---

## 🔐 生产环境安全加固

### 1. 添加认证（必须）

修改 `application.yml`：
```yaml
mcp:
  server:
    auth:
      enabled: true
      type: bearer_token
      tokens:
        - your-secret-token-here
```

修改 `SseController.java` 添加 Token 验证：
```java
@GetMapping("/sse")
public SseEmitter sse(
    @RequestParam(required = false) String token,
    @RequestHeader(required = false) String authorization
) {
    // 验证 Token
    if (!authService.validate(token, authorization)) {
        throw new UnauthorizedException("Invalid token");
    }
    // ...
}
```

### 2. 添加 HTTPS

```bash
# 使用 Nginx 反向代理
sudo apt install nginx -y

# 配置 Nginx
sudo nano /etc/nginx/sites-available/payment-mcp

# 内容：
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl;
    server_name your-domain.com;
    
    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}

# 启用配置
sudo ln -s /etc/nginx/sites-available/payment-mcp /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx

# 申请免费 SSL 证书
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d your-domain.com
```

### 3. 添加限流

```java
// 添加 RateLimiter
@Bean
public RateLimiter rateLimiter() {
    return RateLimiter.create(100); // 每秒 100 请求
}
```

### 4. 添加审计日志

```java
// 记录所有工具调用
@Around("execution(* com.payment.mcp.tools.*.*(..))")
public Object logToolCall(ProceedingJoinPoint pjp) throws Throwable {
    log.info("Tool call: {} with args: {}", pjp.getSignature().getName(), pjp.getArgs());
    Object result = pjp.proceed();
    log.info("Tool result: {}", result);
    return result;
}
```

---

## 📊 监控与告警

### 1. 添加健康检查端点

已实现：`GET /actuator/health`

### 2. 添加 Prometheus 指标

```xml
<!-- pom.xml 添加依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### 3. 配置告警

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

---

## 🧪 完整测试清单

### 功能测试
- [ ] 健康检查端点可访问
- [ ] SSE 连接可建立
- [ ] tools/list 返回 3 个工具
- [ ] query_balance 返回正确余额
- [ ] create_payment 创建成功
- [ ] query_order 查询成功

### 平台测试
- [ ] 智谱 AI 平台连接成功
- [ ] Coze 平台连接成功
- [ ] 智能体/Bot 可调用工具
- [ ] 自然语言触发工具调用

### 安全测试
- [ ] Token 认证生效
- [ ] HTTPS 加密传输
- [ ] 限流策略生效
- [ ] 审计日志记录

---

## 🆘 常见问题

### Q1: 平台提示「连接超时」
**原因**: 服务器防火墙未开放端口或内网穿透失效  
**解决**: 
```bash
# 检查端口
netstat -tlnp | grep 8080

# 检查防火墙
sudo ufw status
sudo ufw allow 8080/tcp

# 重新获取内网穿透地址
ngrok http 8080
```

### Q2: 工具列表为空
**原因**: MCP 协议版本不匹配或工具注册失败  
**解决**:
1. 检查日志：`tail -f logs/application.log`
2. 确认工具类有 `@Component` 注解
3. 验证 `tools/list` 接口手动调用

### Q3: 支付工具调用失败
**原因**: Mock 模式未返回预期格式  
**解决**:
```bash
# 手动测试
curl -X POST "http://localhost:8080/mcp?session=test" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"create_payment","arguments":{"amount":100,"order_id":"ORD001","user_id":"user_123"}}}'
```

### Q4: 智谱/Coze 平台找不到 MCP 配置入口
**原因**: 平台 UI 更新或权限不足  
**解决**:
1. 确认账号有「开发者」权限
2. 查看平台最新文档
3. 联系平台客服

---

## 📞 技术支持

- 项目问题：查看 `/Users/zhouzhenjiang/workspace/payment-mcp-mvp/README.md`
- 智谱平台：https://open.bigmodel.cn/dev/api
- Coze 平台：https://www.coze.com/docs

---

**祝你发布顺利！** 🎉

最后更新：2026-03-15  
作者：小江同学
