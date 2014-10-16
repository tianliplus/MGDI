package com.ctrlw.control;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.ctrlw.model.CustomImage;
import com.ctrlw.model.WorkRange;

public class TimeSeriesControl {
	public static HashMap<Integer, CustomImage> extractAnnualMax(
			HashMap<Integer, LinkedList<File>> fileMap, WorkRange range) {

		HashMap<Integer, CustomImage> annualMaxMap;
		Map.Entry<Integer, LinkedList<File>> entry;
		int year;
		LinkedList<File> fileList;
		CustomImage customImage;
		// For each map in fileMap, compute the annual max Image
		annualMaxMap = new HashMap<Integer, CustomImage>();
		Iterator iter = fileMap.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Entry<Integer, LinkedList<File>>) iter.next();
			year = entry.getKey();
			fileList = entry.getValue();
			// compute max image from fileList
			System.out.print("Year " + year + "...  ");
			customImage = computeMaxImage(fileList, range);
			annualMaxMap.put(year, customImage);
			System.out.println("Done.");
		}
		return annualMaxMap;
	}

	private static CustomImage computeMaxImage(LinkedList<File> fileList,
			WorkRange range) {
		CustomImage maxImage, fileImage;

		maxImage = new CustomImage(range);
		for (File f : fileList) {
			fileImage = new CustomImage(f, range);
			maxImage.mathMax(fileImage);
		}
		return maxImage;
	}

	public static CustomImage computeAverageMax(
			HashMap<Integer, CustomImage> annualMaxLST,
			CustomImage landCoverImage) {
		CustomImage averageCustomImage = CustomImage
				.computeAverage(annualMaxLST.values().toArray(
						new CustomImage[0]));
		averageCustomImage.averageByLandCover(landCoverImage);
		return averageCustomImage;
	}

	public static HashMap<Integer, CustomImage> computeAnnualDI(
			HashMap<Integer, CustomImage> annualMaxLST,
			HashMap<Integer, CustomImage> annualMaxEVI,
			CustomImage averageMaxLST,
			CustomImage averageMaxEVI) {
		HashMap<Integer, CustomImage> annualDI;
		CustomImage diUp, diDown;
		CustomImage maxLST, maxEVI;
		Map.Entry<Integer, CustomImage> entry;
		Iterator iterator;

		diDown = averageMaxLST;
		diDown.mathDivide(averageMaxEVI);

		annualDI = new HashMap<Integer, CustomImage>();
		iterator = annualMaxLST.entrySet().iterator();
		while (iterator.hasNext()) {
			entry = (Entry<Integer, CustomImage>) iterator.next();
			int year = entry.getKey();
			maxLST = entry.getValue();
			maxEVI = annualMaxEVI.get(year);
			diUp = maxLST;
			diUp.mathDivide(maxEVI);
			diUp.mathDivide(diDown);
			annualDI.put(year, diUp);
		}
		return annualDI;
	}
}
