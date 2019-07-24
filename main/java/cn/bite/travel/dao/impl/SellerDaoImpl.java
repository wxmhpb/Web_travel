package cn.bite.travel.dao.impl;

import cn.bite.travel.dao.SellerDao;
import cn.bite.travel.domain.Route;
import cn.bite.travel.domain.Seller;
import cn.bite.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class SellerDaoImpl implements SellerDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public Seller findBySid(int sid) {
        String sql="select * from tab_seller where sid=?";
       return template.queryForObject(sql,new BeanPropertyRowMapper<Seller>(Seller.class),sid) ;
    }
}
