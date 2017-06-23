package com.wuliangit.elevator.spider.lhztb;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wuliangit.elevator.spider.Common.BID_STATE_ZHAOBIAO;
import static com.wuliangit.elevator.spider.Common.BID_STATE_ZHONGBIAO;

/**
 * @author boom
 * @description 临海市公共资源管理中心
 * @create 2017-06-17 17:21
 **/
public class LhztbProcessor1 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.lhztb.gov.cn/Bulletin/info.asp.*").match()){
            String title = html.xpath("//*[@id=\"Table2\"]/tbody/tr/td[2]/table/tbody[1]/tr[1]/td/strong/font/text()").toString();
            String regExBefore1 = ".*采购公告";
            String regExBefore2 = ".*招标公告";
            String regExAfter1 = ".*采购成交公告";
            String regExAfter2 = ".*中标公告";
            Pattern pBefore1 = Pattern.compile(regExBefore1);
            Pattern pBefore2 = Pattern.compile(regExBefore2);
            Pattern pAfter1 = Pattern.compile(regExAfter1);
            Pattern pAfter2= Pattern.compile(regExAfter2);
            Bid bid = new Bid();
            if(pBefore1.matcher(title).find() || pBefore2.matcher(title).find()){
                if(bidService.isExistByTitle(title)){
                    return;
                }
                bid.setType(BID_STATE_ZHAOBIAO);
            }else if(pAfter1.matcher(title).find() || pAfter2.matcher(title).find()){
                if(bidService.isExistByTitle(title)){
                    return;
                }
                bid.setType(BID_STATE_ZHONGBIAO);
            }else{
                System.out.println("不符合采集规则");
                System.out.println(title);
                return;
            }
            bid.setUrl(page.getUrl().toString());
//            bid.setPublicTime(html.xpath("//*[@id=\"Table2\"]/tbody/tr/td[2]/table/tbody[1]/tr[2]/td/text()").toString().substring(5,13));
            bid.setContent(html.xpath("//*[@id=\"Zoom\"]/p[2]").toString().replaceAll("(\0|\\s*|\r|\n|\t)",""));
            bid.setTitle(title);
            bidService.insertBid(bid);
        }else{
            Selectable selectable = html.xpath("//*[@id=\"Table2\"]/tbody/tr/td[3]/table[3]/tbody/tr/td/table[2]/tbody");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://www.lhztb.gov.cn/Bulletin/info.asp.*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 4; i++) {

            Request request = new Request("http://www.lhztb.gov.cn/Search/index.asp?SearchKey=%B5%E7%CC%DD&searchtype=&page="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
