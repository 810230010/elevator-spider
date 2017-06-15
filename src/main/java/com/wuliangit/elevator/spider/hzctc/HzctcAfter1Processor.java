package com.wuliangit.elevator.spider.hzctc;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class HzctcAfter1Processor implements PageProcessor {

    // 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

    public Site getSite() {
        return site;
    }

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);

        Html html = page.getHtml();

        if(page.getUrl().regex("http://www.hzctc.cn/ProArticle/.*").match()){

            String title = html.xpath("//*[@id=\"ctl00_ContentPlaceHolder1_DB_Title\"]/text()").toString();
            System.out.println(title);

            if (title.contains("电梯")) {

                //存在了跳出
                if (bidService.isExistByTitle(title)) {
                    return;
                }

                Bid bid = new Bid();
                bid.setUrl(page.getUrl().toString());
                bid.setPublicTime(html.xpath("//*[@id=\"ctl00_ContentPlaceHolder1_DB_PublishStartTime\"]/text()").toString());
                bid.setContent(html.xpath("//*[@id=\"PageMain\"]/div[2]").toString().replaceAll("(\0|\\s*|\r|\n)",""));
                bid.setType("ZHONGBIAO");
                bid.setTitle(title);
                bid.setSid("");
                bid.setBeginTime(html.xpath("//*[@id=\"ctl00_ContentPlaceHolder1_DB_HtmlContent\"]/div/table/tbody/tr/td/table[1]/tbody/tr[5]/td[2]/p/span[1]/text()").toString());
                bid.setEndTime(html.xpath("//*[@id=\"ctl00_ContentPlaceHolder1_DB_HtmlContent\"]/div/table/tbody/tr/td/table[1]/tbody/tr[5]/td[4]/p/span[1]/text()").toString());
                bidService.insertBid(bid);
            }
        }else{
            Selectable selectable = html.xpath("//*[@id=\"GridView4\"]/tbody");
            page.addTargetRequests(selectable.links().regex("http://www.hzctc.cn/ProArticle/.*").all());
        }

    }

}