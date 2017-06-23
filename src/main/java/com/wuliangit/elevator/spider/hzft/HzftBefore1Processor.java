package com.wuliangit.elevator.spider.hzft;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.wuliangit.elevator.spider.Common.BID_STATE_ZHAOBIAO;

/**
 * Created by Administrator on 2017/6/15.
 */
//投标
public class HzftBefore1Processor implements PageProcessor{

    private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);
        Html html = page.getHtml();
        if (page.getUrl().regex("http://cg.hzft.gov.cn:80/www/noticedetail.do.*").match()) {
            String title = html.xpath("/html/body/div[2]/div/div[2]/div/h1/text()").toString();
            System.out.println(title);
            String sidTemp = html.xpath("/html/body/div[2]/div/div[2]/div/div[2]/div[1]/div[1]/p[2]/text()").toString();
            if (StringUtils.isEmpty(sidTemp)) {
                sidTemp = html.xpath("/html/body/div[2]/div/div[2]/div/div[2]/p[3]/text()").toString();
            }

            String sid = sidTemp.substring(sidTemp.indexOf("：")+1, sidTemp.length());
            if (!StringUtils.isEmpty(sid)){
                System.out.println(sid);
            }

            //存在了跳出
            if (bidService.isExistBySid(sid)) {
                return;
            }

            Bid bid = new Bid();
            bid.setUrl(page.getUrl().toString());
//            bid.setPublicTime(html.xpath("/html/body/div[2]/div/div[2]/div/div[1]/span[2]/em/text()").toString().substring(0, 10));
            bid.setContent(html.xpath("/html/body/div[2]/div/div[2]/div/div[2]").toString().replaceAll("(\0|\\s*|\r|\n|\t)",""));
            bid.setType(BID_STATE_ZHAOBIAO);
            bid.setTitle(title);
            bid.setSid(sid);

            bidService.insertBid(bid);

        } else {
            Selectable selectable = html.xpath("/html/body/div[2]/div/table/tbody/tr[1]/td[2]/div/ul");
            Selectable links = selectable.links();
            page.addTargetRequests(links.regex("http://cg.hzft.gov.cn:80/www/noticedetail.do.*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


    public static  Request[] getRequest() {
        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 9; i++) {
            //设置POST请求
            Request request = new Request("http://cg.hzft.gov.cn/www/noticelist.do");
            //只有POST请求才可以添加附加参数
            request.setMethod(HttpConstant.Method.POST);

            HashMap<String, Object> hashMap = new HashMap<String, Object>();

            hashMap.put("parameters['regionguid']", "");
            hashMap.put("page.pageNum", i + "");
            hashMap.put("parameters['noticetype']", "3");
            hashMap.put("parameters['title']", "电梯");

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
