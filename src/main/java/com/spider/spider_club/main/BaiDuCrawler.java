package com.spider.spider_club.main;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Crawler(name = "basic")
public class BaiDuCrawler extends BaseSeimiCrawler {


    @Override
    public String[] startUrls() {
        try {
            return new String[]{"https://www.baidu.com/s?wd=" + URLEncoder.encode("老北京布鞋", "UTF-8")};
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start(Response response) {
        JXDocument document = response.document();
        Element element = document.selNOne("//div[@class='c-container']").asElement();

        Elements elements = element.getElementsByClass("c-showurl");

        String s = elements.get(0).ownText();
        System.out.println(s);
    }
}
