package com.wuliangit.elevator.spider.syztb;

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
 * @description 绍兴市上虞区
 * @create 2017-06-22 11:38
 **/
public class SyztbProcessor1 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.syztb.gov.cn/syqztb/InfoDetail/.*").match()){
            String title = html.xpath("/html/body/div[2]/div/div[2]/div/table/tbody/tr[1]/td/font/text()").toString();
            String regExBefore1 = ".*采购公告";
            String regExBefore2 = ".*招标公告";
            String regExAfter1 = ".*成交公告";
            String regExAfter2 = ".*中标公示";
            Pattern pBefore1 = Pattern.compile(regExBefore1);
            Pattern pBefore2 = Pattern.compile(regExBefore2);
            Pattern pAfter1 = Pattern.compile(regExAfter1);
            Pattern pAfter2= Pattern.compile(regExAfter2);
            Bid bid = new Bid();
            System.out.println();
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
            bid.setPublicTime(html.xpath("/html/body/div[2]/div/div[2]/div/table/tbody/tr[2]/td/text()").toString().substring(13,22));
            bid.setContent(html.xpath("/html/body/div[2]/div/div[2]/div/table/tbody/tr[3]/td").toString().replaceAll("(\0|\\s*|\r|\n|\t)",""));
            bid.setTitle(title);
            bidService.insertBid(bid);
        }else{
            Selectable selectable = html.xpath("//*[@id=\"aspnetForm\"]");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://www.syztb.gov.cn/syqztb/InfoDetail/.*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        Request request = new Request("http://www.syztb.gov.cn/syqztb//showinfo/searchresult.aspx?keyword=%E7%94%B5%E6%A2%AF&type=");
        //设置get请求
        request.setMethod(HttpConstant.Method.GET);
        requests.add(request);

        return requests.toArray(new Request[] {});
    }
}
