package com.ctrlw.control;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.ctrlw.Constants;
import com.ctrlw.model.CustomImage;
import com.ctrlw.model.WorkRange;

public class FileControl {
	public static int getYearCount(String dir) {
		return 0;
	}

	public static WorkRange[] getWorkRange(String fileName) {

		BufferedReader br = null;
		File file = new File(fileName);
		String tempLine;
		String[] rangeText;
		LinkedList<WorkRange> rangeList;
		WorkRange tempRange;

		rangeList = new LinkedList<WorkRange>();
		try {
			br = new BufferedReader(new FileReader(file));
			while ((tempLine = br.readLine()) != null) {
				rangeText = tempLine.split(",");
				if (rangeText.length == 2) {
					tempRange = new WorkRange(Integer.parseInt(rangeText[0]
							.trim()), Integer.parseInt(rangeText[0].trim()),
							Integer.parseInt(rangeText[1].trim()),
							Integer.parseInt(rangeText[1].trim()));
					rangeList.add(tempRange);
				} else if (rangeText.length == 4) {
					tempRange = new WorkRange(Integer.parseInt(rangeText[0]
							.trim()), Integer.parseInt(rangeText[1].trim()),
							Integer.parseInt(rangeText[2].trim()),
							Integer.parseInt(rangeText[3].trim()));
					rangeList.add(tempRange);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rangeList.toArray(new WorkRange[0]);
	}

	public static HashMap<Integer, LinkedList<File>> getYearFileList(String dir) {
		HashMap<Integer, LinkedList<File>> map;
		LinkedList<File> mapFileList;

		File folder = new File(dir);
		File[] files = folder.listFiles();
		map = new HashMap<Integer, LinkedList<File>>();

		for (File f : files) {
			int fYear = getFileYear(f);
			if ((mapFileList = map.get(fYear)) != null) {
				mapFileList.add(f);
			} else {
				LinkedList<File> fileList = new LinkedList<File>();
				fileList.add(f);
				map.put(fYear, fileList);
			}
		}
		// Iterator iter = map.entrySet().iterator();
		//
		// while (iter.hasNext()) {
		// Map.Entry entry = (Map.Entry) iter.next();
		// int mapYear = (Integer) entry.getKey();
		// LinkedList<File> list = (LinkedList<File>) entry.getValue();
		// System.out.println("" + mapYear);
		// System.out.println(list.size());
		// }
		return map;

		// return null;
	}

	private static int getFileYear(File f) {
		String fName = f.getName();
		String sYear = fName.substring(9, 13);
		return Integer.parseInt(sYear);
	}

	public static String[] getFileList(String dir) {
		return null;
	}

	public static void saveCustomImage(HashMap<Integer, CustomImage> annualDI,
			String saveDir) {
		Map.Entry<Integer, CustomImage> entry;
		Iterator iterator;
		int year;
		CustomImage image;

		iterator = annualDI.entrySet().iterator();
		while (iterator.hasNext()) {
			entry = (Entry<Integer, CustomImage>) iterator.next();
			year = entry.getKey();
			image = entry.getValue();
			saveENVIFormat(image.getData(), image.getWidth(),
					image.getHeight(), saveDir, "MGDI" + year);
		}
	}

	public static void saveENVIFormat(double[] data, int width, int height,
			String saveDir, String saveName) {
		DataOutputStream dOutBinStream = null;
		BufferedWriter bw = null;

		byte[] bytes;
		try {
			dOutBinStream = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(saveDir + '\\' + saveName)));
			bytes = doubleArrayToByte(data);
			dOutBinStream.write(bytes);

			bw = new BufferedWriter(new FileWriter(new File(saveDir + '\\'
					+ saveName + ".hdr")));
			bw.write("ENVI\n");
			bw.write("description = {}\n");
			bw.write("samples = " + width + '\n');
			bw.write("lines   = " + height + '\n');
			bw.write("bands   = 1\nheader offset = 0\nfile type = ENVI Standard\ndata type = 5\ninterleave = bsq\nsensor type = Unknown\nbyte order = 0\nwavelength units = Unknown");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dOutBinStream.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveENVIFormat(byte[] data, int width, int height,
			String saveDir, String saveName) {
		DataOutputStream dOutBinStream = null;
		BufferedWriter bw = null;

		try {
			dOutBinStream = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(saveDir + '\\' + saveName)));
			dOutBinStream.write(data);

			bw = new BufferedWriter(new FileWriter(new File(saveDir + '\\'
					+ saveName + ".hdr")));
			bw.write("ENVI\n");
			bw.write("description = {}\n");
			bw.write("samples = " + width + '\n');
			bw.write("lines   = " + height + '\n');
			bw.write("bands   = 1\nheader offset = 0\nfile type = ENVI Standard\ndata type = 1\ninterleave = bsq\nsensor type = Unknown\nbyte order = 0\nwavelength units = Unknown");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dOutBinStream.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] doubleArrayToByte(double[] dArray) {
		byte[] bytes;
		long l;

		bytes = new byte[dArray.length * 8];
		for (int i = 0; i < dArray.length; i++) {
			l = Double.doubleToLongBits(dArray[i]);
			for (int j = 8 * i; j < 8 * i + 8; j++) {
				bytes[j] = (new Long(l)).byteValue();
				l = l >> 8;
			}
		}
		return bytes;
	}

	public static double[] readDataFromFile(File file, WorkRange range) {
		double[] data;
		int xmin, xmax, ymin, ymax;
		int lineStart, lineEnd;
		String[] sData;

		data = new double[range.getWidth() * range.getHeight()];
		xmin = range.getXMin();
		xmax = range.getXMax();
		ymin = range.getYMin();
		ymax = range.getYMax();

		sData = readBinData(file);
		int dataIndex = 0;
		for (int i = ymin; i <= ymax; i++) {
			lineStart = ImageControl
					.sampleLinetoIndex(xmin, i, Constants.WIDTH);
			lineEnd = ImageControl.sampleLinetoIndex(xmax, i, Constants.WIDTH);
			for (int j = lineStart; j <= lineEnd; j++) {
				data[dataIndex++] = Integer.parseInt(sData[j]);
			}
		}
		return data;
	}

	private static String[] readBinData(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			return br.readLine().split(" ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
