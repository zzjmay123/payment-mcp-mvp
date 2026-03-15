#!/bin/bash

# Payment MCP Server MVP - 启动脚本

echo "╔══════════════════════════════════════════════════════════╗"
echo "║       Payment MCP Server MVP 启动脚本                     ║"
echo "╚══════════════════════════════════════════════════════════╝"

# 检查 Java 版本
echo ""
echo "📋 检查 Java 版本..."
if command -v java &> /dev/null; then
    java -version 2>&1 | head -1
else
    echo "❌ Java 未安装"
    echo ""
    echo "请安装 Java 17+："
    echo "  macOS:  brew install openjdk@17"
    echo "  Ubuntu: sudo apt install openjdk-17-jdk"
    echo "  其他：https://adoptium.net/"
    exit 1
fi

# 检查 Maven
echo ""
echo "📋 检查 Maven..."
if command -v mvn &> /dev/null; then
    mvn -version 2>&1 | head -1
else
    echo "❌ Maven 未安装"
    echo ""
    echo "请安装 Maven 3.8+："
    echo "  macOS:  brew install maven"
    echo "  Ubuntu: sudo apt install maven"
    echo "  其他：https://maven.apache.org/download.cgi"
    exit 1
fi

# 编译项目
echo ""
echo "🔨 编译项目..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ 编译成功！"
else
    echo "❌ 编译失败"
    exit 1
fi

# 运行测试
echo ""
echo "🧪 运行测试..."
mvn test -q

if [ $? -eq 0 ]; then
    echo "✅ 测试通过！"
else
    echo "❌ 测试失败"
    exit 1
fi

# 启动服务
echo ""
echo "🚀 启动服务..."
echo ""
mvn spring-boot:run
