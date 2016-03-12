package com.xiaohe.process;

import java.io.File;

public interface FileProcess {
	public void process(File inputFile, ElementProcessor processor);
}
