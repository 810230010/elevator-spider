package com.wuliangit.elevator;


import com.wuliangit.elevator.spider.dsztb.DsztbAfter1;
import com.wuliangit.elevator.spider.dsztb.DsztbBefore1;
import com.wuliangit.elevator.spider.zsztb.ZsztbAfter1;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.Spider;

/**
 * Created by nilme on 2017/6/13.
 */
public class DoMain {
    public static void main(String[] args) throws Exception {
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


        //中国国际招标网（招标）
//        Spider.create(new ChinabiddingProcessor1()).addRequest(ChinabiddingProcessor1.getRequest()).thread(1).run();
        //杭州市政府采购网
//        Spider.create(new HzftBefore1Processor()).addRequest(HzftBefore1Processor.getRequest()).thread(1).run();
        //浙江省公共资源交易中心（招标文件公示）
//        Spider.create(new NewZmctcBeforeProcessor1()).addRequest(NewZmctcBeforeProcessor1.getRequest()).thread(1).run();
        //浙江省公共资源交易中心（招标公告）
//        Spider.create(new NewZmctcBeforeProcessor2()).addUrl("http://new.zmctc.com/zjgcjy/ShowInfo/SearchResult.aspx?keyword=电梯&searchtype=title&categoryNum=004001").thread(1).run();
        //中国国际招标网(中标)
//        Spider.create(new ChinabiddingProcessor2()).addRequest(ChinabiddingProcessor2.getRequest()).thread(1).run();


        //宁波市国际招标有限公司(招标)
        //Spider.create(new NbbiddingBeforeProcessor1()).addRequest(NbbiddingBeforeProcessor1.getRequest()).thread(2).run();
        //宁波市国际招标有限公司(中标)
        //Spider.create(new NbbiddingAfterProcessor1()).addRequest(NbbiddingAfterProcessor1.getRequest()).thread(2).run();

        //临海市公共资源交易中心
//        Spider.create(new LhztbProcessor1()).addRequest(LhztbProcessor1.getRequest()).thread(2).run();

        //台州公共资源交易网（招标)
//        Spider.create(new TzztbBeforeProcessor1()).addRequest(TzztbBeforeProcessor1.getRequest()).thread(4).run();
        //台州公共资源交易网（中标）
//        Spider.create(new TzztbAfterProcessor1()).addRequest(TzztbAfterProcessor1.getRequest()).thread(4).run();

        //新昌县公共资源交易网（招标）
//        Spider.create(new XcztbBeforeProcessor1()).addRequest(XcztbBeforeProcessor1.getRequest()).thread(1).run();
        //新昌县公共资源交易网（中标）
//        Spider.create(new XcztbAfterProcessor1()).addRequest(XcztbAfterProcessor1.getRequest()).thread(1).run();
        //新昌县公共资源交易网（采购）
//        Spider.create(new XcztbBeforeProcessor2()).addRequest(XcztbBeforeProcessor2.getRequest()).thread(1).run();
        //新昌县公共资源交易网（采购成功）
//        Spider.create(new XcztbAfterProcessor2()).addRequest(XcztbAfterProcessor2.getRequest()).thread(1).run();

        //嵊州市公共资源交易网
//        Spider.create(new SzztbProcessor1()).addRequest(SzztbProcessor1.getRequest()).thread(1).run();

        //诸暨市公共资源交易网招标
//        Spider.create(new ZjztbBeforeProcessor1()).addRequest(ZjztbBeforeProcessor1.getRequest()).thread(4).run();
        //诸暨市公共资源交易网中标
//        Spider.create(new ZjztbAfterProcessor1()).addRequest(ZjztbAfterProcessor1.getRequest()).thread(4).run();
        //诸暨市公共资源交易网采购
//        Spider.create(new ZjztbBeforeProcessor2()).addRequest(ZjztbBeforeProcessor2.getRequest()).thread(2).run();
        //诸暨市公共资源交易网采购成功
     //   Spider.create(new ZjztbAfterProcessor2()).addRequest(ZjztbAfterProcessor2.getRequest()).thread(2).run();
      //宁波市海曙区公共资源交易中心(招标)
        //Spider.create(new HaishuGgzyBefore1()).addRequest(HaishuGgzyBefore1.getRequest()).thread(2).run();
        //宁波市海曙区公共资源交易中心(中标)
        //Spider.create(new HaishuGgzyAfter1()).addRequest(HaishuGgzyAfter1.getRequest()).thread(2).run();

        //镇海公共资源交易中心(招标)
        //Spider.create(new ZhztbBefore1()).addRequest(ZhztbBefore1.getRequest()).thread(2).run();
        // 镇海公共资源交易中心(中标)
        //Spider.create(new ZhztbAfter1()).addRequest(ZhztbAfter1.getRequest()).thread(2).run();

//        Spider.create(new ZjztbAfterProcessor2()).addRequest(ZjztbAfterProcessor2.getRequest()).thread(2).run();

        //绍兴市上虞区
//        Spider.create(new SyztbProcessor1()).addRequest(SyztbProcessor1.getRequest()).thread(1).run();

        //绍兴市柯桥区招标
//        Spider.create(new SxxztbBeforeProcessor1()).addRequest(SxxztbBeforeProcessor1.getRequest()).thread(4).run();
        //绍兴市柯桥区中标
//        Spider.create(new SxxztbAfterProcessor1()).addRequest(SxxztbAfterProcessor1.getRequest()).thread(4).run();
        //绍兴市柯桥区采购
//        Spider.create(new SxxztbBeforeProcessor2()).addRequest(SxxztbBeforeProcessor2.getRequest()).thread(4).run();
        //绍兴市柯桥区采购成功
        //Spider.create(new SxxztbAfterProcessor2()).addRequest(SxxztbAfterProcessor2.getRequest()).thread(4).run();

        //宁波北仑区公共资源(招标)
        //Spider.create(new BlztbBefore1()).addRequest(BlztbBefore1.getRequest()).thread(2).run();
        //宁波北仑区公共资源(中标)
        //Spider.create(new BlztbAfter1()).addRequest(BlztbAfter1.getRequest()).thread(2).run();

        //慈溪公共资源交易(招标)
        //Spider.create(new CixiBefore1()).addRequest(CixiBefore1.getRequest()).thread(2).run();
        //慈溪公共资源交易(中标)
        //Spider.create(new CixiAfter1()).addRequest(CixiAfter1.getRequest()).thread(2).run();

        //宁波市鄞州区公共资源(招标)
        //Spider.create(new NbyzBefore1()).addRequest(NbyzBefore1.getRequest()).thread(2).run();
        //宁波市鄞州区公共资源(中标)
        //Spider.create(new NbyzAfter1()).addRequest(NbyzAfter1.getRequest()).thread(2).run();

        //余姚市招标投标王(招标)
        //Spider.create(new YyztbBefore1()).addRequest(YyztbBefore1.getRequest()).thread(2).run();
        //余姚市投标招标(中标)
        //Spider.create(new YyztbAfter1()).addRequest(YyztbAfter1.getRequest()).thread(2).run();

         //宁波大榭开发区(招标)
        //Spider.create(new DaxieBefore1()).addRequest(DaxieBefore1.getRequest()).thread(1).run();
        // 宁波大榭开发区(中标)
        //Spider.create(new DaxieAfter1()).addRequest(DaxieAfter1.getRequest()).thread(1).run();

        //舟山电子招投标平台(招标)
        //Spider.create(new ZsztbBefore1()).addRequest(ZsztbBefore1.getRequest()).thread(2).run();
        //舟山电子招投标平台(中标)
        //Spider.create(new ZsztbAfter1()).addRequest(ZsztbbAfter1.getRequest()).thread(2).run();

        //岱山县(招标)
        //Spider.create(new DsztbBefore1()).addRequest(DsztbBefore1.getRequest()).thread(2).run();
        //岱山县(中标)
        Spider.create(new DsztbAfter1()).addRequest(DsztbAfter1.getRequest()).thread(2).run();
    }

}
