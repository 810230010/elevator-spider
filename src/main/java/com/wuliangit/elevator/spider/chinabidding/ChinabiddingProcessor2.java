package com.wuliangit.elevator.spider.chinabidding;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChinabiddingProcessor2 implements PageProcessor {

    // 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("UTF-8");

    public Site getSite() {
        return site;
    }

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);

        Html html = page.getHtml();

        if (page.getUrl().regex("http://www.chinabidding.com/bidDetail/.*").match()) {

            String title = html.xpath("/html/body/div[2]/active/h1/text()").toString();
            System.out.println(title);

            //存在了跳出
            if (bidService.isExistByTitle(title)) {
                return;
            }

            Bid bid = new Bid();
            bid.setUrl(page.getUrl().toString());
//            bid.setPublicTime(html.xpath("/html/body/div[2]/active/em/text()").toString().substring(0, 10));
            bid.setContent(html.xpath("/html/body/div[2]").toString().replaceAll("(\0|\\s*|\r|\n)",""));
            bid.setType("ZHONGBIAO");
            bid.setTitle(title);

            bidService.insertBid(bid);
        } else {
            Selectable selectable = html.xpath("//*[@id=\"lab-show\"]/div[2]/div[2]/div/ul");
            page.addTargetRequests(selectable.links().regex("http://www.chinabidding.com/bidDetail/.*").all());
        }

    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 20; i <= 40; i++) {
            //设置POST请求
            Request request = new Request("http://www.chinabidding.com/search/proj.htm");
            //只有POST请求才可以添加附加参数
            request.setMethod(HttpConstant.Method.POST);

            HashMap<String, Object> hashMap = new HashMap<String, Object>();

            hashMap.put("fullText", "电梯");
            hashMap.put("currentPage", i + "");
            hashMap.put("infoClassCodes","0108");

            try {
                request.setRequestBody(HttpRequestBody.form(hashMap, "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            requests.add(request);
        }

        return requests.toArray(new Request[] {});
    }

}