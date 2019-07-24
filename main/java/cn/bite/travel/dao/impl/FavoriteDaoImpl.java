package cn.bite.travel.dao.impl;

import cn.bite.travel.dao.FavoriteDao;
import cn.bite.travel.domain.Favorite;
import cn.bite.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template=new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        //Favorite对象不一定存在。不一定已经被收藏，避免查到空用户
        Favorite favorite=null;
        try{
            String sql="select * from tab_favorite where rid=? and uid=?";
            favorite=template.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        }catch(DataAccessException e){
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public void add(int rid, int uid) {
        String sql="insert into tab_favorite values(?,?,?)";
         template.update(sql, rid, new Date(), uid);
    }
}
