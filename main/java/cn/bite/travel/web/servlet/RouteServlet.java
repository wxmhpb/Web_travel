package cn.bite.travel.web.servlet;

import cn.bite.travel.domain.PageBean;
import cn.bite.travel.domain.Route;
import cn.bite.travel.domain.User;
import cn.bite.travel.service.FavoriteService;
import cn.bite.travel.service.RouteService;
import cn.bite.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 旅游线路的后台业务
 */
//分页查询
@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    //service层业务对象
    private RouteService routeService = new RouteServiceImpl() ;
    public void  pageQuery(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //接收参数
        String currentPageStr=request.getParameter("currentPage");
        String pageSizeStr=request.getParameter("pageSize");
        String cidStr=request.getParameter("cid");
        //接收rname线路名称
        String rname=request.getParameter("rname");
       //解决乱码
        rname=new String(rname.getBytes("iso-8859-1"),"utf-8");

        int cid=0; //类别id
        //处理参数
        if(cidStr!=null&&cidStr.length()>0){
            cid=Integer.parseInt(cidStr);
        }
        int currentPage=0; //当前页面，如果不传递，则默认为第一页
        if(currentPageStr!=null&&currentPageStr.length()>0){
            currentPage=Integer.parseInt(currentPageStr);
        }else{
            currentPage=1;
        }
        int pageSize=0;//每页显示条数，如果不传递，默认显示5条记录
        if(pageSizeStr!=null&&pageSizeStr.length()>0){
            pageSize=Integer.parseInt(pageSizeStr);
        }else{
            pageSize=5;
        }
        //调用service查询PageBean对象
        PageBean<Route>pb=routeService.pageQuery(cid,currentPage,pageSize,rname);
        //将pageBean对象序列化为json,返回
        writeValue(pb,response);
    }
    public void  findOne(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //接收id
        String rid=request.getParameter("rid");
        //调用service查询route对象
        Route route=routeService.findOne(rid);
        //转为json写回客户端
        writeValue(route,response);
    }
    public void  isFavorite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       //获取线路id
        String rid=request.getParameter("rid");
        //获取当前登录的用户user
        User user=(User)request.getSession().getAttribute("user");
        int uid;
        if(user==null){
            //用户未登录
         uid=0;
        }else{
            //用户已登录
            uid=user.getUid();
        }
        //调用service查询是否已经收藏
        boolean flag= FavoriteService.isFavorite(rid,uid);
        //写回客户端
        writeValue(flag,response);
    }
    public void  addFavorite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取线路rid
        String rid=request.getParameter("rid");
        //获取当前登录的用户
        User user=(User)request.getSession().getAttribute("user");
        int uid;
        if(user==null){
            //用户未登录
            return;
        }else{
            //用户已经登录
            uid=user.getUid();
        }
        //调用service添加
        FavoriteService.add(rid,uid);
    }
}
