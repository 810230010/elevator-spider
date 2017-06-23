package com.wuliangit.elevator.spider.zhztb;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.spider.Common;
import com.wuliangit.elevator.util.SpringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class ZhztbAfter1 implements PageProcessor {
     private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
     private static final String BASE_URL = "http://www.zhztb.gov.cn/";
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.zhztb.gov.cn/zfcgzbgg.*").match()){
            String title = html.xpath("/html/body/div[3]/div/div[2]/font/text()").toString();
            BidService bidService = SpringUtils.getBean(BidService.class);
            if(title.contains(Common.BID_ZHONGBIAO_KEYWORD) || title.contains(Common.BID_ZHONGBIAO_KEYWORD2)){
                if(!bidService.isExistByTitle(title)){
                    String public_time = html.xpath("/html/body/div[3]/div/div[2]/em/text()")
                            .toString().substring(6);
                    String content = html.xpath("/html/body/div[3]/div/div[2]/div").toString();
                    Bid bid = new Bid();
//                    bid.setPublicTime(public_time);
                    bid.setTitle(title);
                    bid.setContent(content);
                    bid.setType("ZHONGBIAO");
                    bid.setUrl(page.getUrl().toString());
                    System.out.println(page.getUrl().toString());
                    bidService.insertBid(bid);
                }


            }


        }else{
            Selectable selectable = html.xpath("/html/body/div[3]/div/div[2]/div[2]");
            Selectable links = selectable.links();
            List<String> requestList = selectable.links().regex("/zfcgzbgg/.*").all();
            for(String s: requestList){
                s += BASE_URL;
            }
            page.addTargetRequests(requestList);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
    public static Request[] getRequest() {

        List<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 11; i++) {

            Request request = new Request("http://www.zhztb.gov.cn/sr.jspx?q=%E7%94%B5%E6%A2%AF&pageNo="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
