package com.ctrlw.model;

import java.io.File;
import java.util.LinkedList;

public class YearFileList {
	private File[] mFileList;
	private int mYear;

	public void setFileList(LinkedList<File> linkedList) {
		mFileList = linkedList.toArray(new File[0]);
	}

	public void setYear(int year) {
		mYear = year;
	}
	public int getYear() {
		return mYear;
	}
}
