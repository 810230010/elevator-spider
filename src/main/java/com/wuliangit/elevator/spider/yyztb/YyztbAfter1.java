package com.wuliangit.elevator.spider.yyztb;

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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class YyztbAfter1 implements PageProcessor {
    private Site site = Site.me().setSleepTime(1000).setRetryTimes(2).setCharset("GBK");
    private static final String BASE_URL = "http://www.yyztb.gov.cn";   //余姚市招标招投网
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.yyztb.gov.cn/Content.jsp.*").match()){
            String title = html.xpath("/html/body/table/tbody/tr/td[2]/div[2]/table/tbody/tr[1]/td/span/text()").toString();
            System.out.println(title);
            BidService bidService = SpringUtils.getBean(BidService.class);
            if(!bidService.isExistByTitle(title) && Common.hasZhongBiaoKeyword(title)){
                String all = html.xpath("/html/body/table/tbody/tr/td[2]/div[2]/table/tbody/tr[2]/td/span[1]/text()").toString();
                String public_time = all.replace("年", "-")
                        .replace("月", "-")
                        .replace("日", "");
                String content = html.xpath("//*[@id=\"vsb_content\"]").toString();
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
                bid.setType(Common.BID_STATE_ZHONGBIAO);
                bid.setUrl(page.getUrl().toString());
                bidService.insertBid(bid);
            }


        }else{
            Selectable selectable = html.xpath("//*[@id=\"searchlistform1\"]/table[1]/tbody");
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

        List<Request> requests = new LinkedList<Request>();

        for (int i = 1; i <= 19; i++) {

            Request request = new Request("http://www.yyztb.gov.cn/SeacheOut.jsp?currentnum=" + i + "&newskeycode=%B5%E7%CC%DD");
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
