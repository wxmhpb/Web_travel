package cn.bite.travel.dao;

import cn.bite.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    //根据rid获取图片集合信息
    public List<RouteImg> findByRid(int rid);
}
