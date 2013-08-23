package com.example.googleoauthexample;

import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class ChooseAccount extends loginTask {	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * This disables the title foe the theme Theme.dialog.
		 * It needs to be called immediately after super.onCreate() and
		 * always before setContentView.
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		chooseAccount();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}