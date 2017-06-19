package com.wuliangit.elevator.spider.szztb;

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
 * @description 嵊州市公共资源交易网
 * @create 2017-06-19 16:35
 **/
public class SzztbProcessor1 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);

    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);
        Html html = page.getHtml();
        if(page.getUrl().regex("http://jyzx.szzj.gov.cn/art.*").match()){
            String title = html.xpath("//*[@id=\"main\"]/table[2]/tbody/tr/td/table[1]/tbody/tr[1]/td/text()").toString();
            String regExBefore1 = ".*中 标.*";
            String regExBefore2 = ".*成 交.*";
            String regExBefore3 = ".*成交.*";
            String regExBefore4 = ".*中标.*";
            String regExAfter1 = ".*招标.*";
            String regExAfter2 = ".*采购.*";
            Pattern pBefore1 = Pattern.compile(regExBefore1);
            Pattern pBefore2 = Pattern.compile(regExBefore2);
            Pattern pBefore3 = Pattern.compile(regExBefore3);
            Pattern pBefore4 = Pattern.compile(regExBefore4);
            Pattern pAfter1 = Pattern.compile(regExAfter1);
            Pattern pAfter2= Pattern.compile(regExAfter2);
            Bid bid = new Bid();
            if(pBefore1.matcher(title).find() || pBefore2.matcher(title).find() || pBefore3.matcher(title).find() || pBefore4.matcher(title).find()){
                if(!bidService.isExistByTitle(title.trim())){
                    bid.setType(BID_STATE_ZHONGBIAO);
                    bid.setUrl(page.getUrl().toString());
                    bid.setPublicTime(html.xpath("//*[@id=\"main\"]/table[2]/tbody/tr/td/table[1]/tbody/tr[2]/td/table/tbody/tr/td[1]/text()").toString().substring(5,14));
                    bid.setContent(html.xpath("//*[@id=\"zoom\"]").toString().replaceAll("(\0|\\s*|\r|\n|\t)",""));
                    bid.setTitle(title);
                    bidService.insertBid(bid);
                }else{
                    return;
                }
            }else if(pAfter1.matcher(title).find() || pAfter2.matcher(title).find()){
                if(!bidService.isExistByTitle(title.trim())){
                    bid.setType(BID_STATE_ZHAOBIAO);
                    bid.setUrl(page.getUrl().toString());
                    bid.setPublicTime(html.xpath("//*[@id=\"main\"]/table[2]/tbody/tr/td/table[1]/tbody/tr[2]/td/table/tbody/tr/td[1]/text()").toString().substring(5,14));
                    bid.setContent(html.xpath("//*[@id=\"zoom\"]").toString().replaceAll("(\0|\\s*|\r|\n|\t)",""));
                    bid.setTitle(title);
                    bidService.insertBid(bid);
                }else{
                    return;
                }
            }else{
                System.out.println("不符合采集规则");
                System.out.println(title);
                return;
            }
        }else{
            Selectable selectable = html.xpath("/html/body/table/tbody/tr/td/table[1]");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://jyzx.szzj.gov.cn/art.*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= 2; i++) {

            Request request = new Request("http://www.szztb.gov.cn/module/sitesearch/index.jsp?keyword=vc_title&columnid=0&keyvalue=%E7%94%B5%E6%A2%AF&webid=12&modalunitid=19751&currpage="+i);
            //设置get请求
            request.setMethod(HttpConstant.Method.GET);
            requests.add(request);
        }
        return requests.toArray(new Request[] {});
    }
}
