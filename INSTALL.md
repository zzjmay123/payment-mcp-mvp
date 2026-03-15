# 🔧 环境安装指南

**重要**: 由于需要管理员权限，以下步骤需要你手动执行。

---

## 方法 1: 使用 Homebrew（推荐，需要 sudo）

### Step 1: 安装 Homebrew（如果已有可跳过）
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Step 2: 安装 Java 17
```bash
brew install openjdk@17
```

### Step 3: 安装 Maven
```bash
brew install maven
```

### Step 4: 验证
```bash
java -version
mvn -version
```

---

## 方法 2: 使用 SDKMAN（免 sudo）

### Step 1: 安装 SDKMAN
```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

### Step 2: 安装 Java 17
```bash
sdk install java 17.0.9-tem
```

### Step 3: 安装 Maven
```bash
sdk install maven
```

### Step 4: 验证
```bash
java -version
mvn -version
```

---

## 方法 3: 手动下载安装

### Java 17
1. 访问：https://adoptium.net/
2. 下载 macOS 版本
3. 安装后配置 JAVA_HOME

### Maven
1. 访问：https://maven.apache.org/download.cgi
2. 下载二进制包
3. 解压并添加到 PATH

---

## 快速验证

安装完成后，运行以下命令验证：

```bash
# 检查 Java
java -version
# 应该看到：openjdk version "17.x.x"

# 检查 Maven
mvn -version
# 应该看到：Apache Maven 3.9.x
```

---

## 一键安装脚本

我已经为你准备了两个脚本：

### 脚本 1: 使用 Homebrew（需要输入密码）
```bash
cd /Users/zhouzhenjiang/workspace/payment-mcp-mvp
./install-java-mac.sh
```

### 脚本 2: 使用 SDKMAN（免密码）
```bash
cd /Users/zhouzhenjiang/workspace/payment-mcp-mvp
./install-no-sudo.sh
```

---

## 常见问题

### Q1: Homebrew 安装失败（网络问题）
```bash
# 使用国内镜像
export HOMEBREW_BREW_GIT_REMOTE="https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/brew.git"
export HOMEBREW_CORE_GIT_REMOTE="https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/homebrew-core.git"
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Q2: SDKMAN 安装失败
```bash
# 手动下载安装
curl -s "https://get.sdkman.io" > sdkman-install.sh
bash sdkman-install.sh
```

### Q3: Java 版本不对
```bash
# 查看已安装的 Java
/usr/libexec/java_home -V

# 切换版本（添加到 ~/.zshrc）
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
source ~/.zshrc
```

---

## 安装完成后

```bash
cd /Users/zhouzhenjiang/workspace/payment-mcp-mvp
./start.sh
```

---

**抱歉，由于系统权限限制，我无法自动完成安装。但脚本已经为你准备好了，只需运行一个命令即可！** 🙏
