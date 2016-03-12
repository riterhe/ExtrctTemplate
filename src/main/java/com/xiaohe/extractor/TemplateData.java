package com.xiaohe.extractor;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiaohe.common.Baidu;
import com.xiaohe.process.ElementProcessor;
import com.xiaohe.process.FileProcess;

public class TemplateData implements FileProcess{
	private static String OUTPUTPATH;
	
	private static void exit_with_help(){
		System.out.print(
		 "Usage: java -jar xxx inputfileFolder outputfile\n"
		);
		System.exit(1);
	}
	
	public void process(File pagefile, ElementProcessor processor) {
		// TODO Auto-generated method stub
		Baidu baidu = new Baidu();
		String title = pagefile.getName();
		if (title.contains("（")) {
			title = title.substring(0, title.indexOf("（"));
		}
		Baidu.TITLE = title;
		Document doc = null;
		try {
			doc = Jsoup.parse(pagefile, "utf-8");
		} catch (IOException e1) {
			System.err.println("读文件错误");
			return;
		}
		//get inforbox
		Element inforbox = baidu.getBox(doc);
		if (inforbox != null) {
			processor.resolveInforbox(inforbox);
		}
	}
	
	public static void getResult(Element inforbox) {
		Elements dtList = inforbox.select("dt");
		Elements ddList = inforbox.select("dd");
		if ((!dtList.isEmpty()) && (!ddList.isEmpty())) {
			for (int i = 0; i < dtList.size(); i++) {
				String property = dtList.get(i).text().replaceAll("[^\u4e00-\u9fa5]", "");
				String value = ddList.get(i).text().replaceAll("\"", "");
				String[] result = null;
				if (!StringUtils.equals(property, "主要成就")) {//编写比较乱，暂时不处理
					if (StringUtils.endsWith(value, "等") && value.length() >1 ) {
						value = StringUtils.removeEnd(value, "等");
					}
					//if (value.matches("[^\u4e00-\u9fa5]+")) {
					result = value.split(",|;|；|、|，");
					//}
					for (int j = 0; j < result.length; j++) {
						StringBuilder triplesBulider = new StringBuilder();
						triplesBulider.append(Baidu.TITLE).append("\t").append(property).append("\t").append(result[j]);
						triplesBulider.append("\n");
						try {
							FileUtils.writeStringToFile(new File(OUTPUTPATH), triplesBulider.toString(), "UTF-8", true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		TemplateData templateData = new TemplateData();
		if (args.length != 2) {
			exit_with_help();
		}
		String floderPath = args[0];
		OUTPUTPATH = args[1];
		File fileFloder = new File(floderPath);
		if (fileFloder.isDirectory()) {
			File[] pagefile = fileFloder.listFiles();
			for (int i = 0; i < pagefile.length; i++) {
				System.out.println("reslove the " + pagefile[i].getName());
				templateData.process(pagefile[i], new ElementProcessor() {
					public void resolveInforbox(Element inforbox) {
						getResult(inforbox);
					}
				});
			}
		}
/*		templateData.process(new File("/home/riter/RE/page/邓小平（中国共产党第二代领导集体核心人物）"), new ElementProcessor() {
			
			public void resolveInforbox(Element inforbox) {
				getResult(inforbox);				
			}
		});*/
		System.out.println("reslove over");
	}
}
