package com.example.listpics;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Show extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(getApplicationContext());

		textView.setText("f1"
				+ getApplicationContext().getFilesDir().getAbsolutePath()
				+ "\n");
		setContentView(textView);
	}
}
