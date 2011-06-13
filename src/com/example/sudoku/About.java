package com.example.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class About extends Activity {

	private static final String TAG = "ABOUT";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		finish();
		return true;
	}
	
}
