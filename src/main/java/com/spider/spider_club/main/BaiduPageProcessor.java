package com.spider.spider_club.main;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.net.URLEncoder;

public class BaiduPageProcessor implements PageProcessor {


    private Site site = Site.me().setRetryTimes(3).setSleepTime(5000);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document document = html.getDocument();

        Elements elements = document.getElementsByClass("c-container");
        Elements elements1 = elements.get(0).getElementsByClass("c-showurl");
        for (Element element : elements1) {
            String s = element.ownText();
            System.out.println(s);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) throws Exception {


        Spider.create(new BaiduPageProcessor()).addUrl("https://www.baidu.com/s?wd=" + URLEncoder.encode("老北京布鞋", "UTF-8")).run();


    }
}
