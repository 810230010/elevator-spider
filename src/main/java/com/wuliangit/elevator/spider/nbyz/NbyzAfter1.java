package com.wuliangit.elevator.spider.nbyz;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/23.
 */
public class NbyzAfter1 implements PageProcessor {
    private Site site = Site.me().setSleepTime(1000).setRetryTimes(2);
    private static final String BASE_URL = "http://www.ggjy.nbyz.gov.cn";   //宁波市鄞州区公共资源
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.ggjy.nbyz.gov.cn/TSPB/web/zbgg/Content.jsp.*").match()){
            String title = html.xpath("/html/body/table[2]/tbody/tr[2]/td/table/tbody/tr[1]/td/text()").toString();
            System.out.println(title);
            BidService bidService = SpringUtils.getBean(BidService.class);
            if(!bidService.isExistByTitle(title) && Common.hasZhongBiaoKeyword(title)){
                String all = html.xpath("/html/body/table[2]/tbody/tr[2]/td/table/tbody/tr[3]/td[2]/span/text()").toString();
                String public_time = all.substring(6, all.length() - 1);
                String content = html.xpath("/html/body/table[2]/tbody/tr[2]/td/table/tbody/tr[4]/td/div").toString();
                Bid bid = new Bid();
                bid.setPublicTime(public_time);
                bid.setTitle(title);
                bid.setContent(content);
                bid.setType(Common.BID_STATE_ZHONGBIAO);
                bid.setUrl(page.getUrl().toString());
                bidService.insertBid(bid);
            }


        }else{
            Selectable selectable = html.xpath("/html/body/table[2]/tbody/tr[2]/td/table/tbody");
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

    public static  Request getRequest() {
            //设置POST请求
            Request request = new Request("http://www.ggjy.nbyz.gov.cn/TSPB/pub_news/Pub_News_InfoViewBeanCTRL");
            //只有POST请求才可以添加附加参数
            request.setMethod(HttpConstant.Method.POST);

            Map<String, Object> hashMap = new HashMap<String, Object>();

            hashMap.put("title", "电梯");
            try {
                request.setRequestBody(HttpRequestBody.form(hashMap, "GBK"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }



        return request;
    }
}
