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
import org.xml.sax.SAXException;

import java.io.IOException;

public class HttpUnitBaiDuTest {


    public static void main(String[] args) throws IOException, SAXException {
        String baseUrl = "http://www.baidu.com";
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);

        webClient.getOptions().setCssEnabled(false); //禁用css
        webClient.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);// 当 HTTP 的状态非 200 时是否抛出异常, 这里选择不需要
        webClient.getOptions().setJavaScriptEnabled(true); // 很重要，启用 JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 很重要，设置支持 AJAX


        HtmlPage htmlpage = webClient.getPage(baseUrl);
        HtmlForm form = htmlpage.getFormByName("f");
        HtmlTextInput textField = form.getInputByName("wd");
        textField.setValueAttribute("老北京布鞋");
        DomElement button = htmlpage.getElementById("su");
        HtmlPage nextPage = button.click();

        webClient.waitForBackgroundJavaScript(3000);// 异步 JS 执行需要耗时, 所以这里线程要阻塞 30 秒, 等待异步 JS 执行结束


        parseDoc(nextPage);

        System.out.println("第二页");

        DomElement page = nextPage.getElementById("page");

        DomNodeList<HtmlElement> domNodeList = page.getElementsByTagName("a");
        HtmlPage click = domNodeList.get(0).click();

        parseDoc(click);


    }

    private static void parseDoc(HtmlPage href) {
        Document document = Jsoup.parse(href.asXml());


        Elements result = document.getElementsByClass("c-container");
        for (Element element : result) {

            Elements elements = element.getElementsByClass("c-showurl");

            System.out.println(elements.get(0).ownText());

        }
        System.err.println(result.size());
    }


}
