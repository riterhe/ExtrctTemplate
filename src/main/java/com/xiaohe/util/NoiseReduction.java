package com.xiaohe.util;

public class NoiseReduction {
	
	/**
	 * @param line
	 * @return 去掉所有的标点符号，连续几个空格都换为一个。
	 */
	public static String removePunctuation(String line, String rep) {
		//return line.replaceAll("\\pP|\\pS", rep).replaceAll("\\s{1,}", " ").trim();
		return line.replaceAll("\\pP|\\pS|\\pZ", rep).replaceAll("\\s{1,}", " ").trim();
	}
	public static String removeAllSymbol(String line) {
		return line.replaceAll("\\pP|\\pS|\\pZ", "");
	}
	public static void main(String[] args) {
		//String string="测试<>《》！*(^)$%~!@#$…&%￥—+=、。，；‘’“”：·`文本";
		String string2 = "1963年4月杨学军出生于山东,1983年毕业于南京通信工程学院,此后就读于国防科技大学,1991年获得工学博士学位,随后留学任教,并在1995年晋升为教授。 在国...";
		System.out.println(removeAllSymbol(string2));
	}
}
