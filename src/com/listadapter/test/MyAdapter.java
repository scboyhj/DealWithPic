package com.listadapter.test;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listpics.R;

public class MyAdapter extends BaseAdapter implements OnScrollListener {
	int max = 200;
	ExecutorService executor = Executors.newCachedThreadPool();

	BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(10);
	@SuppressLint("NewApi")
	ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 120,
			TimeUnit.SECONDS, blockingQueue);
	ArrayList<String> pathList;
	Context context;
	LruCache<String, Bitmap> cache;
	ListView listView;

	public MyAdapter(ArrayList<String> arrayList, Context context,
			ListView listView) {
		// TODO Auto-generated constructor stub
		this.pathList = arrayList;
		this.context = context;
		this.listView = listView;
		long t = Runtime.getRuntime().totalMemory();
		cache = new LruCache<String, Bitmap>((int) (t / 8));

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pathList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pathList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// if (convertView == null) {
		convertView = LayoutInflater.from(context).inflate(R.layout.image_item,
				null);
		// Holder holder = new Holder();
		// holder.imageView = (ImageView)
		// convertView.findViewById(R.id.img);
		// holder.textView = (TextView) convertView.findViewById(R.id.tv);
		// convertView.setTag(holder);
		// }
		// Holder holder = (Holder) convertView.getTag();
		//
		// holder.textView.setText(position + "");
		ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
		TextView textView = (TextView) convertView.findViewById(R.id.tv);
		textView.setText(position + "");
		Bitmap bitmap = cache.get(pathList.get(position));
		imageView.setTag(pathList.get(position));
		// if (bitmap == null) {
		// LoadImage(imageView, pathList.get(position));
		// } else {
		// if (imageView.getTag().equals(pathList.get(position)))
		// imageView.setImageBitmap(bitmap);
		// }
		if (bitmap == null)
			imageView.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_launcher));
		else
			imageView.setImageBitmap(bitmap);
		return convertView;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			DownHolder downHolder = (DownHolder) msg.obj;
			String tag = downHolder.tag;
			Bitmap bitmap = downHolder.bitmap;
			ImageView imageView = (ImageView) listView.findViewWithTag(tag);

			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}

		};
	};

	class DownHolder {
		String tag;
		Bitmap bitmap;
		ImageView imageView;
	}

	class Holder {
		ImageView imageView;
		TextView textView;
	}

	int firstItem;
	int visiItem;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			loadIMG(firstItem, visiItem);
		} else {
			cancelTask();
		}

	}

	private void cancelTask() {
		// TODO Auto-generated method stub
		// executor.shutdown();
		int p = blockingQueue.size();
		blockingQueue.clear();
		int q = blockingQueue.size();
		// /Log.i("tag", "p->" + p + " q->" + q);
	}

	int j;

	class MyRun implements Runnable {
		int i;

		public MyRun(int i) {
			// TODO Auto-generated constructor stub
			this.i = i;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Log.i("tag", "正在加载" + j + "张");
				String string = pathList.get(i);
				URL url = new URL(string);
				InputStream inputStream = url.openStream();
				// inputStream.mark(inputStream.available());
				Options opts = new Options();
				opts.outHeight = max;
				opts.outWidth = max;
				opts.inSampleSize = 2;
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
						opts);
				cache.put(string, bitmap);
				DownHolder downHolder = new DownHolder();
				downHolder.bitmap = bitmap;

				downHolder.tag = string;
				Message message = handler.obtainMessage();
				message.obj = downHolder;
				message.sendToTarget();
				Log.i("tag", "加载完毕" + j + "张");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void loadIMG(int firstItem2, int visiItem2) {
		// TODO Auto-generated method stub

		for (int i = firstItem2; i < firstItem2 + visiItem2; i++) {
			// E j = i;
			if (cache.get(pathList.get(i)) != null)
				continue;

			executor.execute(new MyRun(i));
		}

	}

	boolean ftime = true;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		firstItem = firstVisibleItem;
		visiItem = visibleItemCount;
		if (ftime && visibleItemCount > 0) {
			ftime = false;
			loadIMG(0, visibleItemCount);
		}

	}

}
