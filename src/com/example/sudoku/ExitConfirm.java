package com.example.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ExitConfirm extends Activity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_confirm);
		
		View yesBtn = findViewById(R.id.exit_confirm_yes);
		View noBtn = findViewById(R.id.exit_confirm_no);
		yesBtn.setOnClickListener(this);
		noBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exit_confirm_yes:
			finish();
			getParent().finish();
			break;
		case R.id.exit_confirm_no:
			finish();
			break;
		}
	}
}
