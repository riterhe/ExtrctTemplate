package com.xiaohe.common;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Test {
	public static void main(String[] args) throws IOException {
		//String res = "1980-04-30";
		Document document = Jsoup.parse(new File("/home/riter/RE/page/test.html"), "UTF-8");
		String text = document.text();
		System.out.println(text);
		Elements dtList = document.select("dt");
		Elements ddList = document.select("dd");
		StringBuilder sb = new StringBuilder();
		if ((!dtList.isEmpty()) && (!ddList.isEmpty())) {
			for (int i = 0; i < dtList.size(); i++) {
				String property = dtList.get(i).text().replaceAll("[^\u4e00-\u9fa5]", "");
				String value = ddList.get(i).text();
				String[] result = null;
				if (StringUtils.equals(property, "主要成就")) {
					continue;
					//编写比较乱，暂时不处理
				}else{
					if (StringUtils.endsWith(value, "等") && value.length() >1 ) {
						value = StringUtils.removeEnd(value, "等");
					}
					if (value.matches("[^\u4e00-\u9fa5]+")) {
						System.out.println("no chinese");
						result = value.split(",|;|；|、|，");
					}else{
						System.out.println("chinese");
						result = value.split(",|;|；|、|，| ");
					}
				}
				System.out.println(result.length + " : " + Arrays.toString(result));
			}
		}
	}
}
