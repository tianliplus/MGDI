package com.ctrlw.model;

public class WorkRange {
	private int mXMin, mXMax, mYMin, mYMax;
	
	public WorkRange(int xmin, int xmax, int ymin, int ymax) {
		mXMin = xmin;
		mXMax = xmax;
		mYMin = ymin;
		mYMax = ymax;
	}

	public int getHeight() {
		return mYMax - mYMin + 1;
	}

	public int getWidth() {
		return mXMax - mXMin + 1;
	}

	public int getArea() {
		return (mXMax - mXMin + 1) * (mYMax - mYMin + 1);
	}

	public int getXMin() {
		return mXMin;
	}

	public void setXMin(int mXMin) {
		this.mXMin = mXMin;
	}

	public int getXMax() {
		return mXMax;
	}

	public void setXMax(int mXMax) {
		this.mXMax = mXMax;
	}

	public int getYMin() {
		return mYMin;
	}

	public void setYMin(int mYMin) {
		this.mYMin = mYMin;
	}

	public int getYMax() {
		return mYMax;
	}

	public void setYMax(int mYMax) {
		this.mYMax = mYMax;
	}


}
