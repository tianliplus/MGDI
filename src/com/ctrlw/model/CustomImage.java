package com.ctrlw.model;

import java.io.File;
import java.util.HashMap;

import com.ctrlw.control.FileControl;

public class CustomImage {
	private int mWidth,mHeight;
	private double[] mData;

	public CustomImage(int width, int height) {
		mWidth = width;
		mHeight = height;
		mData = new double[mWidth * mHeight];
	}

	public CustomImage(WorkRange range) {
		mWidth = range.getWidth();
		mHeight = range.getHeight();
		mData = new double[mWidth * mHeight];
	}

	public CustomImage(File file, WorkRange range) {
		mWidth = range.getWidth();
		mHeight = range.getHeight();
		mData = FileControl.readDataFromFile(file, range);
	}

	public double[] getData() {
		return mData;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public void mathMax(CustomImage anotherImage) {
		for (int i = 0; i < mData.length; i++) {
			mData[i] = mData[i] > anotherImage.mData[i] ? mData[i]
					: anotherImage.mData[i];
		}
		return;
	}

	public static CustomImage computeAverage(CustomImage[] images) {
		if (images == null)
			return null;
		CustomImage averageImage;

		for (int i = 0; i < images.length - 1; i++) {
			if (images[i].mWidth != images[i + 1].mWidth
					|| images[i].mHeight != images[i + 1].mHeight) {
				System.out
						.println("Error: Sizes do not match when computing average customImages");
				return null;
			}
		}
		averageImage = new CustomImage(images[0].mWidth, images[0].mHeight);
		for (CustomImage c : images) {
			averageImage.mathPlus(c);
		}
		averageImage.mathDivide(images.length);
		return averageImage;
	}

	public void averageByLandCover(CustomImage landcover) {
		double[] lcArray = landcover.getData();
		HashMap<Double, RecordLandCover> map = new HashMap<Double, RecordLandCover>();
		for (int i = 0; i < lcArray.length; i++) {
			if(map.containsKey(lcArray[i])){
				map.get(lcArray[i]).count++;
				map.get(lcArray[i]).val += mData[i];
			} else {
				map.put(lcArray[i], new RecordLandCover(1, mData[i]));
			}
		}
		for (RecordLandCover r : map.values()) {
			r.val = r.val / r.count;
		}
		for (int i = 0; i < mData.length; i++) {
			mData[i] = map.get(lcArray[i]).val;
		}
			
	}

	public void mathPlus(CustomImage anotherImage) {
		for (int i = 0; i < mData.length; i++) {
			mData[i] += anotherImage.mData[i];
		}
	}

	public void mathDivide(CustomImage anotherImage) {
		for (int i = 0; i < mData.length; i++) {
			mData[i] /= anotherImage.mData[i];
		}
		return;
	}

	public void mathDivide(int n) {
		for (int i = 0; i < mData.length; i++) {
			mData[i] /= n;
		}
	}
}
