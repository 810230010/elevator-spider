package com.wuliangit.elevator.spider.haishuggzy;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.spider.Common;
import com.wuliangit.elevator.util.SpringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class HaishuGgzyAfter1 implements PageProcessor{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("utf-8");
    private static final String BASE_URL = "http://ggzy.haishu.gov.cn/";
    @Override
    public void process(Page page) {
         Html html = page.getHtml();
        if(page.getUrl().regex("http://ggzy.haishu.gov.cn/info.aspx.*").match()){
            String curUrl = page.getUrl().toString();
            String desUrl = BASE_URL + "info_html.aspx" + curUrl.substring(curUrl.indexOf("?")) ;
            page.addTargetRequest(desUrl);
        }else if(page.getUrl().regex("http://ggzy.haishu.gov.cn/info_html.aspx.*").match()) {

            String title = html.xpath("//*[@id=\"lblTitle\"]/text()").toString();
            if (title.contains(Common.BID_ZHONGBIAO_KEYWORD) ) {
                BidService bidService = SpringUtils.getBean(BidService.class);
                if(!bidService.isExistByTitle(title)){
                    String public_time = html.xpath("//*[@id=\"lblAddTime\"]/text()").toString();
                    String content = html.xpath("//*[@id=\"Zoom\"]").toString();
                    System.out.println(title);
                    Bid bid = new Bid();
                    bid.setPublicTime(public_time);
                    bid.setTitle(title);
                    bid.setContent(content);
                    bid.setType("ZHONGBIAO");
                    bid.setUrl(page.getUrl().toString());
                    bidService.insertBid(bid);
                }
            }
        }else{
            List<String> requestList = new LinkedList<String>();
            Document doc = html.getDocument();
            Elements items = doc.getElementsByClass("searchTitle");
            for(Element item: items){
                String url =  BASE_URL + item.child(0).attr("href").toString();
                System.out.println(url);
                requestList.add(url);
            }
            page.addTargetRequests(requestList);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
    //发送get请求得到url页面
    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 14; i++) {

            Request request = new Request("http://ggzy.haishu.gov.cn/shlist.aspx?keyword=%E7%94%B5%E6%A2%AF&page="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
