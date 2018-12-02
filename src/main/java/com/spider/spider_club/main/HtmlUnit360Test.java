package com.spider.spider_club.main;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;

public class HtmlUnit360Test {

    public static void main(String[] args) throws Exception {
        String BaseUrl = "https://www.so.com";

        WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);

        webClient.getOptions().setCssEnabled(false); //禁用css
        webClient.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时，是否抛出异常
        //webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);// 当 HTTP 的状态非 200 时是否抛出异常, 这里选择不需要
        webClient.getOptions().setJavaScriptEnabled(false); // 很重要，启用 JS
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 很重要，设置支持 AJAX

        HtmlPage page = webClient.getPage(BaseUrl);

        DomNodeList<DomElement> list = page.getElementsByTagName("form");

        HtmlTextInput input = ((HtmlForm) list.get(0)).getInputByName("q");

        input.setValueAttribute("老北京布鞋");

        DomElement button = page.getElementById("search-button");

        HtmlPage click = button.click();

        parseDoc(click);

        System.out.println("第二页");


        DomElement page1 = click.getElementById("page");

        DomNodeList<HtmlElement> a = page1.getElementsByTagName("a");
        HtmlPage click1 = a.get(0).click();

        // parseDoc(click1);


    }

    public static String getDomainName(String url) throws URISyntaxException {
        String d = getDomainNamePri(url);
        String[] arr = d.split("\\.");
        try {
            d = d.trim();
            if (d.toLowerCase().endsWith(".com.cn") ||
                    d.toLowerCase().endsWith(".net.cn") ||
                    d.toLowerCase().endsWith(".org.cn") ||
                    d.toLowerCase().endsWith(".gov.cn")
            ) {
                //目前常见域名里面,超过1段的,域名加长取全
                return arr[arr.length - 3] + "." + arr[arr.length - 2] + "." + arr[arr.length - 1];
            } else {
                return arr[arr.length - 2] + "." + arr[arr.length - 1];
            }
        } catch (Exception e) {
            return d;
        }

    }

    public static String getDomainNamePri(String url) throws URISyntaxException {
        url = url.trim();
        if (!url.toLowerCase().startsWith("http")) {
            url = "http://" + url;
        }
        url = url.replaceAll("(http\\:\\/\\/.*?\\/).*", "$1");
        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain == null) {
            return url.replace("http://", "").replaceAll("\\/", "");
        }
        try {
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception e) {
            return url.replace("http://", "").replaceAll("\\/", "");
        }
    }

    private static void parseDoc(HtmlPage click) {
        Document document = Jsoup.parse(click.asXml());


        Elements result = document.getElementsByClass("result");
        Elements elements1 = result.get(0).getElementsByClass("res-list");
        for (Element element : elements1) {

            Elements elements = element.getElementsByClass("res-linkinfo");

            try {
                String s = elements.get(0).child(0).ownText();
                System.out.println(s + ">>>>" + getDomainName(s));
            } catch (Exception e) {
                System.out.println("没有链接");
            }


        }
        System.err.println(elements1.size());
    }
}
