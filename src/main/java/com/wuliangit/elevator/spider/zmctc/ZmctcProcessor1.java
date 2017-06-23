package com.wuliangit.elevator.spider.zmctc;

import com.wuliangit.elevator.entity.Bid;
import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wuliangit.elevator.spider.Common.BID_STATE_ZHAOBIAO;

public class ZmctcProcessor1 implements PageProcessor {

    // 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

    public Site getSite() {
        return site;
    }

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);

        Html html = page.getHtml();

        if (page.getUrl().regex("http://new.zmctc.com/zjgcjy/infodetail.*").match()) {

            String title = html.xpath("//*[@id=\"tdTitle\"]/font[1]/b/text()").toString();

            System.out.println(title);

            if (title.contains("电梯")) {

                String sid = html.xpath("//*[@id=\"TDContent\"]/div[2]/p[2]/a/span/text()").toString();

                //存在了跳出
                if (bidService.isExistBySid(sid)) {
                    return;
                }

                Bid bid = new Bid();
                bid.setUrl(page.getUrl().toString());
//                bid.setPublicTime(html.xpath("//*[@id=\"tdTitle\"]/font[2]/text()/text()").toString());
                bid.setContent(html.xpath("/html/body/div/table[1]/tbody/tr/td[2]/table/tbody/tr[2]/td/table[2]/tbody/tr/td/table/tbody").toString());
                bid.setType(BID_STATE_ZHAOBIAO);
                bid.setTitle(title);
                bid.setSid(sid);

                bidService.insertBid(bid);
            }
        } else {
            Selectable selectable = html.xpath("//*[@id=\"AreaArticle3\"]/div[2]/div[2]/div");
            Selectable links = selectable.links();
            page.addTargetRequests(selectable.links().regex("http://www.hzctc.cn/ProArticle/.*").all());
        }

    }

    public static Request getRequest() {
        //设置POST请求
        Request request = new Request("http://www.zmctc.com/zjgcjy/Notice/AfficheInfoMore.aspx");
        //只有POST请求才可以添加附加参数
        request.setMethod(HttpConstant.Method.POST);

        request.addCookie("Cookie","ASP.NET_SessionId=aof32a45zskfdl55c1elyt45");

        //设置POST参数
        List<NameValuePair> nvs = new ArrayList<NameValuePair>();
        nvs.add(new BasicNameValuePair("__VIEWSTATE", "dDwtOTQ4MTU2NTI4O3Q8O2w8aTwxPjs+O2w8dDw7bDxpPDE+Oz47bDx0PDtsPGk8MD47PjtsPHQ8O2w8aTwxPjtpPDI+Oz47bDx0PDtsPGk8MD47PjtsPHQ8O2w8aTwwPjs+O2w8dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzO1BhZ2VTaXplOz47bDxpPDE+O2k8Mz47aTwzPjtsPD47aTwzMD47Pj47PjtAMDw7O0AwPHA8bDxWaXNpYmxlOz47bDxvPHQ+Oz4+Ozs7Oz47Pjs7Ozs7Ozs7Oz47bDxpPDA+Oz47bDx0PDtsPGk8Mj47aTwzPjtpPDQ+Oz47bDx0PDtsPGk8MT47aTwyPjs+O2w8dDw7bDxpPDA+Oz47bDx0PEA8XDxhIGhyZWY9Imh0dHA6Ly9uZXcuem1jdGMuY29tL3pqZ2NqeS9pbmZvZGV0YWlsLz9pbmZvaWQ9NmYzZWY0YTctN2NlNS00OGQzLTkxNTQtNTY5MjQ3ZTVkOWMyJmNhdGVnb3J5TnVtPTAwNDAwMTAwMSIgdGFyZ2V0PSJfYmxhbmsiIHRpdGxlPSLmuKnlt57mnLrlnLrmlrDlu7rotKfov5DljLrlj4rnlJ/kuqfovoXliqnorr7mlr3lt6XnqIvnlLXmoq/orr7lpIfkvpvotKfkuI7lronoo4XnlLXmoq/orr7lpIfkvpvotKfkuI7lronoo4VbRTMzMDAwMDAwMDEwMDAyMjYwMTcwMDFd5oub5qCH5YWs5ZGKIlw+5rip5bee5py65Zy65paw5bu66LSn6L+Q5Yy65Y+K55Sf5Lqn6L6F5Yqp6K6+5pa95bel56iL55S15qKv6K6+5aSH5L6b6LSn5LiO5a6J6KOF55S15qKv6K6+5aSH5L6b6LSn5LiO5a6J6KOFW0UzMzAwMDAwMDAxMDAwMjI2MDE3MDAxXeaLm+agh+WFrOWRilw8L2FcPjtcZTtFMzMwMDAwMDAwMTAwMDIyNjAxNzAwMTsuLi9JbWFnZXMvZG90cy9kb3RfbnVsbC5naWY7Pjs7Pjs+Pjt0PDtsPGk8MD47PjtsPHQ8QDwyMDE3LTA0LTA3Oz47Oz47Pj47Pj47dDw7bDxpPDE+O2k8Mj47PjtsPHQ8O2w8aTwwPjs+O2w8dDxAPFw8YSBocmVmPSJodHRwOi8vbmV3LnptY3RjLmNvbS96amdjankvaW5mb2RldGFpbC8/aW5mb2lkPWQzNDE0MWNhLWI4MzEtNGJhNS1hZGFlLTg3MDQ4M2U2NjIyOSZjYXRlZ29yeU51bT0wMDQwMDEwMDEiIHRhcmdldD0iX2JsYW5rIiB0aXRsZT0i5rWZ5rGf55yB5Lq65rCR5Yy76ZmiMuWPt+alvOW7uuiuvuW3peeoi+eUteair+iuvuWkh+S+m+i0p+S4juWuieijheeUteair+iuvuWkh+S+m+i0p+S4juWuieijhVtFMzMwMDAwMDAwMTAwMDUwMjAwMjAwMV3mi5vmoIflhazlkYoiXD7mtZnmsZ/nnIHkurrmsJHljLvpmaIy5Y+35qW85bu66K6+5bel56iL55S15qKv6K6+5aSH5L6b6LSn5LiO5a6J6KOF55S15qKv6K6+5aSH5L6b6LSn5LiO5a6J6KOFW0UzMzAwMDAwMDAxMDAwNTAyMDAyMDAxXeaLm+agh+WFrOWRilw8L2FcPjtcZTtFMzMwMDAwMDAwMTAwMDUwMjAwMjAwMTsuLi9JbWFnZXMvZG90cy9kb3RfbnVsbC5naWY7Pjs7Pjs+Pjt0PDtsPGk8MD47PjtsPHQ8QDwyMDE3LTAxLTI1Oz47Oz47Pj47Pj47dDw7bDxpPDE+O2k8Mj47PjtsPHQ8O2w8aTwwPjs+O2w8dDxAPFw8YSBocmVmPSJodHRwOi8vbmV3LnptY3RjLmNvbS96amdjankvaW5mb2RldGFpbC8/aW5mb2lkPTAxMWRkMzU0LTg4NjItNDM4OC05NzA1LWZiZjU5YzY1ZTU4OSZjYXRlZ29yeU51bT0wMDQwMDEwMDEiIHRhcmdldD0iX2JsYW5rIiB0aXRsZT0i5rWZ5rGf5rC05Yip5rC055S15a2m6Zmi5rC05Yip5pWZ6IKy57u85ZCI5qW86aG555uu55S15qKv55S15qKv6K6+5aSHW0UzMzAwMDAwMDAxMDAwMjYzMDAzMDAxXeaLm+agh+WFrOWRiiJcPua1meaxn+awtOWIqeawtOeUteWtpumZouawtOWIqeaVmeiCsue7vOWQiOalvOmhueebrueUteair+eUteair+iuvuWkh1tFMzMwMDAwMDAwMTAwMDI2MzAwMzAwMV3mi5vmoIflhazlkYpcPC9hXD47XGU7RTMzMDAwMDAwMDEwMDAyNjMwMDMwMDE7Li4vSW1hZ2VzL2RvdHMvZG90X251bGwuZ2lmOz47Oz47Pj47dDw7bDxpPDA+Oz47bDx0PEA8MjAxNi0xMi0yMjs+Ozs+Oz4+Oz4+Oz4+Oz4+Oz4+Oz4+O3Q8O2w8aTwwPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8cDxsPEN1c3RvbUluZm9UZXh0O1JlY29yZGNvdW50O1BhZ2VTaXplOz47bDzlhbHvvJpcPGZvbnQgY29sb3I9InJlZCJcPlw8Ylw+M1w8L2JcPlw8L2ZvbnRcPuadoSDnrKzvvJpcPGZvbnQgY29sb3I9InJlZCJcPlw8Ylw+MS8xXDwvYlw+XDwvZm9udFw+6aG1O2k8Mz47aTwzMD47Pj47Pjs7Pjs+Pjs+Pjs+Pjs+Pjs+Pjs+Pjs+Cq8gec8upN4hJFartoIqWBOQSPY="));
        nvs.add(new BasicNameValuePair("ZBWJInfoList1:KeyWord", "电梯"));

        //转换为键值对数组
        NameValuePair[] values = nvs.toArray(new NameValuePair[]{});

        //将键值对数组添加到map中
        Map<String, Object> params = new HashMap<String, Object>();
        //key必须是：nameValuePair
        params.put("nameValuePair", values);

        //设置request参数
        request.setExtras(params);

        return request;
    }

}