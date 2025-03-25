ms079
=====

冒险岛 v079 版本，是大巨变前最经典的版本。

---

## 一、运行条件

**运行必备条件——数据库：**

- [MySQL 5.7.30+][3] （提取码：6ifn）
  - 注意，原版本在 MySQL 4.x 环境下运行，我自己的服务器，使用阿里云的 5.7.32-log 版本，可以正常运行
- PostgreSQL（暂未支持）
- Sqlite3（暂未支持）

### 1.1 开发环境

- [JDK 1.8][1]：注意，原版本仅在 Java7 JRE 环境运行，迁移到 Java8 将存在 js 脚本导入的问题，目前已修复此兼容性问题
- [IntelliJ IDEA][2]：推荐使用的开发工具

启动入口是 `com.github.mrzhqiang.maplestory.MapleStoryApplication.java` 的 `main` 方法。

### 1.2 运行环境

1. 安装 [JDK 1.8][1] （或以上）运行环境
2. 设置 `JAVA_HOME` 环境变量
3. 使用启动脚本启动即可
4. 【推荐】使用 GUI 启动，获得更多运维功能

## 二、如何编译？

编译通常使用 [Maven][4] 插件，如果你是开发者，建议通过 IDEA 工具进行编译。

**更新：目前已加入 assembly 编译插件，可以同时打包 `wz` 和 `脚本` 以及其他资源到 `ms079-[version]-dist.zip` 压缩文件中。此压缩文件可以作为绿色安装包使用，只需要解压出来，安装运行环境，以及初始化数据库，然后就可以运行了。**

### 2.1 IDEA 工具

展开右侧栏 Maven 菜单中的 Lifecycle 选项，双击 `compile` 命令，即可生成编译文件。

**需要打包为 `ms079-[version].jar` 的话，则使用 `package` 命令即可。**

### 2.2 Maven 插件

在项目根目录下打开 CMD 工具（需要安装 Maven 插件）：

- 编译命令：`mvn clean compile`
- 打包命令：`mvn clean package`

## 三、初始化数据库

~~IDEA 社区版不支持数据库操作。~~

社区版可以找到 Database Navigator 插件，也可以很舒服的操作数据库。

IDEA 旗舰版可以连接数据库。

找到 `db/ms079.sql` 文件，右键运行。记得先连接数据库，选好默认的 schema 实例。

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
5. 【可选】或者也可以打开 `启动服务端-命令行.bat` or `启动服务端-命令行.sh`
6. 等待显示【启动成功，可以进入游戏】类似的信息即可

**注意：新版本已无法通过 JDK7 来编译，要使用 JDK7 版本编译，请自行找到第一次提交的 git 版本。**

*提示：`ms079-[version]-dist.tar.gz` 文件是 Linux 服务器的压缩文件，目前没有经过 Linux 服务器的测试。*

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