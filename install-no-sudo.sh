#!/bin/bash

# 免 sudo 安装 Java + Maven (使用 SDKMAN)

echo "╔══════════════════════════════════════════════════════════╗"
echo "║     免管理员权限安装 Java 17 + Maven                      ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo ""

# Step 1: 安装 SDKMAN (不需要 sudo)
echo "📦 Step 1: 安装 SDKMAN..."
if [ -d "$HOME/.sdkman" ]; then
    echo "✅ SDKMAN 已安装"
else
    curl -s "https://get.sdkman.io" | bash
    if [ $? -eq 0 ]; then
        echo "✅ SDKMAN 安装成功"
    else
        echo "❌ SDKMAN 安装失败"
        exit 1
    fi
fi

# 加载 SDKMAN
source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null || export SDKMAN_DIR="$HOME/.sdkman"

# Step 2: 安装 Java 17
echo ""
echo "☕ Step 2: 安装 Java 17..."
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [[ "$java_version" =~ ^[0-9]+$ ]] && [ "$java_version" -ge 17 ]; then
        echo "✅ Java $java_version 已安装"
    else
        echo "⚠️  Java 版本过低，使用 SDKMAN 安装 Java 17..."
        sdk install java 17.0.9-tem
    fi
else
    sdk install java 17.0.9-tem
fi

# Step 3: 安装 Maven
echo ""
echo "🔨 Step 3: 安装 Maven..."
if command -v mvn &> /dev/null; then
    echo "✅ Maven 已安装"
else
    sdk install maven
fi

# Step 4: 验证
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "🎉 安装完成！验证结果："
echo "═══════════════════════════════════════════════════════════"
echo ""

source "$HOME/.sdkman/bin/sdkman-init.sh" 2>/dev/null

echo "☕ Java 版本:"
java -version 2>&1 | head -3

echo ""
echo "🔨 Maven 版本:"
mvn -version 2>&1 | head -3

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "✅ 所有依赖安装完成！"
echo "═══════════════════════════════════════════════════════════"
echo ""
echo "下一步："
echo "  1. 重启终端（或运行：source ~/.zshrc）"
echo "  2. cd /Users/zhouzhenjiang/workspace/payment-mcp-mvp"
echo "  3. ./start.sh"
echo ""
