package com.ctrlw.control;


public class ImageControl {
	// Sample, line start with 0
	public static int sampleLinetoIndex(int sample, int line, int width) {
		return line * width + sample;
	}
}
