package com.listadapter.test;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.listpics.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ListMainAct extends Activity {
	ListView listView;
	public String[] pics = {
			"http://h.hiphotos.baidu.com/image/pic/item/a8014c086e061d95a53d6d3979f40ad162d9cabc.jpg",
			"http://a.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c21dd9bd2a0cf41bd5ad6e393c.jpg",
			"http://c.hiphotos.baidu.com/image/pic/item/e61190ef76c6a7ef1c8e252efffaaf51f3de660a.jpg",
			"http://c.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd42177fa21a38b87d6277ff948.jpg",
			"http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d1613a13169f1deb48f8c546450.jpg",
			"http://g.hiphotos.baidu.com/image/pic/item/a9d3fd1f4134970ade6ee02897cad1c8a7865d9c.jpg",
			"http://a.hiphotos.baidu.com/image/pic/item/caef76094b36acaf266fcff17ed98d1001e99c2b.jpg",
			"http://f.hiphotos.baidu.com/image/pic/item/0ff41bd5ad6eddc4cc97b9f03bdbb6fd5266334c.jpg",
			"http://c.hiphotos.baidu.com/image/pic/item/bf096b63f6246b6066845b7ce9f81a4c510fa234.jpg",
			"http://b.hiphotos.baidu.com/image/pic/item/a8ec8a13632762d056c418b6a2ec08fa503dc6c5.jpg",
			"http://g.hiphotos.baidu.com/image/w%3D2048/sign=154252fad762853592e0d521a4d776c6/6d81800a19d8bc3e3f272ec2808ba61ea8d3458e.jpg",
			"http://b.hiphotos.baidu.com/image/w%3D2048/sign=41d856d767380cd7e61ea5ed957cac34/a6efce1b9d16fdfa5ef76f89b68f8c5494ee7b3a.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D2048/sign=fc01ad46c88065387beaa313a3e5a144/77c6a7efce1b9d1692e8b269f1deb48f8c54648e.jpg",
			"http://h.hiphotos.baidu.com/image/w%3D2048/sign=27f3b4e89d3df8dca63d8891f929738b/9f510fb30f2442a78e0f47bad343ad4bd11302af.jpg",
			"http://b.hiphotos.baidu.com/image/pic/item/c2cec3fdfc03924557289e018594a4c27d1e251a.jpg",
			
			"http://d.hiphotos.baidu.com/image/pic/item/8326cffc1e178a823a7f37a4f403738da977e877.jpg",
			"http://e.hiphotos.baidu.com/image/pic/item/d833c895d143ad4bed76ddc980025aafa40f065a.jpg",
			"http://f.hiphotos.baidu.com/image/pic/item/d000baa1cd11728b12d8d623cafcc3cec3fd2c3d.jpg",
			"http://d.hiphotos.baidu.com/image/pic/item/f703738da97739123842cb7ffa198618367ae24c.jpg",
			"http://e.hiphotos.baidu.com/image/pic/item/f7246b600c33874447ce87c7500fd9f9d72aa006.jpg",

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listlay);
		listView = (ListView) findViewById(R.id.listView1);
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < pics.length; i++) {
			arrayList.add(pics[i]);
		}
		MyAdapter adapter = new MyAdapter(arrayList, getApplicationContext(),
				listView);
		listView.setOnScrollListener(adapter);
		listView.setAdapter(adapter);

	}
}
