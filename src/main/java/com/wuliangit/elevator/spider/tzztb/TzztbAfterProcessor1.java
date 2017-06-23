package com.wuliangit.elevator.spider.tzztb;

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

import static com.wuliangit.elevator.spider.Common.BID_STATE_ZHONGBIAO;

/**
 * @author boom
 * @description 台州中标
 * @create 2017-06-19 11:54
 **/
public class TzztbAfterProcessor1 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);
        Html html = page.getHtml();
        if(page.getUrl().regex("http://www.tzztb.com/tzcms/gcjyzbjg/index_.*").match()){
            Selectable selectable = html.xpath("/html/body/div[2]/div[3]/div/div/div[2]/div[2]");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://www.tzztb.com/tzcms/gcjyzbjg.*").all());
        }else{
            String title = html.xpath("/html/body/div[2]/div[3]/div/div[1]/div/div[2]/font/text()").toString();
            String regExBefore1 = ".*电梯.*";
            Pattern pBefore1 = Pattern.compile(regExBefore1);
            Bid bid = new Bid();
            if(pBefore1.matcher(title).find()){
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
//            bid.setPublicTime(html.xpath("/html/body/div[2]/div[3]/div/div[1]/div/div[2]/em/text()").toString().substring(5,16));
            bid.setContent(html.xpath("/html/body/div[2]/div[3]/div/div[1]/div/div[2]/div").toString().replaceAll("(\0|\\s*|\r|\n|\t)",""));
            bid.setTitle(title);
            bidService.insertBid(bid);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 25; i++) {

            String url = "http://www.tzztb.com/tzcms/gcjyzbjg/index_" + i + ".htm";
            Request request = new Request(url);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
