package com.xiaohe.extractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.xiaohe.common.Baidu;
import com.xiaohe.crawler.PageDownload;
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
		Baidu baidu = new Baidu();
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
			ArrayList<String> resultList = baidu.getSummary(title + " " + value);
			if (!resultList.isEmpty()) {
				System.out.println("extract " + title + " : " + key);
				for(int i=0; i<resultList.size(); i++){
					String result = resultList.get(i);
					//System.out.println(result);
					result = result.replace(title, "#####");
					result = result.replace(value, "*****");
					FileUtils.writeStringToFile(new File(outputPath + "/" + key), result, "UTF-8", true);
					//FileOperate.WriteFile(outputPath + "/" + key, result);
				}
			}
		}
		br.close();
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
