package com.xiaohe.common;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;

public class Test {
	public static void main(String[] args) throws IOException {
		//String res = "1980-04-30";
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(0, "title");
		jsonArray.add(1, "property");
		jsonArray.add(2, "value");
		jsonArray.add(2, "line");
		System.out.println(jsonArray.toString());
		String result = jsonArray.toString();
		String teString = "[\"title\",\"property\",\"line\",\"value\"]";
		JSONArray array = JSONArray.fromObject(teString);
		for(int i=0; i<array.size(); i++){
			System.out.println(array.get(i));
		}
		String string = "hah hehe";
		String[] token = StringUtils.split(string, " ");
		System.out.println(Arrays.toString(token));
	}
}
