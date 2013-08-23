package com.example.googleoauthexample;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoggedIn extends loginTask{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Check if its logged in.
		 * If not, then, login will be prompted.
		 * If already logged in, then content of this activity is displayed.
		 */
		if(isLoggedIn()){
			setContentView(R.layout.activity_logged_in);
			Button logout = (Button) findViewById(R.id.logout);
			logout.setOnClickListener(new OnClickListener(){
				public void onClick(View view){
					logout();
				}
			});
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_logged_in, menu);
		return true;
	}
}