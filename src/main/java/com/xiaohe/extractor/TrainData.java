package com.xiaohe.extractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.xiaohe.common.Baidu;
import net.sf.json.JSONArray;

public class TrainData {
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
		List<String> lines = FileUtils.readLines(new File(inputPath), "UTF-8");
		Baidu baidu = new Baidu();
		for (int i = 0; i < lines.size(); i++) {
			String[] tokens = lines.get(i).split("\t");//title, property, value
			if (tokens.length != 3) {
				continue;
			}
			ArrayList<String> resultList = baidu.getSummary(tokens[0] + " " + tokens[2]);
			if (resultList != null && !resultList.isEmpty()) {
				System.out.println("extract " + tokens[0] + " : " + tokens[2]);
				for(int j=0; j<resultList.size(); j++){
					String result = resultList.get(j);
					JSONArray dataArray = new JSONArray();
					dataArray.add(0, tokens[0]);
					dataArray.add(1, tokens[1]);
					dataArray.add(2, tokens[2]);
					dataArray.add(3, result);
					FileUtils.writeStringToFile(new File(outputPath), dataArray.toString()+"\n", "UTF-8", true);
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		TrainData trainData = new TrainData();
		if (args.length != 2) {
			exit_with_help();
		}
		inputPath = args[0];
		outputPath = args[1];
		trainData.process();
	}
}
