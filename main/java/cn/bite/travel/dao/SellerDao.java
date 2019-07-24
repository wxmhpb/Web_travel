package cn.bite.travel.dao;

import cn.bite.travel.domain.Seller;

public interface SellerDao {
    //根据卖家sid查询卖家信息
    public Seller findBySid(int sid);
}
