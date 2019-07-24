package cn.bite.travel.service.impl;

import cn.bite.travel.dao.FavoriteDao;
import cn.bite.travel.dao.impl.FavoriteDaoImpl;
import cn.bite.travel.domain.Favorite;
import cn.bite.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    FavoriteDao favoriteDao=new FavoriteDaoImpl();
    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite favorite=favoriteDao.findByRidAndUid(Integer.parseInt(rid),uid);
        return favorite!=null;
    }

    @Override
    public void add(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
    }
}
