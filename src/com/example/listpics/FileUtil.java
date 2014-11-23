package com.example.listpics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileUtil {
	Context context;
	String CACHED_NAME = "my_cached";
	String FILE_TAG = "FileUtil";
	int SD_CACHED_MAX_SIZE = 5 * 1024 * 1024;// 定义最大缓存大小为5M
	int TIME_OFF = 24 * 60 * 60;

	public FileUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	/**
	 * 保存图片到SDcard
	 * 
	 * @param bitmap
	 *            保存的图片
	 * @param url
	 *            图片的地址，作为唯一标识
	 */
	public void saveBitmapToSD(Bitmap bitmap, String url) {
		if (bitmap == null) {
			Log.i(FILE_TAG, "bitmap can't be null");
			return;
		}
		if (!hasSD()) {
			Log.i(FILE_TAG, "SD is null");
			return;
		}
		if (SD_CACHED_MAX_SIZE > freeSpaceOnSD()) {
			Log.i(FILE_TAG, "SD is full");
		}
		String fileName = url;
		// String fileDir = context.getFilesDir().getPath()+CACHED_NAME;
		// File file=new File(fileDir);
		File cachedFileDir = context.getDir(CACHED_NAME, Context.MODE_PRIVATE);
		File cachedFile = new File(cachedFileDir, fileName);

		try {
			cachedFile.createNewFile();
			OutputStream outputStream = new FileOutputStream(cachedFile);
			bitmap.compress(CompressFormat.PNG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
			Log.i(FILE_TAG, "Bimap is saved");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 判断是否有SD
	 */
	public boolean hasSD() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 计算手机SD空间
	 * 
	 * @return
	 */
	public int freeSpaceOnSD() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) statFs.getAvailableBlocks() * statFs
				.getBlockSize()) / (1024 * 1024);
		return (int) sdFreeMB;

	}

	/**
	 * 修改文件的最后时间
	 */
	public void upDateLastTime(String url) {
		File dirfile = new File(context.getDir(CACHED_NAME,
				context.MODE_PRIVATE).getAbsolutePath());
		File file = new File(dirfile, url);
		long time = System.currentTimeMillis();
		file.setLastModified(time);
	}

	/**
	 * 缓存优化，当缓存超过定义的大小时或sd剩余小于规定的时候，根据时间排序删除50%
	 */
	public void upDateCached() {
		File file = new File(context.getDir(CACHED_NAME, context.MODE_PRIVATE)
				.getAbsolutePath());
		File[] files = file.listFiles();
		int size = 0;
		for (int i = 0; i < files.length; i++) {
			size += files[i].length();
		}

		if (size / (1024 * 1024) > SD_CACHED_MAX_SIZE) {
			int deleteSize = (int) (files.length * 0.5 + 1);
			Arrays.sort(files, new FileLastTimeCompare());
			for (int i = 0; i < deleteSize; i++) {
				files[i].delete();
			}
		}

	}

	/**
	 * 清理过期的缓存
	 * 
	 * @param dir
	 * @param name
	 */
	public void removeExpiredCached() {

		File file = new File(context.getDir(CACHED_NAME, context.MODE_PRIVATE)
				.getAbsolutePath());
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if ((System.currentTimeMillis() - files[i].lastModified()) > TIME_OFF) {
				files[i].delete();
			}
		}

	}

	/**
	 * 
	 * 用于比较大小的类
	 * 
	 */
	class FileLastTimeCompare implements Comparator<File> {

		@Override
		public int compare(File lhs, File rhs) {
			// TODO Auto-generated method stub
			if (lhs.lastModified() > rhs.lastModified()) {
				return 1;
			} else if (lhs.lastModified() < rhs.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}
