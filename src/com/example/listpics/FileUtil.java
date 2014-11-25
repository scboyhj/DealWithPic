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
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileUtil {
	Context context;
	String CACHED_NAME = "my_cached";
	String FILE_TAG = "FileUtil";
	int SD_CACHED_MAX_SIZE = 15;// ������󻺴��СΪ5M
	int TIME_OFF = 24 * 60 * 60;

	public FileUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		File cachedFileDir = new File(context.getFilesDir() + File.separator
				+ CACHED_NAME);
		if (!cachedFileDir.exists())
			cachedFileDir.mkdirs();
	}

	public String getNameFromUrl(String st) {

		return st.substring(st.lastIndexOf("/") + 1, st.length());

	}

	public Bitmap getBitmapFromCached(String url) {
		Bitmap bitmap = null;
		url = getNameFromUrl(url);
		File dirfile = new File(context.getFilesDir() + File.separator
				+ CACHED_NAME);
		File file = new File(dirfile, url);
		if (file.exists()) {
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			upDateLastTime(url);
			return bitmap;
		}
		return bitmap;
	}

	/**
	 * ����ͼƬ��SDcard
	 * 
	 * @param bitmap
	 *            �����ͼƬ
	 * @param url
	 *            ͼƬ�ĵ�ַ����ΪΨһ��ʶ
	 */
	public void saveBitmapToSD(Bitmap bitmap, String url) {
		url = getNameFromUrl(url);
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
		// File cachedFileDir = context.getDir(CACHED_NAME,
		// Context.MODE_PRIVATE);
		File cachedFileDir = new File(context.getFilesDir() + File.separator
				+ CACHED_NAME);

		File cachedFile = new File(cachedFileDir, fileName);
		String st = cachedFile.getAbsolutePath();
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
	 * �ж��Ƿ���SD
	 */
	public boolean hasSD() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * �����ֻ�SD�ռ�
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
	 * �޸��ļ������ʱ��
	 */
	public void upDateLastTime(String url) {
		url = getNameFromUrl(url);
		File dirfile = new File(context.getFilesDir() + File.separator
				+ CACHED_NAME);
		File file = new File(dirfile, url);
		long time = System.currentTimeMillis();
		file.setLastModified(time);
	}

	/**
	 * �����Ż��������泬������Ĵ�Сʱ��sdʣ��С�ڹ涨��ʱ�򣬸���ʱ������ɾ��50%
	 */
	public void upDateCached() {
		File file = new File(context.getFilesDir() + File.separator
				+ CACHED_NAME);
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
	 * ������ڵĻ���
	 * 
	 * @param dir
	 * @param name
	 */
	public void removeExpiredCached() {

		File file = new File(context.getFilesDir() + File.separator
				+ CACHED_NAME);

		File files[] = file.listFiles();
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				if ((System.currentTimeMillis() - files[i].lastModified()) > TIME_OFF) {
					files[i].delete();
				}
			}

	}

	/**
	 * 
	 * ���ڱȽϴ�С����
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
