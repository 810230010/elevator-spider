package com.wuliangit.elevator.spider.cixi;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.spider.Common;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class CixiBefore1 implements PageProcessor {
    private Site site = Site.me().setSleepTime(1000).setRetryTimes(2);
    private static final String BASE_URL = "http://www.blztb.gov.cn/";   //宁波市北仑区公共资源
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        if(page.getUrl().regex("http://ggzy.cixi.gov.cn/art.*").match()){
            String title = html.xpath("//*[@id=\"zw\"]/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/span/text()").toString();
            System.out.println(title);

            BidService bidService = SpringUtils.getBean(BidService.class);
            if(!bidService.isExistByTitle(title) && title.contains(Common.BID_ZHAOBIAO_KEYWORD)){
                String all = html.xpath("//*[@id=\"zw\"]/table/tbody/tr[2]/td/div/table/tbody/tr[5]/td/font/text()").toString();
                String public_time = all.substring(6, 16).replace("年", "/").replace("月", "/");
                String content = html.xpath("//*[@id=\"zoom\"]/table[2]/tbody").toString();
                Bid bid = new Bid();
                bid.setPublicTime(public_time);
                bid.setTitle(title);
                bid.setContent(content);
                bid.setType(Common.BID_STATE_ZHAOBIAO);
                bid.setUrl(page.getUrl().toString());
                bidService.insertBid(bid);
            }


        }else{
            Selectable selectable = html.xpath("//*[@id=\"zw\"]/table/tbody/tr[2]/td/table[1]/tbody");
            Selectable links = selectable.links();
            List<String> requestList = links.all();
            page.addTargetRequests(requestList);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static  Request[] getRequest() {
        List<Request> requests = new LinkedList<Request>();

        for (int i = 1; i <= 18; i++) {
            //设置POST请求
            Request request = new Request("http://ggzy.cixi.gov.cn/module/sitesearch/index.jsp");
            //只有POST请求才可以添加附加参数
            request.setMethod(HttpConstant.Method.POST);

            HashMap<String, Object> hashMap = new HashMap<String, Object>();

            hashMap.put("keyword", "vc_title");
            hashMap.put("keyvalue", "电梯");
            hashMap.put("currpage", i + "");
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
