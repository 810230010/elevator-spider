package com.wuliangit.elevator.service.impl;

import com.wuliangit.elevator.dao.BidMapper;
import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nilme on 2017/6/13.
 */

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidMapper bidMapper;

    @Override
    public void insertBid(Bid bid) {
        bidMapper.insertSelective(bid);
    }

    @Override
    public boolean isExistByTitle(String sid) {
        String id = bidMapper.isExistByTitle(sid);
        if (StringUtils.isEmpty(id)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isExistBySid(String sid) {
        String id = bidMapper.isExistBySid(sid);
        if (StringUtils.isEmpty(id)) {
            return false;
        } else {
            return true;
        }
    }

}
