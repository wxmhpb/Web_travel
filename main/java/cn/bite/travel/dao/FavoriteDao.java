package cn.bite.travel.dao;

import cn.bite.travel.domain.Favorite;

public interface FavoriteDao {
    public Favorite findByRidAndUid(int rid,int uid);
    public void add(int rid,int uid);
}
