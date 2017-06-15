package com.wuliangit.elevator;

import com.wuliangit.elevator.dao.BidMapper;
import com.wuliangit.elevator.spider.chinabidding.ChinabiddingProcessor1;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;

/**
 * Created by nilme on 2017/6/13.
 */
public class DoMain {


    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-context.xml");
        System.out.println("【爬虫开始】请耐心等待一大波数据到你碗里来...");

//        BidService bean = SpringUtils.getBean(BidService.class);
//        BidMapper bidMapper = SpringUtils.getBean(BidMapper.class);
//        System.out.println(bean.isExistBySid("1"));
//        System.out.println(bidMapper.selectByPrimaryKey(1).getBidId());


//        //杭州公共资源网（开标安排）
//        Spider.create(new HzctcBefore1Processor()).addUrl("http://www.hzctc.cn/IndexTradeInfo/jsxm.html").thread(3).run();
//        //杭州公共资源网（招标公告）
//        Spider.create(new HzctcBefore2Processor()).addUrl("http://www.hzctc.cn/IndexTradeInfo/jsxm.html").thread(3).run();
//        //杭州公共资源网（中标公示）
//        Spider.create(new HzctcAfter1Processor()).addUrl("http://www.hzctc.cn/IndexTradeInfo/jsxm.html").thread(3).run();
//        //杭州公共资源网（中标公告）
//        Spider.create(new HzctcAfter2Processor()).addUrl("http://www.hzctc.cn/IndexTradeInfo/jsxm.html").thread(3).run();
//        //浙江省重大工程交易网
//        Spider.create(new ZmctcProcessor1()).addRequest(ZmctcProcessor1.getRequest()).thread(5).run();


        //中国国际招标网
        Spider.create(new ChinabiddingProcessor1()).addRequest(ChinabiddingProcessor1.getRequest()).thread(1).run();

    }

}
