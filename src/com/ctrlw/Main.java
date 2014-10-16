package com.ctrlw;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import com.ctrlw.control.FileControl;
import com.ctrlw.control.TimeSeriesControl;
import com.ctrlw.model.CustomImage;
import com.ctrlw.model.WorkRange;


public class Main {

	public static void main(String[] args) {
		mgdi();
		// firePoints();
	}



	private static void firePoints() {
		System.out.println("Begin processing Fire Points...");
		System.out
				.println("************************************************************");
		String rangeFileName = "./res/ranges";
		String saveDir = "E:\\Document\\Study\\bnu\\导师项目\\140506MGDI\\MGDI\\200x200";
		String saveName = "firePoints";
		Functions.createImageOfSeletedPixels(rangeFileName, saveDir, saveName,
				Constants.XMAX - Constants.XMIN + 1, Constants.YMAX
						- Constants.YMIN + 1);
		System.out.println("Task complete.");
	}



	private static void mgdi() {
		String lstDir = "E:\\Document\\Study\\bnu\\导师项目\\140506MGDI\\binLST";
		String eviDir = "E:\\Document\\Study\\bnu\\导师项目\\140506MGDI\\binEVI";
		String saveDir = "E:\\Document\\Study\\bnu\\导师项目\\140506MGDI\\MGDI\\full2";
		String landCoverPath = "E:\\Document\\Study\\bnu\\导师项目\\140614-森林干扰处理\\bin数据\\landcover\\MCD12Q1.A2003001.h25v03.005.2009344204320.hdf";
		System.out.println("Begin processing MGDI...");
		System.out
				.println("************************************************************");
		long startTime = System.currentTimeMillis();
		work(lstDir, eviDir, saveDir, landCoverPath);
		long endTime = System.currentTimeMillis();
		System.out.println("\nTask complete successfully!");
		System.out.println("\nTotal time： " + (endTime - startTime) + "ms");
	}



	private static void work(String lstDir, String eviDir, String saveDir,
			String landCoverPath) {
		HashMap<Integer, LinkedList<File>> yearLSTFiles, yearEVIFiles;
		WorkRange range;
		HashMap<Integer, CustomImage> annualMaxLST, annualMaxEVI, annualDI;
		CustomImage averageMaxLST, averageMaxEVI, landcoverImage;
		int xmin, xmax, ymin, ymax;
		// Extract files for each year, and check whether LST and EVI are of the
		// same time
		yearLSTFiles = FileControl.getYearFileList(lstDir);
		yearEVIFiles = FileControl.getYearFileList(eviDir);
		if (yearLSTFiles.size() != yearEVIFiles.size()) {
			System.out.println("Error: Years are not match.");
			return;
		}

		// Initial process range, and compute annual max data image
		xmin = Constants.XMIN;
		xmax = Constants.XMAX;
		ymin = Constants.YMIN;
		ymax = Constants.YMAX;

		// xmin = 0;
		// xmax = 1199;
		// ymin = 0;
		// ymax = 1199;
		System.out
				.println("The process area include samples "
						+ xmin
						+ " - "
						+ xmax
						+ " and lines "
						+ ymin
						+ " - "
						+ ymax
						+ ". \nNotice: The pixel in the upper left corner of a tile has the coordinates (0, 0).\n");
		range = new WorkRange(xmin, xmax, ymin, ymax);

		System.out.println("Computing annual max LST...");
		annualMaxLST = TimeSeriesControl.extractAnnualMax(yearLSTFiles, range);

		System.out
				.println("Annual max LST complete.\n\nComputing annual max EVI...");
		annualMaxEVI = TimeSeriesControl.extractAnnualMax(yearEVIFiles, range);

		// Compute average values of annual max data

		landcoverImage = new CustomImage(new File(landCoverPath), range);
		System.out
				.println("Annual max EVI complete.\n\nComputing average max LST...");
		averageMaxLST = TimeSeriesControl.computeAverageMax(annualMaxLST,
				landcoverImage);

		System.out
				.println("Average max LST complete.\n\nComputing average max EVI...");
		averageMaxEVI = TimeSeriesControl.computeAverageMax(annualMaxEVI,
				landcoverImage);

		// Compute annual Disturbance Index
		System.out
				.println("Average max EVI complete.\n\nComputing annual MGDI...");
		annualDI = TimeSeriesControl.computeAnnualDI(annualMaxLST,
				annualMaxEVI, averageMaxLST, averageMaxEVI);

		// Output images
		System.out.println("Annual MGDI complete.\n\nExporting MGDI images...");
		FileControl.saveCustomImage(annualDI, saveDir);
		System.out.println("\nMGDI exporting complete.\n");
	}

	private static void test() {
		WorkRange range = new WorkRange(Constants.XMIN, Constants.XMAX,
				Constants.YMIN, Constants.YMAX);
		double[] data = FileControl
				.readDataFromFile(
						new File(
								"E:\\Document\\Study\\bnu\\导师项目\\140506MGDI\\binLST\\MOD11A2.A2001017.h25v03.005.2006354232517.hdf"),
						range);
		for (double d : data) {
			System.out.println(d);
		}
	}
}
