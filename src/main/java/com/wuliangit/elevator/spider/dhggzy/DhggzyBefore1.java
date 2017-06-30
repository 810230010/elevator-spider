package com.wuliangit.elevator.spider.dhggzy;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class DhggzyBefore1 implements PageProcessor{
    private Site site = Site.me().setSleepTime(1000).setRetryTimes(2);
    private static final String BASE_URL = "http://www.dhggzy.cn";
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.dhggzy.cn/dhggzy/InfoDetail/.*").match()){
            String title = html.xpath("//*[@id=\"tdTitle\"]/font[1]/b/text()").toString();
            System.out.println(title);
            BidService bidService = SpringUtils.getBean(BidService.class);
            if(!bidService.isExistByTitle(title) && Common.hasZhaoBiaoKeyword(title)){
                String all = html.xpath("//*[@id=\"tdTitle\"]/font[2]/text()").toString();
                String public_time = all.substring(all.indexOf("：") + 2, all.indexOf("】") - 1).replaceAll("/", "-");
                System.out.println(public_time);
                String content = html.xpath("//*[@id=\"TDContent\"]").toString();
                Bid bid = new Bid();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sf.parse(public_time);
                    date = java.sql.Date.valueOf(public_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(date);
                bid.setPublicTime(date);
                bid.setTitle(title);
                bid.setContent(content);
                bid.setType(Common.BID_STATE_ZHAOBIAO);
                bid.setUrl(page.getUrl().toString());
                bidService.insertBid(bid);
            }


        }else{
            Selectable selectable = html.xpath("//*[@id=\"SearchResult1_DataGrid1\"]/tbody");
            Selectable links = selectable.links();
            List<String> requestList = links.all();

            for(String request: requestList){
                request = BASE_URL + request;
            }
            page.addTargetRequests(requestList);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
    public static Request getRequest() {
            Request request = new Request("http://www.dhggzy.cn/dhggzy/showinfo/searchresult.aspx?keyword=电梯&searchtype=title");
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
        return request;
    }
}
