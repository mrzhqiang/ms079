ms079
=====

冒险岛 v079 版本，是大巨变前最经典的版本。

---

## 一、环境准备

**所有环境**，必须安装数据库：

- [MySQL 5.7.30+][3] （提取码：6ifn）
  - 注意，原版本在 MySQL 4.x 环境下运行，我自己的服务器，使用阿里云的 5.7.32-log 版本，可以正常运行

### 1.1 开发环境

- [JDK 1.8][1]：注意，原版本仅在 Java7 JRE 环境运行，迁移到 Java8 将存在 js 脚本导入的问题，待未来修复此兼容性问题
- [IntelliJ IDEA][2]：推荐使用的开发工具

启动入口是 `server.Start` 的 `main` 方法。

### 1.2 运行环境

必须安装 [JDK 1.8][1] 运行环境。

必须设置 `JAVA_HOME` 环境变量。

Linux 服务器参考 `启动服务器-命令行.bat` 逐一执行命令即可启动，有时间我再翻译为 `.sh` 脚本。

Windows 系统或服务器，启动脚本二选其一。

## 二、如何编译？

通常使用 IDEA 工具或 [Maven][4] 插件进行编译操作。

**更新：目前已加入 assembly 编译插件，可以同时打包 `wz` 和 `脚本` 以及其他资源到 `ms079-[version]-dist.zip` 压缩文件中。**

**提示：此压缩文件可以作为发布包使用，只需要解压出来，安装运行环境，以及初始化数据库，然后运行即可。**

### 2.1 IDEA 工具

以下方式任选其一：

1. 不想使用 Maven 的话，参考 `Build Artifacts` 教程，它可以将所有依赖库添加到 jar 包中，因此可以通过 `java -jar xxxx.jar` 方式直接运行
2. 点击工具上方的 `Build` 菜单，生成编译文件
3. 使用 `Ctrl + F9` 快捷键，执行 `Build` 命令，生成编译文件
4. 展开工具右侧栏 Maven 菜单中的 Lifecycle 选项，双击 `compile` 命令，生成编译文件

**需要打包为 `ms079-[version].jar` 的话，在第四种方式中，替换 `compile` 为 `package` 即可。**

### 2.2 Maven 插件

在项目根目录下打开 CMD 工具（需要安装 Maven 插件）：

- 编译命令：`mvn clean compile`
- 打包命令：`mvn clean package`

**注意，Maven 命令也可以从 IDEA 中运行，使用 `Alt + F12` 快捷键（内置 Maven 插件）即可。**

## 三、初始化数据库

IDEA 社区版不支持数据库操作。

IDEA 旗舰版可以连接数据库，然后找到 `db/ms079.sql` 文件，右键运行。记得先连接数据库，选好默认的 schema 实例。

另外，还可以下载 [Navicat Permium 15][5] 工具（提取码：`j6lt`），来执行操作：

1. 连接本地 MySQL 数据库
2. 创建名为 ms079 的数据库实例，编码为 `utf-8`
3. 在数据库上右键，选择 Execute SQL File...
4. 找到 `db/ms079.sql` 文件，点击开始执行

## 四、如何运行？

运行分两步：首先启动服务端，然后安装客户端进行登录。

### 4.1 启动服务端

**前提是 `JAVA_HOME` 环境变量正常，以及数据库初始化完毕。**

1. 打包（已有 `ms079-[version]-dist.zip` 文件，可忽略此步骤）
   - 展开 IDEA 右侧栏 Maven 菜单中的 `Lifecycle` 选项
   - 双击 `clean` 清理旧文件
   - 双击 `package` 进行打包
   - 确定已生成 `/target/ms079-[version]-dist.zip` 文件
   - 【可选】或者通过 Maven 插件，在根目录下使用 `mvn clean package` 命令进行打包
2. 解压 zip 文件到某一个文件夹
3. 进入文件夹，修改 `服务端配置.ini` 配置文件中的参数（主要是数据库的账号密码）
4. 打开 `启动服务端-GUI.bat`，点击【启动服务端】按钮
5. 【可选】或打开 `启动服务端-命令行.bat`
6. 等待显示【启动成功，可以进入游戏】类似的信息即可

**注意，js 文件的提示是由于 Java8 不兼容的问题，如需修复，可以选择使用 `JDK 1.7_80` 来编译打包和运行。**

**提示：`ms079-[version]-dist.tar.gz` 文件是 Linux 服务器的压缩文件，目前没有经过 Linux 服务器的测试。**

### 4.2 客户端登录

1. 安装 [冒险岛v079客户端][6]
2. 删除客户端中的 HShield 目录，下载 [079 私服过 HS 文件][7] （提取码：`7i0u`）进行替换
4. 拷贝 `V079登录器.bat` 到客户端下，双击运行
    - 如需联网：请编辑 `服务端配置.ini` 和 `V079登录器.bat` 中的相关 IP 地址和端口
    - 简单起见：仅将所有的 `127.0.0.1` 修改为服务器 IP 地址，端口保持原样
    - **云服务器还需要在安全组中，打开对应的端口授权，切记切记**

---

# 声明

仅供个人学习交流使用，不得用于任何商业途径，请在下载 24 小时后删除。



[1]:https://alywp.net/5whNJG
[2]:https://www.jetbrains.com/idea/
[3]:https://pan.baidu.com/s/1v-2jXg9xqNmo5ww5YjUhQQ
[4]:https://maven.apache.org/download.cgi
[5]:https://pan.baidu.com/s/1kZwb2ZdOjf5ZG_HPkWtwWQ
[6]:https://alywp.net/2bBtbJ
[7]:https://pan.baidu.com/s/1gAOhxhwxd1T4bqX8HSoFNQ