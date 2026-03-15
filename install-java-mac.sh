#!/bin/bash

# macOS 自动安装 Java 17 和 Maven 脚本

echo "╔══════════════════════════════════════════════════════════╗"
echo "║     自动安装 Java 17 + Maven for macOS                   ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo ""

# 检查是否是 macOS
if [[ "$(uname)" != "Darwin" ]]; then
    echo "❌ 此脚本仅适用于 macOS"
    exit 1
fi

# 检查是否需要 sudo
if [ "$EUID" -ne 0 ]; then 
    echo "⚠️  需要管理员权限，请输入密码"
    sudo echo "✅ 权限确认成功"
    SUDO="sudo"
else
    SUDO=""
fi

# Step 1: 尝试安装 Homebrew（如果没有）
echo ""
echo "📦 Step 1: 检查 Homebrew..."
if ! command -v brew &> /dev/null; then
    echo "⚠️  Homebrew 未安装，正在安装..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    
    if [ $? -eq 0 ]; then
        echo "✅ Homebrew 安装成功"
        # 添加到 PATH
        echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
        eval "$(/opt/homebrew/bin/brew shellenv)"
    else
        echo "❌ Homebrew 安装失败"
        echo ""
        echo "请手动安装 Homebrew："
        echo '/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"'
        exit 1
    fi
else
    echo "✅ Homebrew 已安装"
fi

# Step 2: 安装 Java 17
echo ""
echo "☕ Step 2: 安装 Java 17..."
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$java_version" -ge 17 ]; then
        echo "✅ Java $java_version 已安装，跳过"
    else
        echo "⚠️  Java 版本过低 ($java_version)，需要 Java 17+"
        $SUDO brew install openjdk@17
    fi
else
    $SUDO brew install openjdk@17
fi

if [ $? -eq 0 ]; then
    echo "✅ Java 17 安装成功"
    # 设置 JAVA_HOME
    echo ""
    echo "📝 配置 JAVA_HOME..."
    echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
    echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
    source ~/.zshrc 2>/dev/null || true
else
    echo "❌ Java 安装失败"
    exit 1
fi

# Step 3: 安装 Maven
echo ""
echo "🔨 Step 3: 安装 Maven..."
if command -v mvn &> /dev/null; then
    mvn_version=$(mvn -version 2>&1 | head -1 | awk '{print $NF}')
    echo "✅ Maven $mvn_version 已安装，跳过"
else
    $SUDO brew install maven
fi

if [ $? -eq 0 ]; then
    echo "✅ Maven 安装成功"
else
    echo "❌ Maven 安装失败"
    exit 1
fi

# Step 4: 验证安装
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "🎉 安装完成！验证结果："
echo "═══════════════════════════════════════════════════════════"
echo ""

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
