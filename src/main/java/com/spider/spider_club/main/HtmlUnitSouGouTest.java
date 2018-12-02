package com.spider.spider_club.main;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUnitSouGouTest {

    public static void main(String[] args) throws Exception {
        String baseUrl = "https://www.sogou.com";


        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);

        webClient.getOptions().setCssEnabled(false); //禁用css
        webClient.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);// 当 HTTP 的状态非 200 时是否抛出异常, 这里选择不需要
        webClient.getOptions().setJavaScriptEnabled(false); // 很重要，启用 JS
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 很重要，设置支持 AJAX

        final HtmlPage htmlpage = webClient.getPage(baseUrl);

        final HtmlForm form = htmlpage.getFormByName("sf");
        // 得到搜索框
        final HtmlTextInput textField = form.getInputByName("query");
        //设置搜索框的value
        textField.setValueAttribute("老北京布鞋");
        DomElement button = htmlpage.getElementById("stb");
        // 设置好之后，模拟点击按钮行为。
        final HtmlPage nextPage = button.click();

        //webClient.waitForBackgroundJavaScript(3000);// 异步 JS 执行需要耗时, 所以这里线程要阻塞 30 秒, 等待异步 JS 执行结束


        parseDoc(nextPage);

        System.out.println("第二页");

        DomElement pagebar_container = nextPage.getElementById("pagebar_container");

        DomNodeList<HtmlElement> a = pagebar_container.getElementsByTagName("a");
        HtmlPage click = a.get(0).click();

        // parseDoc(click);


    }

    private static void parseDoc(HtmlPage click) {
        Document document = Jsoup.parse(click.asXml());


        Elements result = document.getElementsByClass("results");
        Elements elements1 = result.get(0).getElementsByClass("rb");
        for (Element element : elements1) {

            Elements elements = element.getElementsByClass("fb");

            try {
                System.out.println(elements.get(0).child(0).ownText());
            } catch (Exception e) {
                System.out.println("没有链接");
            }


        }
        System.err.println(elements1.size());
    }
}
