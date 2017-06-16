package com.wuliangit.elevator.spider.newZmctc;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/6/16.
 */
public class NewZmctcBeforeProcessor2 implements PageProcessor{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    
    @Override
    public void process(Page page) {
         BidService bidService = SpringUtils.getBean(BidService.class);
         Html html = page.getHtml();
         if(page.getUrl().regex("http://new.zmctc.com/zjgcjy/infodetail/.*").match()){
             String title = html.xpath("//*[@id=\"tdTitle\"]/font[1]/b/text()").toString();
             System.out.println(html.xpath("//*[@id=\"tdTitle\"]/font[2]/text()").toString().substring(7,16));
             if(bidService.isExistByTitle(title)){
                 return;
             }

             Bid bid = new Bid();
             bid.setUrl(page.getUrl().toString());
             bid.setPublicTime(html.xpath("//*[@id=\"tdTitle\"]/font[2]/text()").toString().substring(7,16));
             bid.setContent(html.xpath("//*[@id=\"TDContent\"]/div[2]").toString().replaceAll("(\0|\\s*|\r|\n)",""));
             bid.setType("ZHAOBIAO");
             bid.setTitle(title);
             bidService.insertBid(bid);
         }else{
             Selectable selectable = html.xpath("//*[@id=\"SearchResult1_DataGrid1\"]/tbody");
             Selectable links = selectable.links();
             page.addTargetRequests(selectable.links().regex("http://new.zmctc.com/zjgcjy/infodetail/.*").all());
         }
    }

    @Override
    public Site getSite() {
        return site;
    }

}
