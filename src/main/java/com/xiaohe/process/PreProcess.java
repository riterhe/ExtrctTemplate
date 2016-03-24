package com.xiaohe.process;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import net.sf.json.JSONArray;

public class PreProcess implements FileProcess{
	private static String INPUT_PATH = "";
	private static String OUTPUT_PATH = "";
	private static HashMap<String, String> PROPERTIES_MAP;
	
	private static void exit_with_help(){
		System.out.print(
		 "Usage: java -jar xxx inputfileFolder outputfile\n"
		);
		System.exit(1);
	}
	
	public void process(File inputFile, ElementProcessor processor) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param inputPath 文件的输入路径
	 * @param outputPath 文件的输出路径
	 * @param LineProcessor 行处理器
	 */
	public void process(File inputFile, File outputFile, LineProcessor processor) {
		LineIterator lineIterator;
		try {
			lineIterator = FileUtils.lineIterator(inputFile, "UTF-8");
			while(lineIterator.hasNext()){
				String line = lineIterator.next();
				processor.lineFilter(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getTrainLine(String line){
		JSONArray array = JSONArray.fromObject(line);
		StringBuilder sBuilder = new StringBuilder();
		if (array.size() != 4) {
			return null;
		}
		String property = array.getString(1);
		String value = array.getString(2);
		String content = array.getString(3);
		content = content.replaceAll("[^\u4e00-\u9fa5]", "");
		if (PROPERTIES_MAP.containsKey(property)) {
			if (property.equals("职位") || property.equals("工作地点") || property.equals("职业")) {
				List<Term> termList = NLPTokenizer.segment(value);
				boolean isNT = false;
				for(int i=0; i< termList.size(); i++){
					if (termList.get(i).nature.startsWith("nt")) {
						isNT = true;
						break;
					}
				}
				if (!isNT) {
/*					List<Term> list = HanLP.segment(content);
					sBuilder.append(PROPERTIES_MAP.get(property)).append("\t").append(termList2String(list));
					return sBuilder.toString();*/
					return null;
				}
			}
			List<Term> list = HanLP.segment(content);
			sBuilder.append(PROPERTIES_MAP.get(property)).append("\t").append(termList2String(list));
			return sBuilder.toString();
		}
		return null;
	}
	
	public static String termList2String(List<Term> list) {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sBuilder.append(list.get(i).word).append(" ");
		}
		return sBuilder.toString().trim();
	}
	public void config() {
		PROPERTIES_MAP = new HashMap<String, String>();
		PROPERTIES_MAP.put("出生地", "出生地");
		PROPERTIES_MAP.put("出生地点", "出生地");
		PROPERTIES_MAP.put("出生地址", "出生地");
		PROPERTIES_MAP.put("出生地区", "出生地");
		PROPERTIES_MAP.put("出生城市", "出生地");
		
		/*PROPERTIES_MAP.put("出生日期", "出生日期");
		PROPERTIES_MAP.put("出生时间", "出生日期");
		PROPERTIES_MAP.put("出生年月", "出生日期");
		PROPERTIES_MAP.put("出生年份", "出生日期");
		PROPERTIES_MAP.put("出生日", "出生日期");
		PROPERTIES_MAP.put("出生年", "出生日期");
		PROPERTIES_MAP.put("出生年代", "出生日期");
		PROPERTIES_MAP.put("出生年月日", "出生日期");
		PROPERTIES_MAP.put("出生时期", "出生日期");*/
		
		PROPERTIES_MAP.put("所属运动队", "所属机构");
		PROPERTIES_MAP.put("经纪公司", "所属机构");
		PROPERTIES_MAP.put("供职单位", "所属机构");
		PROPERTIES_MAP.put("职位", "所属机构");
		PROPERTIES_MAP.put("工作地点", "所属机构");
		PROPERTIES_MAP.put("职业", "所属机构");
		PROPERTIES_MAP.put("工作单位", "所属机构");
		PROPERTIES_MAP.put("所属机构", "所属机构");
		
		PROPERTIES_MAP.put("毕业院校", "毕业院校");
		PROPERTIES_MAP.put("毕业学校", "毕业院校");
		PROPERTIES_MAP.put("毕业于", "毕业院校");
		
/*		PROPERTIES_MAP.put("逝世日期", "逝世日期");
		PROPERTIES_MAP.put("逝世时间", "逝世日期");
		PROPERTIES_MAP.put("逝世", "逝世日期");
		PROPERTIES_MAP.put("去世时间", "逝世日期");*/
		
		PROPERTIES_MAP.put("民族", "民族");
		PROPERTIES_MAP.put("性别", "性别");
	}
	
	public static void main(String[] args) {
		PreProcess preProcess = new PreProcess();
		//preProcess.config();
		File inputFile = new File("/media/riter/data/硕士/实验数据/trainData/filtedDataHanlP4dl");
		File outputFile = new File("/media/riter/data/硕士/实验数据/trainData/filtedDataHanlP4dl1");
		LineIterator lineIterator;
		try {
			lineIterator = FileUtils.lineIterator(inputFile, "UTF-8");
			while(lineIterator.hasNext()){
				String line = lineIterator.next();
				String[] tokens = line.split("\t");
				int label = Integer.parseInt(tokens[0])-1;
				/*if (!PROPERTIES_MAP.containsKey(tokens[0])) {
					continue;
				}*/
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append(label).append("\t").append(tokens[1]);
				
				/*String[] list = tokens[1].split(" ");
				for (int i = 0; i < list.length; i++) {
					String tmp = list[i].replaceAll("[^\u4e00-\u9fa5]", "");
					if (tmp.isEmpty()) {
						continue;
					}else {
						sBuilder.append(tmp).append(" ");
					}
				}*/
				FileUtils.writeStringToFile(outputFile, sBuilder.toString()+"\n", "UTF-8", true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*if (args.length != 2) {
			exit_with_help();
		}
		INPUT_PATH = args[0];
		OUTPUT_PATH = args[1];
		PreProcess preProcess = new PreProcess();
		preProcess.config();
		final File inputFile = new File(INPUT_PATH);
		final File outputFile = new File(OUTPUT_PATH);
		preProcess.process(inputFile, outputFile, new LineProcessor() {
			
			public void lineFilter(String line) {
				// TODO Auto-generated method stub
				String result = getTrainLine(line);
				if (result != null) {
					try {
						FileUtils.writeStringToFile(outputFile, result+"\n", "UTF-8", true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});*/
		System.out.println("reslove over");
	}
}
