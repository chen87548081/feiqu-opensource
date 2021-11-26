# 飞趣社区开源版本

#### 介绍
飞趣社区做了快两年了，最近也想不到什么新功能去做了，于是想起了开源的事情，我一个人开发终究不能让这个社区走向前方，于是我下定决心开源，开源之前我也蛮纠结的，就像是把自己辛辛苦苦做的东西送给别人了，但是转念一想，也许这会为我的社区向前发展提供助力，不同人的思想或许会激发灵感，所以我就决定开源了。

时隔多年，我又来看了下我的项目，不知道现在有没有落伍，官网地址换了，大家关注下
社区网站地址：https://www.f2qu.com  现已支持https
qq讨论群：632118669  本人qq联系方式：573047307  在线接一些简单的二次开发项目，赚点钱养家糊口

有什么问题可以在下面提问，或者加群讨论。

这个项目一开始使用springmvc开发，后来听了群里的人说springboot多么好多么好的，我也就上了贼船，开始了springboot之旅，从此一发不可收拾。

springmvc项目就不演示给大家看了，毕竟那么多配置文件，想想也烦，大家一起加入springboot 大家庭吧

此项目使用了hutool工具类(https://www.hutool.club/)作支撑，参考了
zheng项目（https://gitee.com/shuzheng/zheng）以及
ruoyi的项目（https://gitee.com/y_project/RuoYi），
https://gitee.com/vakinge/jeesuite-libs
，这三个项目给了我很多的帮助，
在此谢谢这三个项目的作者，页面ui使用了layui，集成了阿里云oss（支持前端和后端上传）、七牛云，支持代码自动生成，支持数据库读写分离，减去了70%的工作量，让你更多的精力放在写业务代码的过程中。

当然这个项目还可以用来给你们公司做前端展示页面，也是非常方便的。



#### 软件架构
使用java作为后端开发 使用springboot、mysql、druid、 mybatis、pagehelper、javamail、redis、beetl、hutool、layui、jeesuite、webmagic相关技术集成开发的一个web应用
并且支持爬虫、发邮件。你想要的功能在这应有尽有，如果你还希望集成什么，欢迎提issue
用户信息是通过cookie保存的，为期30天


#### 安装教程

1. mysql创建一个数据库 cwd_boring
2. 导入sql sql目录下面的
3. 安装redis 6379端口

#### 使用说明

1. 使用jdk8
2. mysql 5.7 用户名密码 root root
3. 配置文件里面为
   application-dev.yml:
   feiqu-redis:
   servers: localhost:6379 #redis服务的ip和端口
   password:
   mail:
   default-encoding: utf-8
   host:  smtp.qq.com #改成你的邮件主机
   username: 123@qq.com #邮件服务 登陆用户名
   password: 2333 #邮件服务 登陆密码
   必须改为自己的配置才能生效
   java类里面
   com.feiqu.framwork.constant.CommonConstant.USER_ID_COOKIE
   com.feiqu.framwork.constant.CommonConstant.USER_COOKIE_SECRET
   com.feiqu.framwork.constant.CommonConstant.FORGET_PASSWORD_SECRET
   必须改为自己的配置才能生效
4. ip2region.db -> \feiqu-opensource\feiqu-front\src\main\resources\ip2region\ip2region.db 转移到自己的文件位置 application-dev.yml:22
   这个是参考的https://gitee.com/lionsoul/ip2region
   大家也可以去 https://gitee.com/lionsoul/ip2region/blob/master/data/ip2region.db  下载最新的文件
   然后放到对应的目录就可以了
5. com.feiqu.framwork.aspectj.DataSourceAspect 把注释去掉支持读写分离
6. 阿里云和七牛云的配置在——》feiqu-opensource\feiqu-front\src\main\resources\application.properties
   七牛云
   public.filesystem.provider=qiniu
   public.filesystem.bucketName=***
   public.filesystem.urlprefix=***
   public.filesystem.accessKey=***
   public.filesystem.secretKey=***
   picUrl = FileSystemClient.getPublicClient().upload(CommonConstant.FILE_NAME_PREFIX+currentTimeMillis+".jpg", img);
   阿里云
   aliyun.filesystem.bucketName=***
   aliyun.filesystem.endpoint=***
   aliyun.filesystem.accessKey=***
   aliyun.filesystem.secretKey=***
   aliyun.filesystem.urlprefix=***
   使用：videoUrl = FileSystemClient.getClient("aliyun").upload("video/"+fileName,localFile);
   改成你想要的
   注意跨域和url_prefix
7. 支持第三方登陆 现已集成了qq、微博  微信好像要钱就没弄。。。。
   application.properties里面
   app_id_qq=***
   app_key_qq=***
   app_id_sina=***
   app_key_sina=***
   改成自己的就可以了 记得到qq互联之类的绑定自己的域名哦 https://connect.qq.com/?id=1

8. 没有写专门的后台 就直接放在前端了 把用户的角色 cwd_boring.fq_user的role字段改成1 就是管理员角色了
   点击用户头像进去 可以看到左侧的tab多了很多菜单项 那就是后台管理员的操作地方


9. 代码生成放在\feiqu-opensource\feiqu-generator 项目中
   具体的类是：com.feiqu.generator.util.CSSGenerator
   在main方法里面：generator(Global.getConfig("gen.packageName"),true,LAST_INSERT_ID_TABLES,false,"FQ_USER_PAY_WAY");
   这句话 把最后一个参数改成自己想要生成代码的表

10. 支持登陆用户更换背景图片 （pg：一开始运行起来的项目 基本上都是空页面）

11. 文章发表支持富文本 适用quill框架，相信大家都知道

12. [图片] 在这边application.yml 配置开发环境和开发环境 每次打包的时候只要切换一下 我感觉挺好的

13. redis和mysql的密码都是通过aes加密的，工具类在：com.feiqu.common.utils.AESUtil，避免了明文展示密码。 feiqu-system 是和数据库打交道的

14. com.feiqu.framwork.init.FeiquInitTrigger和com.feiqu.framwork.init.Initialize是两个初始化的方法，大家可以抽空看一下

15. 我抽出了一个定时任务模块，仿照ruoyi的，SysJobController:在这里面提供图形化的任务界面管理

16. 这个项目没有后台，通过修改fq_user表的role字段，如果是1，就是管理员。 你们可以看下这个页面：templates/common/_user_menu.html
    后台相关的代码没有开源，不过可以参考ruoyi，因为我就是参考它做的

17. 私信模块报错的，是mysql的group by问题，可以百度一下解决方案

18. 官网代码和开源的有一些区别，还请各位见谅，未能提供完整的业务代码。官网的有完整的后台项目，如需获取，请私聊作者，谢谢。

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


演示图片：
![首页](https://images.gitee.com/uploads/images/2019/0415/212716_fb07524f_1233679.png "TIM图片20190415212647.png")
![我的小窝](https://images.gitee.com/uploads/images/2019/0415/212801_be0d1782_1233679.png "TIM图片20190415212747.png")
![文章显示](https://images.gitee.com/uploads/images/2019/0415/212852_0f216b2e_1233679.png "TIM图片20190415212836.png")
![网址导航](https://images.gitee.com/uploads/images/2019/0415/212953_3c777e7c_1233679.png "TIM图片20190415212936.png")
![后台管理页面](https://images.gitee.com/uploads/images/2019/0608/224223_2ecc1c13_1233679.png "微信图片_20190608224135.png")

还有一些后台管理页面，这个是通过字段区分渲染的，如果用户的role字段是1，那就显示后台菜单