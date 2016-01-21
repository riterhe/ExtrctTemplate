package com.xiaohe.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.xiaohe.common.Baidu;
import com.xiaohe.crawler.DownloadHtmlPage;
import com.xiaohe.crawler.PageDownload;
import com.xiaohe.crawler.ParserHtmlPage;
import com.xiaohe.util.FileOperate;
import com.xiaohe.util.NoiseReduction;

public class TemplateExtract {
	//private static String pagePath;
	private static String inputPath;
	private static String outputPath;
	
	private static void exit_with_help(){
		System.out.print(
		 "Usage: java -jar xxx.jar [inputPath] [outputPath]\n"
		);
		System.exit(1);
	}
	private void process() throws Exception{
		FileReader fr = new FileReader(new File(inputPath));
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line = br.readLine()) != null){
			String[] tokens = line.split("###");
			String title = tokens[0];
			if (title.contains("（")) {
				title = title.substring(0, title.indexOf("（"));
			}
			String[] pair = tokens[1].split(":");
			if (pair.length != 2) {
				continue;
			}
			String key = pair[0];
			String value = NoiseReduction.removeAllSymbol(pair[1]);
			if (value.isEmpty()) {
				continue;
			}
			String queryUrl = Baidu.getFuzzyBaiduHttpUrl(title + " " + value);
			ArrayList<String> resultList = PageDownload.getSummary(queryUrl);
			if (!resultList.isEmpty()) {
				System.out.println("extract " + title + " : " + key);
				for(int i=0; i<resultList.size(); i++){
					String result = resultList.get(i);
					//System.out.println(result);
					result = result.replace(title, "#####");
					result = result.replace(value, "*****");
					FileOperate.WriteFile(outputPath + "/" + key, result);
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		TemplateExtract templateExtract = new TemplateExtract();
		if (args.length != 2) {
			exit_with_help();
		}
		//pagePath = args[0];
		inputPath = args[0];
		outputPath = args[1];
		templateExtract.process();
	}
}
