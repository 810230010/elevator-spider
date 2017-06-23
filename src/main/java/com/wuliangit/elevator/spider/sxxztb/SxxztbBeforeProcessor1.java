package com.wuliangit.elevator.spider.sxxztb;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.wuliangit.elevator.spider.Common.*;

/**
 * @author boom
 * @description 绍兴市柯桥区招标
 * @create 2017-06-22 14:13
 **/
public class SxxztbBeforeProcessor1 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);

        Html html = page.getHtml();

        if (page.getUrl().regex("http://www.sxxztb.gov.cn/Bulletin/info.aspx?.*").match()) {

            String title = html.xpath("//*[@id=\"lblTitle\"]/text()").toString();

            String regExBefore1 = ".*电梯.*";
            Pattern pBefore1 = Pattern.compile(regExBefore1);

            Bid bid = new Bid();
            if (pBefore1.matcher(title).find()) {

                if (bidService.isExistByTitle(title)) {
                    return;
                }
                bid.setType(BID_STATE_ZHAOBIAO);
            }else{
                System.out.println("不符合标准");
                return;
            }
            bid.setUrl(page.getUrl().toString());
//            bid.setPublicTime(html.xpath("//*[@id=\"lblPublishDate\"]/text()").toString().substring(5,14));
            bid.setContent(html.xpath("//*[@id=\"Zoom\"]/table/tbody/tr/td").toString());
            bid.setTitle(title);

            bidService.insertBid(bid);
        } else {
            Selectable selectable = html.xpath("//*[@id=\"form1\"]/table[2]/tbody/tr/td[3]/div/div/table[2]/tbody/tr[2]/td");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://www.sxxztb.gov.cn/Bulletin/info.aspx?.*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static  Request[] getRequest() {
        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 52; i++) {

            Request request = new Request("http://www.sxxztb.gov.cn/Bulletin/viewmore1.aspx?BulletinTypeId=11&frontid=1&pageindex="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
