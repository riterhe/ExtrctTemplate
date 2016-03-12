package com.xiaohe.crawler;

import java.util.ArrayList;

import org.apache.commons.io.output.ThresholdingOutputStream;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaohe.common.Baidu;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class PageDownload implements PageProcessor{
	private static ArrayList<String> resultList;
	private static String query;
	private Baidu baidu = new Baidu();
	private static Logger logger = Logger.getLogger(PageDownload.class.getName());
	
    private Site site = Site.me()//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);//3次重试

	public Site getSite() {
		return site;
	}

	/**
	 * @param 去过噪音的网页摘要信息
	 * @param query
	 * @return 查看content中是不是包含query的信息
	 */
	public boolean checkEntity(String content, String query) {
		String[] params = query.split(" ");
		for (String s : params) {
			if (!content.contains(s))
				return false;
		}
		return true;
	}
	
	public void process(Page page) {
		Document doc = Jsoup.parse(page.getHtml().toString());
		Element content_left = doc.getElementById("content_left");
		if (content_left == null) {
			return;
		}
		Elements resultes = content_left.children();
		int size = resultes.size();
		for (int i = 1; i <= size; i++) {
			Element result = content_left.getElementById(String.valueOf(i));
			String res = null;
			if ((result == null) || ((res = baidu.getElementText(result)) == null)) {
				continue;
			}
			//HanLPParser.getNERtags(res);
			if (res.contains("...")) {
				res = res.replaceAll("(\\.){3}", ".");
			}
			String[] sentence = res.split("[.。;；!！？?]");
			for(String sen : sentence){
				if (checkEntity(sen, query)) {// check the query result
					resultList.add(sen);
				}
			}	
		}
	}
	
	public  ArrayList<String> getSummary(String Query) {
		String queryUrl = baidu.getFuzzyBaiduHttpUrl(Query);
		resultList = new ArrayList<String>();
		query = Query;
    	Spider spider = Spider.create(new PageDownload());
    	spider.addUrl(queryUrl);
    	spider.run();
    	spider.stop();
		return resultList;
	}
}
