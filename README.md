
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">RuoYi v3.9.0</h1>
<h4 align="center">基于SpringBoot+Vue前后端分离的Java快速开发框架</h4>
<p align="center">
	<a href="https://gitee.com/y_project/RuoYi-Vue/stargazers"><img src="https://gitee.com/y_project/RuoYi-Vue/badge/star.svg?theme=dark"></a>
	<a href="https://gitee.com/y_project/RuoYi-Vue"><img src="https://img.shields.io/badge/RuoYi-v3.9.0-brightgreen.svg"></a>
	<a href="https://gitee.com/y_project/RuoYi-Vue/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

## 平台简介

若依是一套全部开源的快速开发平台，毫无保留给个人及企业免费使用。

* 前端采用Vue3、Element Plus。
* 后端采用Spring Boot、Spring Security、Redis & Jwt。
* 权限认证使用Jwt，支持多终端认证系统。
* 支持加载动态权限菜单，多方式轻松权限控制。
* 高效率开发，使用代码生成器可以一键生成前后端代码。
* 提供了技术栈（[Vue3](https://v3.cn.vuejs.org) [Element Plus](https://element-plus.org/zh-CN) [Vite](https://cn.vitejs.dev)）版本[RuoYi-Vue3](https://gitcode.com/yangzongzhuan/RuoYi-Vue3)，保持同步更新。

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 缓存监控：对系统的缓存信息查询，命令统计等。
17. 在线构建器：拖动表单元素生成相应的HTML代码。
18. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 在线体验

- admin/admin123  

演示地址：http://vue.ruoyi.vip  
文档地址：http://doc.ruoyi.vip

## 升级内容

- springboot版本升级为3.5.0
- jdk版本升级为17
- mybatis版本升级为3.0.3
- spring-framework版本升级为6.2.7

## 环境搭建

- maven环境

  需要提前在IDEA中配置maven的环境和本地仓库，项目中的jar的定义会自动下载到本地仓库中

- 数据库环境

  - 创建自己的数据库，导入代码中的sql脚本即可

  - sql脚本位置：`项目根目录\sql\ry_20250522.sql`

  - 配置数据库，找到`ruoyi-admin`模块下的`application.yml`文件

    ```yaml
      datasource:
        driverClassName: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
        username: root
        password: 1234
    ```

    - 数据库名称、用户名和密码都改为自己的即可

- redis环境

  由于若依已完成的功能中已经集成了redis，所以提前准备好redis的环境

  - 启动redis之后，也是`application.yml`文件进行配置即可

    ```yaml
      # redis 配置
      data:
        redis:
          # 地址
          host: localhost
          # 端口，默认为6379
          port: 6379
          # 数据库索引
          database: 0
          # 密码
          password: 123456
    ```

- 启动项目

  找到`ruoyi-admin`模块中的运行`com.ruoyi.RuoYiApplication.java`，出现如下图表示启动成功。

  ```sh
  (♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  
   .-------.       ____     __        
   |  _ _   \      \   \   /  /    
   | ( ' )  |       \  _. /  '       
   |(_ o _) /        _( )_ .'         
   | (_,_).' __  ___(_ o _)'          
   |  |\ \  |  ||   |(_,_)'         
   |  | \ `'   /|   `-'  /           
   |  |  \    /  \      /           
   ''-'   `'-'    `-..-'    
  ```

  