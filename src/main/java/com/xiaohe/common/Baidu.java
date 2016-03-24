package com.xiaohe.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Baidu {
	private static Logger logger = Logger.getLogger(Baidu.class.getName());
	private static String baiduUrl = "http://www.baidu.com/";
	private static String baikeUrl = "http://baike.baidu.com";
	private static String pattern = "^(http://baike.baidu.com/view/).*\\.htm$|^(http://baike.baidu.com/subview/).*\\.htm$";
	public static String TITLE;
	
	/**
	 * @param query
	 * @return 百度百科的url
	 */
	public String getBaikeItemUrl(String query){
		StringBuilder httpArg = new StringBuilder();
		httpArg.append("/search/word?word=" + query);
		String httpUrl = baikeUrl + httpArg.toString();
		return httpUrl;
	}
	/**
	 * 模糊查找
	 * @param query
	 * @return 按照百度搜索的格式把组合查询语句：空格都换成了"%20"，前缀等，
	 */
	public String getFuzzyBaiduHttpUrl(String query){
		StringBuilder sb = new StringBuilder();
		String[] params = query.split(" ");
		for(int i=0; i< params.length; i++){
			if(i != params.length-1){
				sb.append(params[i] + "%20");
			}else{
				sb.append( params[i]);
			}
		}
		String httpArg = "s?wd=" + sb.toString();
		String httpUrl = baiduUrl + httpArg;
		return httpUrl;
	}
	
	/**
	 * 精确查找
	 * @param query
	 * @return 按照百度搜索的格式把组合查询语句：空格都换成了"%20"，前缀等，
	 */
	public String getPreciseBaiduHttpUrl(String query){
		StringBuilder sb = new StringBuilder();
		String[] params = StringUtils.split(query, " ");
		for(int i=0; i< params.length; i++){
			if(i != params.length-1){
				sb.append("\"" + params[i] + "\"" + "%20");
			}else{
				sb.append("\"" + params[i] + "\"");
			}
		}
		String httpArg = "s?wd=" + sb.toString();
		String httpUrl = baiduUrl + httpArg;
		return httpUrl;
	}
	
	/**
	 * @param doc 一个百科网页的doc
	 * @return	该网页里边所有的百科页面
	 */
	public static List<String> getAllBaikeUrl(Document doc) {
		List<String> list = new ArrayList<String>();
		Elements mainContents = doc.getElementsByClass("main-content");
		if(mainContents.isEmpty()){
			logger.error("there is no content !");
			return null;
		}
		Element mainContent = mainContents.first();
		Elements aHrefs = mainContent.select("a");
		if(aHrefs.isEmpty()){
			logger.error("there is no href !");
			return null;
		}
		Pattern r = Pattern.compile(pattern);
		for(Element href : aHrefs){
			String url = href.attr("href");
			Matcher m = r.matcher(url);
			if (m.matches()) {
				logger.debug(href.text() + " : " + url);
				list.add(url);
			}
		}
		return list;
	}
	
	/**
	 * @param basicInfo 网页中包含inforbox 的 element
	 * @return 返回一个实体的所有属性，用\t分隔
	 */
	public String getBox(Element basicInfo) {
		Elements dtList = basicInfo.select("dt");
		Elements ddList = basicInfo.select("dd");
		StringBuilder sb = new StringBuilder();
		if ((!dtList.isEmpty()) && (!ddList.isEmpty())) {
			for (int i = 0; i < dtList.size(); i++) {
				String property = dtList.get(i).text();
				String value = ddList.get(i).text();
				String result = property + ":" + value + "\t";
				sb.append(result);
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * @param Jsoup加载后的doc
	 * @return 返回一个实体inforbox 的element
	 */
	public Element getBox(Document doc) {
		Elements basicInfos = doc.getElementsByClass("basic-info");
		if (basicInfos.isEmpty()) {
			logger.error("there is no inforbox !");
			return null;
		}
		return basicInfos.first();
	}
	
	/**
	 * @param httpUrl 需要加载的url
	 * @return 返回一个Jsoup加载后的document
	 */
	public Document LoadPageUseJsoup(String httpUrl) {
		Document doc = null;
		boolean flag = true;
		int tryTime = 20;//最多重复20次
		while (flag && tryTime > 0) {
			try {
				doc = Jsoup.connect(httpUrl).timeout(5000).get();
				flag = false;
			} catch (IOException e) {
				System.err.println("连接超时");
				tryTime--;
				continue;
			}
		}
		return doc;
	}
	
	/**索引
	 * @param result 百度搜索得到的一条结果element。
	 * @return 如果是百科数据，跳过，其他的就返回摘要文本
	 */
	public String getElementText(Element result){
		//过滤任何百科，百科直接抽取半结构化数据
		String title = result.select("h3").text();
		if(title.endsWith("百科") || title.endsWith("百度知道")){
			return null;
		}
		if (result.toString().contains("xueshu_links_new")) {//baidu xueshu box
			return null;
		}
		if (!result.select("[tpl=xueshu_detail]").isEmpty()) {//baidu xueshu item
			System.out.println("百度学术，跳过");
			return null;
		}
		if (!result.select("[tpl=exactqa]").isEmpty()) {
			System.out.println("百度百科，跳过");
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
	
	public boolean checkEntity(String content, String query) {
		String[] params = query.split(" ");
		for (String s : params) {
			if (!content.contains(s))
				return false;
		}
		return true;
	}
	
	/**
	 * @param query
	 * @return 搜索结果中包含该query结果摘要
	 */
	public ArrayList<String> getSummary(String query){
		String queryUrl = getPreciseBaiduHttpUrl(query);
		Document doc = LoadPageUseJsoup(queryUrl);
		if (doc == null) {
			return null;
		}
		Element content_left = doc.getElementById("content_left");
		if (content_left == null) {
			return null;
		}
		ArrayList<String> resultList = new ArrayList<String>();
		Elements resultes = content_left.children();
		for (int i = 1; i <= resultes.size(); i++) {
			Element result = content_left.getElementById(String.valueOf(i));
			String res = null;
			if ((result == null) || ((res = getElementText(result)) == null)) {
				continue;
			}
/*			if (res.contains("...")) {
				res = res.replaceAll("(\\.){3}", ".");
			}*/
			res = res.replaceAll("\"", "");
			if (checkEntity(res, query)) {
				resultList.add(res);
			}
		}
		return resultList;
	}
	
	/**
	 * @param Jsoup加载后的doc
	 * @return 返回Tag element
	 */
	public Element getTag(Document doc) {
		Element openTagItem = doc.getElementById("open-tag-item");
		if (openTagItem == null) {
			logger.error("there is no openTagTitle !");
			return null;
		}
		return openTagItem;
	}
}
