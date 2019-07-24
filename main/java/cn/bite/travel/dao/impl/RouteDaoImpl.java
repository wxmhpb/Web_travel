package cn.bite.travel.dao.impl;

import cn.bite.travel.dao.RouteDao;
import cn.bite.travel.domain.Route;
import cn.bite.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 旅游线路商品的dao的实现层
 */
public class RouteDaoImpl implements RouteDao {
    //声明查询模板对象
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource()) ;
    @Override
    public int findTotalPage(int cid,String rname) {


        /**
         * 通过分类id查询总记录数
         * @param cid
         * @return
         */
        //准备sql
        //String sql = "select  count(*) from tab_route where cid = ?" ;
        //查询
        //定义sql模板
        String sql="select count(*) from tab_route where 1=1";
        StringBuilder sb=new StringBuilder(sql);
        List params=new ArrayList();//条件
        //判断参数是否有值
        if(cid!=0){
            sb.append("and cid=?");
            params.add(cid);
        }
        if(rname!=null&&rname.length()>0){
            sb.append("and rname like ?");
            params.add("%"+rname+"%");
        }
        sql=sb.toString();
        return template.queryForObject(sql,Integer.class,params.toArray()) ;

    }

    /**
     * 查询当前页面数据集合,通过分页查询
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    //分页查询，一页最多查询5条记录
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
        //准备sql
      //  String sql = "SELECT * FROM tab_route where  cid = ? limit ?,?" ;    //第一个问号是起始位置，第二个问号是查的记录数
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");//分页条件

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);


        //查询
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),cid,start,pageSize) ;
    }

    @Override
    public Route findOne(int rid) {
        String sql="select * from tab_route where rid=?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid) ;
    }
}
