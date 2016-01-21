package com.xiaohe.crawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserHtmlPage {
	private static Logger logger = Logger.getLogger(ParserHtmlPage.class.getName());
	private static String defaultOutPutFile = "resource/outputFile/baike";
	private static DownloadHtmlPage downloadHtmlPage;
	
	
	public ParserHtmlPage() {
		downloadHtmlPage = new DownloadHtmlPage();
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
	
	/**索引
	 * @param jsoup从web中解析出搜擎返回的结果
	 * @return 如果是百科数据，直接解析，其他的就返回文本，去做句法分析
	 */
	public String getElementText(Element result){
		//过滤任何百科，百科直接抽取半结构化数据
		if(result.select("h3").text().contains("百科")){
			//百科数据已经专门爬去，直接退回
			return null;
		}
		if (result.toString().contains("xueshu_links_new")) {//baidu xueshu box
			return null;
		}
		if (!result.select("[tpl=xueshu_detail]").isEmpty()) {//baidu xueshu item
			System.out.println("百度学术，跳过");
			return null;
		}
		result.select("h3").remove();
		result.select(".newTimeFactor_before_abs").remove();
		result.select(".f13").remove();
		result.select(".c-gap-right-small").remove();
		result.select(".op-bk-polysemy-move").remove();
		result.select(".c-recommend").remove();
		result.select(".c").remove();
		return result.text();
	}
	
	/**
	 * @param 下载的网站的本地地址
	 * @param query 由空格分隔开的搜索项
	 * 第一个是 entity name
	 * 第二个是 attributes name
	 * 其余的是 is limit word
	 * @return 从搜索引擎获得10个结果，对每个结果进行解析，如果不是百科数据，就返回去噪音的文本
	 */
	public ArrayList<String> reduceHTML(String inputPath, String query) {
		ArrayList<String> resultList = new ArrayList<String>();
		File input = new File(inputPath);
		logger.debug("begin to resove the webPage");
		try {
			Document doc = Jsoup.parse(input, "utf-8");
			Element content_left = doc.getElementById("content_left");
			if (content_left == null) {
				return resultList;
			}
			Elements resultes = content_left.children();
			int size = resultes.size();
			for (int i = 1; i <= size; i++) {
				Element result = content_left.getElementById(String.valueOf(i));
				String res = null;
				if ((result == null) || ((res = getElementText(result)) == null)) {
					continue;
				}
				//HanLPParser.getNERtags(res);
				String[] sentence = res.split("[.。;；!！？? ]");
				for(String sen : sentence){
					if (checkEntity(sen, query)) {// check the query result
						resultList.add(sen);
					}
				}	
			}
			logger.debug(query + " is resoved successful !");
			return resultList;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(query + " is resoved error !");
			return resultList;
		}
	}

	public void main(String[] args) {
		String path = "/home/riter/Parser";
		String query = "贾焰 出生 国防科学技术大学";
		String filePath = null;
		ParserHtmlPage page = new ParserHtmlPage();
		if((filePath = downloadHtmlPage.dowloadPageWithQuery(path, query)) != null){
			ArrayList<String> resultList = page.reduceHTML(filePath, query);
			for (int i = 0; i < resultList.size(); i++) {
				System.out.println("result : " + i + " : " + resultList.get(i));
			}
		}
	}
}
