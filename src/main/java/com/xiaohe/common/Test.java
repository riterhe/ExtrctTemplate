package com.xiaohe.common;

import com.xiaohe.util.NoiseReduction;

public class Test {
	public static void main(String[] args) {
		String res = " “大陈,小陈,赶紧出来,山东大汉又来看你们啦。 ”当看到史智勤出现在长沙...”老家在武城县,现为中科院院士、国防科学技术大学校长的杨学军对史智勤的...";
		if (res.contains("...")) {
			System.out.println(true);
			res = res.replaceAll("(\\.){3}", ".");
			System.out.println(res);
			String[] sentence = res.split("[.。;；!！？?]");
			for(String sen : sentence){
				System.out.println(sen);
			}
		}else{
			System.out.println(res);
		}
	}
}
