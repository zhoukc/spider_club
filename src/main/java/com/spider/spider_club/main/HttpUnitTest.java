package com.spider.spider_club.main;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import java.io.IOException;

public class HttpUnitTest {


    public static void main(String[] args) throws IOException, SAXException {

        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);

        webClient.getOptions().setCssEnabled(false); //禁用css
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时，是否抛出异常

        HtmlPage htmlpage = webClient.getPage("https://www.baidu.com");
        // 根据名字得到一个表单，查看上面这个网页的源代码可以发现表单的名字叫“f”
        HtmlForm form = htmlpage.getFormByName("f");
        // 同样道理，获取”百度一下“这个按钮
        HtmlSubmitInput button = form.getInputByValue("百度一下");
        // 得到搜索框
        HtmlTextInput textField = form.getInputByName("wd");
        //设置搜索框的value
        textField.setValueAttribute("老北京布鞋");
        // 设置好之后，模拟点击按钮行为。
        HtmlPage nextPage = button.click();

        //System.err.println(nextPage.asXml());

        Document document = Jsoup.parse(nextPage.asXml());


        Elements result = document.getElementsByClass("c-container");
        for (Element element : result) {

            Elements elements = element.getElementsByClass("c-showurl");

            System.out.println(elements.get(0).ownText());

        }
        System.err.println(result.size());


    }


}
