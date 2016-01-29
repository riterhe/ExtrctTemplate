package com.xiaohe.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperate {
	private static String defaultOutPutFile = "resource/outFile";
	/**
	 * @param filePath
	 * @throws Exception
	 * input a filePath then print the file content
	 */
	public static void readFile(String filePath) throws Exception {
		File inputfile = new File(filePath);
		FileReader fileReader = new FileReader(inputfile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String strRead = null;
		while ((strRead = bufferedReader.readLine()) != null) {
			System.out.println(strRead);
		}
		bufferedReader.close();
		fileReader.close();
	}
	public static void WriteFile(String line) throws Exception {
		WriteFile(defaultOutPutFile, line);
	}
	public static void WriteFile(String filePath, String line){
		/*File outputfile = new File(filePath);
		FileWriter fileWriter = new FileWriter(outputfile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(line);
		bufferedWriter.write("\n");
		bufferedWriter.flush();
		bufferedWriter.close();
		fileWriter.close();*/
		File outputfile = new File(filePath);
		FileOutputStream fo = null;
		try{
			fo = new FileOutputStream(outputfile, true);
			byte[] lineByte = line.getBytes("utf-8");
			fo.write(lineByte);
			fo.write("\n".getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		//readFile("resource/fileReadTest");
		WriteFile("outFile", "中文英文test2utf-8");
	}
}
