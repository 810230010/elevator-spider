package com.wuliangit.elevator.spider.blztb;

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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class BlztbBefore1 implements PageProcessor {
    private Site site = Site.me().setSleepTime(1000).setRetryTimes(2);
    private static final String BASE_URL = "http://www.blztb.gov.cn/";   //宁波市北仑区公共资源
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.blztb.gov.cn/show.aspx.*").match()){
            String title = html.xpath("//*[@id=\"lbl_title\"]/text()").toString();
            System.out.println(title);
            BidService bidService = SpringUtils.getBean(BidService.class);
            if(!bidService.isExistByTitle(title)){
                String all = html.xpath("//*[@id=\"lbl_sou\"]/text()").toString();
                String public_time = all.substring(all.indexOf("时") + 3, all.indexOf("作") - 9);
                String content = html.xpath("/html/body/table[2]/tbody/tr/td[2]/table[3]/tbody/tr[3]/td/table/tbody/tr[1]/td").toString();
                Bid bid = new Bid();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd ");
                Date date = null;
                try {
                    date = sf.parse(public_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bid.setPublicTime(date);
                bid.setTitle(title);
                bid.setContent(content);
                bid.setType(Common.BID_STATE_ZHAOBIAO);
                bid.setUrl(page.getUrl().toString());
                bidService.insertBid(bid);
            }


        }else{
            Selectable selectable = html.xpath("//*[@id=\"form1\"]/table/tbody/tr/td/table[6]/tbody/tr[1]/td");
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

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 3; i++) {

            Request request = new Request("http://www.blztb.gov.cn/ssjg.aspx?slt=1&cls=80&pct=10&ord=m&by=1&key=%b5%e7%cc%dd&pageindex="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
