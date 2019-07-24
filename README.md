# -1.技术
web层
a)Servlet：前端控制器
b)html：视图  ：页面显示
c)Filter：过滤器 ：
d)BeanUtils：数据封装  ：将user对象封装到map集合中
  为什么要封装user对象？

e)Jackson：json序列化工具 ：实现异步交互，服务器将后台数据显示到前台

过程：ObjectMapper是json的jar包核心类，首先创建一个ObjectMapper对象，创建json对象，用ObjectMapper的writeValueAsString方法返回json字符串，调用response的write方法将json字符串写回客户端

注意：写回客户端之前，要设置服务器响应格式：response.setContentType("application/json;charset=utf-8");


service层
f)Javamail：java发送邮件工具   MailUtils.sendMail();激活邮件
g)Redis：nosql内存数据库  ：优化数据库
h)Jedis：java的redis客户端   从JedisUtils中获取redis

Dao层
Mysql：数据库:构建sql语句
i)Druid：数据库连接池
j)JdbcTemplate：jdbc的工具：
创建一个JdbcTemplate对象，通过JDBCUtils.getDataSource()获取数据源，调用里面的query,update等方法执行sql语句


为什么使用jdbc工具而不是直接使用Jdbc操作数据库？


2.2.创建数据库
-- 创建数据库
CREATE DATABASE travel;
-- 使用数据库
USE travel;
--创建表
	复制提供好的sql

六张表：
用户表   tab_user
旅游分类表  tab_category
旅游路线表  tab_route
显示图片表  tab_route_img
卖家信息表 tab_seller
收藏表     tab_favorite

旅游分类表中：
每一次加载首页,都要从数据库去读取分类的信息
缺点:服务器压力大
用户体验差(等待页面整个加载完)


3.功能模块
3.1注册功能和激活邮箱：
1.前台实现表单的提交：
校验：用户名，密码，校验码，邮箱
当提交表单时，验证所有校验方法，当某一个组件失去焦点时，校验对应的方法：blur()
2.ajax异步提交表单：
因为我们前台使用的是html作为视图层，不能够直接从servlet相关的域对象获取值，只能通过ajax获取响应数据
service中查询用户是否存在，如果存在则注册失败，响应错误信息，如果不存在保存注册成功，用户信息到数据库：查询方法，保存对象方法
servlet中：1.获取对象
2.封装user对象
              3.调用service方法完成注册
              4.响应结果：如果存在则注册失败，响应错误信息，如果不存在，响应注册成功
   数据库中：编写sql语句查询，更新保存
   
3.邮件激活：
Service:根据激活码查询用户对象
       调用dao的修改激活状态的方法
Dao:根据激活码查询用户对象
    更新保存激活码状态
Servlet:获取激活码
       调用service完成激活
       获取状态：如果激活成功，跳转登录界面，如果失败，向客户端返回错误信息
发送邮件代码：设置激活码：唯一字符串：UnidUtil.getUnid()
              设置激活状态
               发送邮件正文：MailUtils.sendMail()

3.2登录与退出
登录：seesion域中user对象
1.根据用户名和密码的登录
数据库中编写sql语句
service中判断用户是否存在，如果存在判断是否激活，如果不存在，则未注册，错误时，用Resultinfo对象响应错误信息。
servlet获取用户信息，调用service方法

4.页面中登录用户的信息展示：欢迎回来，xxx
  前台：绑定按钮单击事件，将 “欢迎回来，xxx”拼接成字符串，设置到html中
  后台：servlet：从session中获取登录用户，将user写回客户端
3.退出：1.访问servlet，将session销毁：request.getSession().invalidate();
2.跳转到登录页面
response.sendRedirect(request.getContextPath()+"/login.html");

3.3旅游分类数据展示：
在数据库中查询分类表，返回list集合
Service方法中调用数据库中查询方法进行查询
servlet调用service方法查询list
将list序列化为json对象，返回客户端
前台：
发送ajax请求，实现数组遍历，完成真正的数据展示
将数据信息拼接成字符串，然后设置到ul的html中

3.4旅游线路分页展示：
1.类别id的获取
2.根据不同线路的id查询显示不同旅游类别的数据
3.每页显示：
   总记录数
   总页数
   当前所在页
   每页显示条数
   每页展示的数据集合
分页工具条显示：首页，上一页，末页，下一页
展示分页页码
        
            1.一共展示10个页码，能够达到前5后4的效果
            2.如果前边不够5个，后边补齐10个
            3.如果后边不足4个，前边补齐10个
 计算上一页，下一页
 列表数据展示：拼字符串
每次重新访问，回到顶部：window.scrollTo(0,0);

3.5旅游线路名称查询：
模糊查询

3.6旅游线路详情展示
    线路信息展示
图片展示：大图，小图
卖家信息展示
价格显示
3.7旅游线路收藏功能
   判断用户是否已经收藏过线路
   点击收藏按钮收藏线路


代码优化：
1.使用redis:
过程：
1.应该从JedisUtils获取redis的客户端的对象:Jedis
2.获取到客户端对象,操作redis数据库
3.判断当前redis是否存在categorys集合数据
第一次从数据库中查询出来就存储到redis中，以后查询都从redis中查询

2.期望数据中存储的顺序就是将来展示的顺序，使用redis的sortedset

3.每个功能一个Servlet这样代码太过于繁琐，一个模块一个Servlet，这样都继承BaseServlet,然后BaseServlet在继承HttpServlet这样较为方便，同时要修改所在路径

4.将序列化json对象操作封装为writeValue()方法，调用即可，减少代码冗余。

难点：
注册
分页查询
旅游线路详情展示
