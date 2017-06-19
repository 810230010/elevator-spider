package com.wuliangit.elevator.spider.xcztb;

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
import java.util.regex.Pattern;

import static com.wuliangit.elevator.spider.Common.BID_STATE_ZHAOBIAO;
import static com.wuliangit.elevator.spider.Common.BID_STATE_ZHONGBIAO;

/**
 * @author boom
 * @description 新昌招标
 * @create 2017-06-19 14:52
 **/
public class XcztbBeforeProcessor1 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.xcztb.gov.cn/TPFront/infodetail/.*").match()){
            String title = html.xpath("/html/body/div[2]/div[2]/div/table/tbody/tr[1]/td/font/text()").toString();
            String regExBefore1 = ".*电梯.*";
            Pattern pBefore1 = Pattern.compile(regExBefore1);
            Bid bid = new Bid();
            if(pBefore1.matcher(title).find()){
                bid.setType(BID_STATE_ZHAOBIAO);
            }else{
                System.out.println("不符合采集规则");
                System.out.println(title);
                return;
            }
            bid.setUrl(page.getUrl().toString());
            bid.setPublicTime(html.xpath("/html/body/div[2]/div[2]/div/table/tbody/tr[2]/td/text()").toString().substring(13,22));
            bid.setContent(html.xpath("/html/body/div[2]/div[2]/div/table/tbody/tr[3]/td/table").toString().replaceAll("(\0|\\s*|\r|\n|\t)",""));
            bid.setTitle(title);
            bidService.insertBid(bid);
        }else{
            Selectable selectable = html.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[1]/table/tbody");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://www.xcztb.gov.cn/TPFront/infodetail/.*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 1; i++) {

            Request request = new Request("http://www.xcztb.gov.cn/TPFront/jyxx/001001/001001001/?Paging="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
