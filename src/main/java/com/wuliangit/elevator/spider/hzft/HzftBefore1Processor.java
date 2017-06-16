package com.wuliangit.elevator.spider.hzft;

import com.wuliangit.elevator.service.BidService;
import com.wuliangit.elevator.util.SpringUtils;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/15.
 */
//投标
public class HzftBefore1Processor implements PageProcessor{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private static final String HZFT_BEFORE_URL = "http://cg.hzft.gov.cn/www/noticelist.do";
    @Override
    public void process(Page page) {
        BidService bidService = SpringUtils.getBean(BidService.class);
        Html html = page.getHtml();
        if(page.getUrl().regex("http://cg.hzft.gov.cn/www/noticedetail.do*").match()){
            String title = html.xpath("/html/body/div[2]/div/div[2]/div/div[2]/div[1]/div[1]/p[2]").toString().substring(9);
            System.out.println(title);
            return;
        }else{
                Selectable selectable = html.xpath("/html/body/div[2]/div/table/tbody/tr[1]/td[2]/div/ul");
                Selectable links = selectable.links();
                page.addTargetRequests(selectable.links().regex("http://cg.hzft.gov.cn/www/noticedetail.do*").all());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static Request[] getRequest() {

        ArrayList<Request> requests = new ArrayList<Request>();

        for (int i = 1; i <= getTotalPage(); i++) {
            //设置POST请求
            Request request = new Request("http://cg.hzft.gov.cn/www/noticelist.do");
            //只有POST请求才可以添加附加参数
            request.setMethod(HttpConstant.Method.POST);

            Map<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("page.pageNum", i);
            hashMap.put("parameters['title']", "电梯");
            hashMap.put("parameters['noticetype']",3);
            System.out.println(hashMap);
            try {
                request.setRequestBody(HttpRequestBody.form(hashMap, "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            requests.add(request);
        }


        return requests.toArray(new Request[] {});
    }
    //得到总页数
    public static int getTotalPage(){
        String url = "http://cg.hzft.gov.cn/www/noticelist.do";
        PrintWriter out = null;
        StringBuilder result = new StringBuilder();
        int totalPage = 0;
        try {
            URL targetUrl = new URL(url);
            URLConnection conn = targetUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            String param = "page.pageNum=1&parameters['noticetype']=3&parameters['title']=电梯";
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String html = "";
            while((html = br.readLine()) != null){
                result.append(html).append("\r\n");
            }
            Document doc = Jsoup.parse(result.toString());
            String target = doc.select("span.num").first().ownText();
            totalPage = Integer.parseInt(target.substring(2, target.lastIndexOf("页")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return totalPage;
    }
}
