package com.spider.spider_club.main;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.Requester;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import okhttp3.Request;
import org.eclipse.jetty.util.UrlEncoded;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

public class WebCollectorTest extends BreadthCrawler {


    // 自定义的请求插件
    // 可以自定义User-Agent和Cookie
    public static class MyRequester extends OkHttpRequester {

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
        String cookie = "name=abcdef";

        // 每次发送请求前都会执行这个方法来构建请求
        @Override
        public Request.Builder createRequestBuilder(CrawlDatum crawlDatum) {
            // 这里使用的是OkHttp中的Request.Builder
            // 可以参考OkHttp的文档来修改请求头
            return super.createRequestBuilder(crawlDatum)
                    .addHeader("User-Agent", userAgent);
            //  .addHeader("Cookie", cookie);
        }

    }


    public WebCollectorTest(String crawlPath, String keyword, int maxPageNum) throws Exception {
        super(crawlPath, true);

        for (int pageNum = 1; pageNum <= maxPageNum; pageNum++) {
            String url = createBaiDuUrl(keyword, pageNum);
            CrawlDatum datum = new CrawlDatum(url)
                    .type("searchEngine")
                    .meta("keyword", keyword)
                    .meta("pageNum", pageNum)
                    .meta("depth", 1);
            addSeed(datum);

            //自定义请求方式
            setRequester(new MyRequester());
            //设置爬虫线程,默认50个线程
            //setThreads(10);
        }


    }



    @Override
    public void visit(Page page, CrawlDatums next) {
        // 如果遇到301或者302，手动跳转（将任务加到next中）
        // 并且复制任务的meta
        if (page.code() == 301 || page.code() == 302) {
            next.addAndReturn(page.location()).meta(page.meta());
            return;
        }

        String keyword = page.meta("keyword");
        int pageNum = page.metaAsInt("pageNum");
        //int depth = page.metaAsInt("depth");


        if (page.matchType("searchEngine")) {
            System.out.println("成功抓取关键词" + keyword + "的第" + pageNum + "页搜索结果");
            Document doc = page.doc();
            Elements result = doc.getElementsByClass("c-container");
            for (int rank = 0; rank < result.size(); rank++) {

                Elements elements = result.get(rank).getElementsByClass("c-showurl");
                Element a = elements.get(0);
                String href = a.attr("href");

//                next.addAndReturn(href)
//                        .type("outlink")
//                        .meta("keyword", keyword)
//                        .meta("pageNum", pageNum)
//                        .meta("rank", rank);
                System.out.println(">>>>>>" + page.url());
                System.out.println(UrlEncoded.decodeString(href));
                System.out.println("第" + pageNum + "页,第" + (rank + 1) + "个" + a.ownText());

            }
            System.err.println(result.size());
        }
//        else if (page.matchType("outlink")) {
//
//            int rank = page.metaAsInt("rank");
//            String referer=page.meta("referer");
//
//            String line = String.format("第%s页第%s个结果:%s(%s字节)\tdepth=%s\treferer=%s",
//                    pageNum, rank + 1, page.doc().title(),page.content().length,"", referer);
//            System.out.println(line);
//
//        }
    }


    public static String createBaiDuUrl(String keyword, int pageNum) throws Exception {
        int first = (pageNum - 1) * 10;
        keyword = URLEncoder.encode(keyword, "utf-8");
        return String.format("http://www.baidu.com/s?wd=%s&pn=%s", keyword, first);
    }

    public static void main(String[] args) throws Exception {

        WebCollectorTest webCollectorTest = new WebCollectorTest("My", "电焊机", 1);
        // OkHttpRequester requester = (OkHttpRequester)webCollectorTest.getRequester();
        webCollectorTest.start(1);

        int threads = webCollectorTest.getThreads();
        System.err.println(threads + ">>>>>>>>>>");

    }
}
