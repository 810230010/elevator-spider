package com.wuliangit.elevator.service;

import com.wuliangit.elevator.entity.Bid;

/**
 * Created by nilme on 2017/6/13.
 */
public interface BidService {

    public void insertBid(Bid bid);

    public boolean isExistBySid(String sid);

    public boolean isExistByTitle(String sid);
}
