package com.example.listpics;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class CopyOfMainActivity extends ActionBarActivity {
	ListView listView;
	public final static int IMGAE_MAX_WIDTH = 500;
	public final static int IMAGE_MAX_HEIFHT = 500;
	int CACHED_MEMORY = 15 * 1024 * 1024;
	FileUtil fileUtil;

	public String[] pics = {
			"http://h.hiphotos.baidu.com/image/pic/item/a8014c086e061d95a53d6d3979f40ad162d9cabc.jpg",
			"http://a.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c21dd9bd2a0cf41bd5ad6e393c.jpg",
			"http://c.hiphotos.baidu.com/image/pic/item/e61190ef76c6a7ef1c8e252efffaaf51f3de660a.jpg",
			"http://c.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd42177fa21a38b87d6277ff948.jpg",
			"http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d1613a13169f1deb48f8c546450.jpg",
			"http://g.hiphotos.baidu.com/image/pic/item/a9d3fd1f4134970ade6ee02897cad1c8a7865d9c.jpg",
			"http://imgt6.bdstatic.com/it/u=2,801627169&fm=25&gp=0.jpg",
			"http://f.hiphotos.baidu.com/image/pic/item/0ff41bd5ad6eddc4cc97b9f03bdbb6fd5266334c.jpg",
			"http://c.hiphotos.baidu.com/image/pic/item/bf096b63f6246b6066845b7ce9f81a4c510fa234.jpg",
			"http://b.hiphotos.baidu.com/image/pic/item/a8ec8a13632762d056c418b6a2ec08fa503dc6c5.jpg",
			"http://g.hiphotos.baidu.com/image/w%3D2048/sign=154252fad762853592e0d521a4d776c6/6d81800a19d8bc3e3f272ec2808ba61ea8d3458e.jpg",
			"http://b.hiphotos.baidu.com/image/w%3D2048/sign=41d856d767380cd7e61ea5ed957cac34/a6efce1b9d16fdfa5ef76f89b68f8c5494ee7b3a.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D2048/sign=fc01ad46c88065387beaa313a3e5a144/77c6a7efce1b9d1692e8b269f1deb48f8c54648e.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D2048/sign=27f3b4e89d3df8dca63d8891f929738b/9f510fb30f2442a78e0f47bad343ad4bd11302af.jpg",
			"http://b.hiphotos.baidu.com/image/pic/item/c2cec3fdfc03924557289e018594a4c27d1e251a.jpg",
			"http://e.hiphotos.baidu.com/image/pic/item/d1160924ab18972b988fa7dde4cd7b899e510a6b.jpg",
			"http://d.hiphotos.baidu.com/image/pic/item/8326cffc1e178a823a7f37a4f403738da977e877.jpg",
			"http://e.hiphotos.baidu.com/image/pic/item/d833c895d143ad4bed76ddc980025aafa40f065a.jpg",
			"http://f.hiphotos.baidu.com/image/pic/item/d000baa1cd11728b12d8d623cafcc3cec3fd2c3d.jpg",
			"http://d.hiphotos.baidu.com/image/pic/item/f703738da97739123842cb7ffa198618367ae24c.jpg",
			"http://e.hiphotos.baidu.com/image/pic/item/f7246b600c33874447ce87c7500fd9f9d72aa006.jpg",

	};
	/**
	 * 缓存常用的文件
	 */
	public HashMap<String, Bitmap> hardBitmapCached = new LinkedHashMap<String, Bitmap>(
			10, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(
				java.util.Map.Entry<String, Bitmap> eldest) {
			// TODO Auto-generated method stub
			if (size() > CACHED_MEMORY) {
				softCachedHashMap.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else {
				return false;
			}

		}
	};
	/**
	 * 缓存不常用的文件
	 */
	public HashMap<String, SoftReference<Bitmap>> softCachedHashMap = new HashMap<String, SoftReference<Bitmap>>(
			10);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list);
		MyAdapter adapter = new MyAdapter();
		listView.setAdapter(adapter);
		fileUtil = new FileUtil(getApplicationContext());
		fileUtil.removeExpiredCached();
		fileUtil.upDateCached();
	}

	/**
	 * 从两种缓存中分别获取
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getFromMemoryCached(String url) {
		synchronized (hardBitmapCached) {
			Bitmap bitmap = hardBitmapCached.get(url);
			if (bitmap != null) {
				hardBitmapCached.remove(url);
				hardBitmapCached.put(url, bitmap);
				return bitmap;
			}

		}

		SoftReference<Bitmap> reference = softCachedHashMap.get(url);
		if (reference != null) {
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				return bitmap;
			} else {
				softCachedHashMap.remove(url);
			}
		}

		return null;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pics.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			convertView = getLayoutInflater()
					.inflate(R.layout.image_item, null);

			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.img);

			Bitmap fbitmap = fileUtil.getBitmapFromCached(pics[position]);
			if (fbitmap == null)

			{
				Bitmap bitmap = getFromMemoryCached(pics[position]);
				if (bitmap == null)

				{
					MyTask myTask = new MyTask(pics[position]);
					myTask.execute(imageView);
				} else {
					imageView.setImageBitmap(bitmap);
				}
				return convertView;
			} else {
				imageView.setImageBitmap(fbitmap);
				return convertView;
			}
		}
	}

	class MyTask extends PreReadTask<ImageView, Void, Bitmap> {
		String url;
		ImageView imageView;

		public MyTask(String urlString) {
			// TODO Auto-generated constructor stub
			url = urlString;
		}

		@Override
		protected Bitmap doInBackground(ImageView... params) {
			// TODO Auto-generated method stub
			imageView = params[0];
			Bitmap showBitmap = null;
			URL urlCon;
			try {

				urlCon = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlCon
						.openConnection();
				InputStream inputStream = connection.getInputStream();
				Options options = new Options();

				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(inputStream, null, options);
				int scale = 1;
				while ((options.outWidth / scale) > IMGAE_MAX_WIDTH
						|| (options.outHeight / scale) > IMAGE_MAX_HEIFHT) {
					scale *= 2;
				}
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				HttpURLConnection connection2 = (HttpURLConnection) urlCon
						.openConnection();
				InputStream inputStream2 = connection2.getInputStream();
				showBitmap = BitmapFactory.decodeStream(inputStream2, null,
						options);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return showBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				hardBitmapCached.put(url, result);
				fileUtil.saveBitmapToSD(result, url);

				imageView.setImageBitmap(result);
			}
		}
	}
}
