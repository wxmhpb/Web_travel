package cn.bite.travel.service.impl;

import cn.bite.travel.dao.RouteDao;
import cn.bite.travel.dao.RouteImgDao;
import cn.bite.travel.dao.SellerDao;
import cn.bite.travel.dao.impl.RouteDaoImpl;
import cn.bite.travel.dao.impl.SellerDaoImpl;
import cn.bite.travel.domain.PageBean;
import cn.bite.travel.domain.Route;
import cn.bite.travel.domain.RouteImg;
import cn.bite.travel.domain.Seller;
import cn.bite.travel.service.RouteService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 旅游线路商品的业务实现层
 */
public class RouteServiceImpl implements RouteService {
    //声明旅游线路的dao层对象
    private RouteDao routeDao = new RouteDaoImpl() ;
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        //组装PageBean的信息
        //创PageBean对象
        PageBean<Route> pb = new PageBean<Route>() ;
        //设置当前页码
        pb.setCurrentPage(currentPage);

        //设置每页显示条数
        pb.setPageSize(pageSize);
        //封装总记录数(查数据库),设置总记录数
        int totalCount = routeDao.findTotalPage(cid,rname) ;
        System.out.println(totalCount);
        pb.setTotalCount(totalCount);

        //封装列表的数据集合(查数据库)
        //起始条数=(当前页码-1) * 每页显示条数
        int start = (currentPage-1) * pageSize ;
        List<Route> list = routeDao.findByPage(cid,start,pageSize,rname) ;
        pb.setList(list);

        //设置总页数=总记录数/每页显示记录条数
        int totalPage = totalCount % pageSize==0 ? totalCount/pageSize :(totalCount/pageSize)+1 ;
        pb.setTotalPage(totalPage);

        //返回
        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //根据id去route表中查询route对象c
        Route route=routeDao.findOne(Integer.parseInt(rid));
        //根据route的id查询图片集合信息
        List<RouteImg>routeImgsList= RouteImgDao.findByRid(Integer.parseInt(rid));
        //将集合设置到route对象
        route.setRouteImgList((routeImgsList));
        //根据route的sid查询商家对象
        Seller seller= SellerDao.findBySid(route.getSid());
        //将卖家信息设置到route对象
        route.setSeller(seller);
        return route;
    }
}
