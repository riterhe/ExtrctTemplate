package com.xiaohe.process;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public interface ElementProcessor {
	public void resolveInforbox(Element inforbox);
	public void resolveTag(Element tag);
}
