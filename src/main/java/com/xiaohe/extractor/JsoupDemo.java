package com.xiaohe.extractor;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.xiaohe.common.Baidu;

public class JsoupDemo {
	private static Logger logger = Logger.getLogger(JsoupDemo.class.getName());
	
	public static void main(String[] args) {
		String filePath = "/home/riter/Parser/孝成许皇后_百度百科.html";
		Document doc;
		try {
			doc = Jsoup.parse(new File(filePath), "utf-8");
			TemplateData t = new TemplateData();
			Baidu baidu = new Baidu();
			String properList = baidu.getBox(doc.getElementsByClass("basic-info").first());
			if(properList.isEmpty()){
				return;
			}
		}catch(Exception e){
			logger.error(e);
			logger.error("there is something Wrong  when parse the 好搜百科 !");
		}
	}
}
