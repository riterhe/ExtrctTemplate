package com.xiaohe.process;

import java.io.File;


public interface FileProcess {
	public void process(File inputFile, ElementProcessor processor);
	public void process(File inputFile, File outputFile, LineProcessor processor);
}
