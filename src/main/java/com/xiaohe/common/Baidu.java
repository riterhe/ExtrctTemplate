package com.xiaohe.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Baidu {
	private static Logger logger = Logger.getLogger(Baidu.class.getName());
	private static String baiduUrl = "http://www.baidu.com/";
	private static String pattern = "^(http://baike.baidu.com/view/).*\\.htm$|^(http://baike.baidu.com/subview/).*\\.htm$";
	
	/**
	 * 模糊查找
	 * @param query
	 * @return 按照百度搜索的格式把组合查询语句：空格都换成了"%20"，前缀等，
	 */
	public static String getFuzzyBaiduHttpUrl(String query){
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
	public static String getPreciseBaiduHttpUrl(String query){
		StringBuilder sb = new StringBuilder();
		String[] params = query.split(" ");
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
	
	/**索引
	 * @param result 百度搜索得到的一条结果摘要。
	 * @return 如果是百科数据，跳过，其他的就返回摘要文本
	 */
	public static String getElementText(Element result){
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
		if (!result.select("[tpl=exactqa]").isEmpty()) {//baidu xueshu item
			System.out.println("百度百科，跳过");
			return null;
		}
		if(result.select("h3").text().contains("百度知道")){
			//知道数据好多是拷贝百科的
			System.out.println("百度知道，跳过");
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
	 * @return 返回一个实体inforbox信息
	 */
	public String getBox(Document doc) {
		Elements basicInfos = doc.getElementsByClass("basic-info");
		if (basicInfos.isEmpty()) {
			logger.error("there is no inforbox !");
			return null;
		}
		return getBox(basicInfos.first());
	}
}
