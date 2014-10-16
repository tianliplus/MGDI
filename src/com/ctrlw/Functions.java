package com.ctrlw;

import com.ctrlw.control.FileControl;
import com.ctrlw.control.ImageControl;
import com.ctrlw.model.WorkRange;

public class Functions {
	public static void createImageOfSeletedPixels(String rangeFileName,
			String saveDir, String saveName, int width, int height) {
		WorkRange[] ranges = FileControl.getWorkRange(rangeFileName);
		byte[] imageData = createImage(ranges, width, height);
		FileControl.saveENVIFormat(imageData, width, height, saveDir, saveName);
	}

	private static byte[] createImage(WorkRange[] range, int width, int height) {
		int xmin, xmax, ymin, ymax;
		for (WorkRange w : range) {
			w.setXMin(w.getXMin() - Constants.XMIN);
			w.setXMax(w.getXMax() - Constants.XMIN);
			w.setYMin(w.getYMin() - Constants.YMIN);
			w.setYMax(w.getYMax() - Constants.YMIN);
		}
		byte[] data = new byte[width * height];
		for (WorkRange w : range) {
			xmin = w.getXMin();
			xmax = w.getXMax();
			ymin = w.getYMin();
			ymax = w.getYMax();
			for (int i = xmin; i <= xmax; i++) {
				for (int j = ymin; j <= ymax; j++) {
					data[ImageControl.sampleLinetoIndex(i, j, width)] = 1;
				}
			}
		}
		return data;
	}
}
