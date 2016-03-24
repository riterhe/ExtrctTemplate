package com.xiaohe.common;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class Test {
	public static void main(String[] args) throws IOException {
		//String res = "1980-04-30";
		LineIterator iterator = FileUtils.lineIterator(new File("/media/riter/data/硕士/实验数据/trainData/filtedData"));
		int count = 0;
		while(iterator.hasNext()){
			String line = iterator.next();
			if (count == 0) {
				FileUtils.writeStringToFile(new File("/media/riter/data/硕士/实验数据/trainData/filtedData2"), line + "\n", "UTF-8", true);
			}
			count++;
			if (count == 4) {
				count = 0;
			}
		}
			
	}
}
