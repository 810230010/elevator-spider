package com.wuliangit.elevator.spider.nbbidding;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
import org.jsoup.Jsoup;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/17.
 */
public class NbbiddingBeforeProcessor1 implements PageProcessor{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.nbbidding.com/support/bidnoticeview.asp.*").match()){
            String title = html.xpath("/html/body/table[2]/tbody/tr[1]/td/b/text()").toString();
            System.out.println(title);

                BidService bidService = SpringUtils.getBean(BidService.class);
                if(!bidService.isExistByTitle(title)){
                    String sid = html.xpath("/html/body/table[2]/tbody/tr[2]/td[2]/text()").toString();
                    String public_time = html.xpath("/html/body/table[2]/tbody/tr[4]/td[2]/text()").toString();
                    String content = html.xpath("//*[@id=\"zoom\"]").toString();
                    Bid bid = new Bid();
                    bid.setSid(sid);
//                    bid.setPublicTime(public_time);
                    bid.setTitle(title);
                    bid.setContent(content);
                    bid.setType("ZHAOBIAO");
                    bid.setUrl(page.getUrl().toString());
                    bidService.insertBid(bid);
                }


        }else{
            Selectable selectable = html.xpath("/html/body/table[2]/tbody");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://www.nbbidding.com/support/bidnoticeview.asp.*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 0; i <= 660; i++) {

            Request request = new Request("http://www.nbbidding.com/support/bidnoticelistall.asp?order=next&page="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
