# 📁 Payment MCP Server MVP - 项目文件索引

**项目位置**: `/Users/zhouzhenjiang/workspace/payment-mcp-mvp`  
**创建时间**: 2026-03-11  
**文件总数**: 18 个

---

## 📂 文件结构

```
payment-mcp-mvp/
│
├── 📄 文档类 (5 个)
│   ├── README.md              # 完整使用文档
│   ├── QUICKSTART.md          # 5 分钟快速开始 ⭐推荐先看
│   ├── DELIVERY.md            # 项目交付总结
│   ├── SELF_CHECK.md          # 代码自检报告
│   └── INDEX.md               # 本文件（文件索引）
│
├── 🔧 脚本类 (2 个)
│   ├── start.sh               # 一键启动脚本 ⭐
│   └── test-api.sh            # API 自动化测试脚本
│
├── 📦 配置类 (2 个)
│   ├── pom.xml                # Maven 配置
│   └── src/main/resources/
│       └── application.yml    # 应用配置
│
├── ☕ Java 源代码 (10 个)
│   │
│   ├── 启动类 (1 个)
│   │   └── McpServerApplication.java
│   │
│   ├── 控制器 (1 个)
│   │   └── controller/
│   │       └── SseController.java
│   │
│   ├── 协议层 (4 个)
│   │   └── protocol/
│   │       ├── JsonRpcHandler.java       # JSON-RPC 处理器 ⭐核心
│   │       ├── JsonRpcRequest.java       # 请求模型
│   │       ├── JsonRpcResponse.java      # 响应模型
│   │       └── SessionStore.java         # Session 存储
│   │
│   ├── 工具类 (3 个)
│   │   └── tools/
│   │       ├── QueryBalanceTool.java     # 查询余额
│   │       ├── CreatePaymentTool.java    # 创建支付
│   │       └── QueryOrderTool.java       # 查询订单
│   │
│   └── 配置类 (1 个)
│       └── config/
│           └── AppConfig.java
│
└── 🧪 测试类 (1 个)
    └── src/test/java/
        └── JsonRpcHandlerTest.java       # 单元测试（6 个用例）
```

---

## 📖 阅读顺序建议

### 第一次使用
1. ⭐ **QUICKSTART.md** - 5 分钟快速开始（最重要！）
2. **README.md** - 了解完整功能
3. **start.sh** - 运行脚本启动服务
4. **test-api.sh** - 运行测试验证功能

### 深入了解
5. **DELIVERY.md** - 了解项目详情和架构
6. **SELF_CHECK.md** - 查看代码质量报告
7. **源代码** - 查看具体实现

### 生产环境准备
8. 查看 DELIVERY.md 的"生产版本安全要求"
9. 添加 OAuth2.0 认证
10. 集成真实支付网关

---

## 🔑 关键文件说明

### 必须了解的

| 文件 | 作用 | 重要度 |
|------|------|--------|
| QUICKSTART.md | 快速开始指南 | ⭐⭐⭐⭐⭐ |
| start.sh | 启动服务 | ⭐⭐⭐⭐⭐ |
| test-api.sh | 测试接口 | ⭐⭐⭐⭐ |
| README.md | 完整文档 | ⭐⭐⭐⭐ |

### 核心代码

| 文件 | 作用 | 行数 |
|------|------|------|
| SseController.java | SSE 端点处理 | ~120 行 |
| JsonRpcHandler.java | JSON-RPC 协议处理 | ~150 行 |
| QueryBalanceTool.java | 查询余额工具 | ~60 行 |
| CreatePaymentTool.java | 创建支付工具 | ~90 行 |
| QueryOrderTool.java | 查询订单工具 | ~70 行 |

### 配置文件

| 文件 | 作用 |
|------|------|
| pom.xml | Maven 依赖和构建配置 |
| application.yml | Spring Boot 应用配置 |

---

## 🚀 快速命令参考

### 启动
```bash
./start.sh                    # 一键启动（编译 + 测试 + 运行）
mvn spring-boot:run           # 直接运行
```

### 测试
```bash
./test-api.sh                 # API 自动化测试
mvn test                      # Maven 单元测试
curl http://localhost:8080/health  # 健康检查
```

### 编译
```bash
mvn clean compile             # 编译
mvn clean package             # 打包
```

### 查看日志
```bash
# 服务启动后，日志会实时输出到控制台
# 按 Ctrl+C 停止服务
```

---

## 📊 项目统计

| 指标 | 数值 |
|------|------|
| **总文件数** | 18 |
| **Java 源文件** | 10 |
| **测试文件** | 1 |
| **配置文件** | 2 |
| **文档文件** | 5 |
| **脚本文件** | 2 |
| **代码总行数** | ~1,200 |
| **文档总行数** | ~800 |
| **测试用例数** | 6 |
| **API 端点数** | 3 |
| **工具数** | 3 |

---

## 🎯 使用场景

### 场景 1: 快速演示
```bash
# 1. 启动服务
./start.sh

# 2. 打开新终端，运行测试
./test-api.sh

# 3. 展示结果给领导/客户看
```

### 场景 2: 对接测试
```bash
# 1. 启动服务
./start.sh

# 2. 在 Coze/智谱平台配置 MCP 服务
# SSE URL: http://你的 IP:8080/sse

# 3. 测试连接
```

### 场景 3: 二次开发
```bash
# 1. 阅读源代码
# 重点：JsonRpcHandler.java, tools/*.java

# 2. 添加新工具
# 复制 tools/QueryBalanceTool.java
# 修改工具名和逻辑

# 3. 在 JsonRpcHandler.java 注册新工具
```

---

## ⚠️ 注意事项

### 环境要求
- ✅ Java 17+
- ✅ Maven 3.8+
- ✅ 端口 8080 可用

### 安全警告
- ❌ 不要部署到公网（无认证）
- ❌ 不要处理真实支付（Mock 数据）
- ❌ 不要存储敏感信息

### 性能限制
- 内存存储 Session（重启丢失）
- 单机部署（无负载均衡）
- 无数据库（数据不持久化）

---

## 📞 问题排查

### 问题 1: 启动失败
```bash
# 检查 Java
java -version

# 检查 Maven
mvn -version

# 检查端口
lsof -i :8080
```

### 问题 2: 测试失败
```bash
# 查看详细日志
mvn test -X

# 跳过测试运行
mvn spring-boot:run -DskipTests
```

### 问题 3: 需要帮助
1. 查看 QUICKSTART.md 的"常见问题"
2. 查看 README.md 的"使用示例"
3. 查看 DELIVERY.md 的"下一步建议"

---

## 🎉 项目完成清单

- [x] 项目代码（10 个 Java 类）
- [x] 单元测试（6 个测试用例）
- [x] 启动脚本（start.sh）
- [x] 测试脚本（test-api.sh）
- [x] 使用文档（README.md）
- [x] 快速开始（QUICKSTART.md）
- [x] 交付总结（DELIVERY.md）
- [x] 自检报告（SELF_CHECK.md）
- [x] 文件索引（INDEX.md）

**完成度**: 100% ✅

---

## 📅 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0-SNAPSHOT | 2026-03-11 | MVP 版本首发 |

---

## 📄 许可证

MIT License

---

**最后更新**: 2026-03-11  
**维护者**: 小江同学

---

🎊 **项目文件索引完成！所有文件已就绪，可以开始使用了！**
