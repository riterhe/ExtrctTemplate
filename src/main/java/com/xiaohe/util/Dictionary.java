package com.xiaohe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {
	private static String  inputPath;
	private static String outputPath;
	private static String datePattern = "(\\d{1,4}年)(\\d{1,2}月)|(\\d{1,4}年)|(\\d{1,2}月)|(\\d{1,4}年)(\\d{1,2}月)(\\d{1,2}日)";
    
	public static void main(String[] args) throws Exception {
		inputPath = "/home/riter/toolkit/HanLP/data/dictionary/custom/entityDictionary.txt";
		outputPath = "/home/riter/toolkit/HanLP/data/dictionary/custom/entityDictionary-1.txt";
		FileReader fr = new FileReader(new File(inputPath));
		BufferedReader br = new BufferedReader(fr);
		String line = "";
	    Pattern r = Pattern.compile(datePattern);
		while((line = br.readLine()) != null){
			String[] tokens = line.split(" ");
			Matcher m = r.matcher(tokens[0]);
			if (tokens[1].equals("m") && m.find()) {
				continue;
			}
			FileOperate.WriteFile(outputPath, line);
		}
		br.close();
	}
}
