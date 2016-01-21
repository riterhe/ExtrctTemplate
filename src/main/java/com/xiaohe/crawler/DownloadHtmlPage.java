package com.xiaohe.crawler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.xiaohe.util.NoiseReduction;

/**
 * @author riter
 * 下载网页相关
 */
public class DownloadHtmlPage {
	private static String baiduUrl = "http://www.baidu.com/";
	private static String googleUrl = "https://www.google.com.hk/";
	private static String path = "/home/riter/Parser";
	private static Logger logger = Logger.getLogger(DownloadHtmlPage.class.getName());
	/**
	 * @param query
	 * @return 按照百度搜索的格式把组合查询语句：空格都换成了"%20"，前缀等，
	 */
	public String getBaiduHttpUrl(String query){
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
	/**
	 * @param query
	 * @return 按照谷歌搜索的格式把组合查询语句：设定前缀等，
	 */
	public String getGoogleHttpUrl(String query){
		StringBuilder sb = new StringBuilder();
		String[] params = query.split(" ");
		for(int i=0; i< params.length; i++){
			if(i != params.length-1){
				sb.append("\"" + params[i] + "\"" + "+");
			}else{
				sb.append("\"" + params[i] + "\"");
			}
		}
		String httpArg = "search?q=" + sb.toString();
		String httpUrl = googleUrl + httpArg;
		return httpUrl;
	}
	/**
	 * @param httpUrl
	 * @return 返回下载到的网页的本地存储地址
	 */
	public String dowloadPageWithUrl(String httpUrl){
		logger.debug("begin to download the page");
		String filePath = path + "/" + NoiseReduction.removePunctuation(httpUrl, "_") + ".html";
		File outputfile = new File(filePath);
		URL url;
		InputStream is = null;
		DataOutputStream out = null;
		try {
			url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			is = connection.getInputStream();
			out = new DataOutputStream(new FileOutputStream(outputfile));
			byte[] buffer = new byte[1024 *40];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				out.write(buffer,0,len);
				out.flush();
			}
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("download is wrong !");
			return null;
		}
		logger.debug("page is download succeed !");	
		return filePath;
	}
	/**
	 * @param path: 下载到本地的网页的地址
	 * @param query: 查询语句，使用空格隔开，第一个是实体名，第二个是属性名，第三个以后是限定词
	 * @return filePath
	 * @throws IOException 
	 */
	public String dowloadPageWithQuery(String filepath, String query){
		String httpUrl = getBaiduHttpUrl(query);
		//String httpUrl = getGoogleHttpUrl(query);
		path = filepath;
		return dowloadPageWithUrl(httpUrl);
	}
	
	public static void main(String[] args) {
		String path = "/home/riter/Parser";
		String query = "贾焰 出生 国防科学技术大学";
		String filePath = null;
		DownloadHtmlPage downloadHtmlPage = new DownloadHtmlPage();
		if((filePath = downloadHtmlPage.dowloadPageWithQuery(path, query)) != null){
			File directory = new File(filePath);
			if(directory.exists() && directory.isDirectory()){
				File[] fileList = directory.listFiles();
				for(int i=0; i<fileList.length; i++){
					if(fileList[i].getName().equals((query + ".html").replaceAll(" ", "_"))){
						logger.info(query + " is download successful");
						break;
					}
				}
			}
		}
	}
}
