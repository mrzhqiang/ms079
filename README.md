ms079
=====

冒险岛 v079 版本，是大巨变前最经典的版本。

---

## 环境准备

从源码构建时，必须有以下环境：

- [JDK 1.8][1]
- [IntelliJ IDEA][2]

仅运行程序时，只需要以下环境：

- [JDK 1.8][1]（注意，原版本仅在 Java7 JRE 环境运行，迁移到 Java8 将存在 js 脚本导入的问题，待未来修复）

注意，数据库环境必不可少：

- [MySQL 5.7.30+][3] （提取码：6ifn）

**提醒：本仓库从原来的 Java7 迁移到 Maven+Java8 版本，或许有一些意外的情况发生，请自行斟酌。**

## 如何编译？

通常使用 IDEA 工具或 [Maven][4] 插件进行编译操作。

### IDEA 工具

以下方式任选其一：

- 点击工具上方的 `Build` 菜单
- 使用 `Ctrl + F9` 快捷键
- 展开工具右侧栏 Maven 菜单中的 Lifecycle 选项，双击 `compile` 命令

**需要打包为 `ms079.jar` 的话，替换第三步中的 `compile` 为 `package` 命令即可。**

### Maven 插件

在根目录下打开 CMD 之后：
- 输入编译命令：`mvn clean compile`

**打包命令为：`mvn clean package`。**

## 初始化数据库

IDEA 社区版不支持数据库操作。

IDEA 旗舰版可以连接数据库，然后执行 `db/ms079.sql` 文件。

另外，还可以下载 [Navicat Permium 15][5] 工具（提取码：`j6lt`），来执行操作：

1. 连接本地 MySQL 数据库
2. 创建名为 ms079 的数据库实例，编码为 `utf-8`
3. 在数据库上右键，选择 Execute SQL File...
4. 找到 `db/ms079.sql` 文件，点击开始执行

## 如何运行？

运行分两步：首先启动服务端，然后安装客户端进行登录。

### 启动服务端

1. 打包（已有 `ms079.jar` 文件，可忽略此步骤）
   - 展开 IDEA 右侧栏 Maven 菜单中的 `Lifecycle` 选项
   - 双击 `clean` 清理旧文件
   - 双击 `package` 进行打包
   - 确定已生成 `/target/ms079.jar` 文件
   - 或者通过 Maven 插件，在根目录下使用 `mvn clean package` 命令进行打包
2. 修改 `服务端配置.ini` 配置文件中的参数（主要是数据库的账号密码）
3. 打开 `GUI启动.bat`，点击【启动服务端】按钮
4. 等待显示【启动成功，可以进入游戏】类似的信息即可

### 客户端登录

1. 安装 [冒险岛v079客户端][6]
2. 删除客户端中的 HShield 目录，下载 [079 私服过 HS 文件][7] （提取码：`7i0u`）进行替换
4. 拷贝 `V079登录器.bat` 到客户端下，双击运行
    - 如需联网：请编辑 `服务端配置.ini` 和 `V079登录器.bat` 中的相关 IP 地址和端口
    - 简单起见：仅将所有的 `127.0.0.1` 修改为服务器 IP 地址，端口保持原样
    - 云服务器还需要在安全组中，打开对应的端口授权，切记切记

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