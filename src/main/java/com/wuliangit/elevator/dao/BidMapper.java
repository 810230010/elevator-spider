package com.wuliangit.elevator.dao;

import com.wuliangit.elevator.entity.Bid;

public interface BidMapper {
    int deleteByPrimaryKey(Integer bidId);

    int insert(Bid record);

    int insertSelective(Bid record);

    Bid selectByPrimaryKey(Integer bidId);

    int updateByPrimaryKeySelective(Bid record);

    int updateByPrimaryKeyWithBLOBs(Bid record);

    int updateByPrimaryKey(Bid record);

    String isExistBySid(String sid);

    String isExistByTitle(String title);
}