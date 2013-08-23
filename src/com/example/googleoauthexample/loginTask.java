package com.example.googleoauthexample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public abstract class loginTask extends Activity {
	private static final int AUTHORIZATION_CODE = 1993;
	private static final int ACCOUNT_CODE = 1601;
	private Dialog progressDialog;
	
	/**
	 * The SCOPE can change depending on the needs.
	 * The scope - userinfo.profile gives the basic user information which would be displayed in their profile.
	 * A good source for information on the various scopes - http://discovery-check.appspot.com/oauth2/v2
	 */
	private final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
	
	public Context context;
	private AuthPreferences authPreferences;
	private AccountManager accountManager;

	/**
	 * initialise() method is for initialising the Authentication preferences class, and the AccountManager.
	 */
	private void initialise(){
		this.context = this;
		authPreferences = new AuthPreferences(context);
		accountManager = AccountManager.get(context);
	}
	
	/**
	 * chooseAccount() method displays a dialog in which, all the google accounts already present on the phone are 
	 * displayed.
	 * The AccountManager.newChooseAccountIntent() does not work for all API levels below 14
	 * TODO: Backward compatibility.
	 */
	public void chooseAccount() {
		initialise();
		Intent intent = AccountManager.newChooseAccountIntent(null, null,new String[] { "com.google" }, false, null, null, null, null);
		startActivityForResult(intent, ACCOUNT_CODE);
	}
	
	/**
	 * On choosing an account, we must take the appropriate action.
	 * The Authentication Preferences is updated for the account chosen, and the token is requested.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			//Starting the progress dialog.
			progressDialog = ProgressDialog.show(this, "Loading...", "");
			if (requestCode == AUTHORIZATION_CODE) {
				requestToken();
			} else if (requestCode == ACCOUNT_CODE) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				authPreferences.setUser(accountName);
				invalidateToken();
				requestToken();
			}
		}
		else{
			/**
			 * If the user cancels from selecting any account, simply exit.
			 */
			this.finish();
		}
	}
	
	/**
	 * Make a token Invalid. This is done usually, when we obtain a new token.
	 */
	private void invalidateToken() {
		accountManager = AccountManager.get(this);
		accountManager.invalidateAuthToken("com.google", authPreferences.getToken());
		authPreferences.setToken(null);
	}
	
	/**
	 * requestToken() requests the token for SCOPE specified and also specifies the callBack.
	 */
	private void requestToken() {
		Account userAccount = null;
		String user = authPreferences.getUser();
		for (Account account : accountManager.getAccountsByType("com.google")) {
			if (account.name.equals(user)) {
				userAccount = account;
				break;
			}
		}
		accountManager.getAuthToken(userAccount, "oauth2:" + SCOPE, null, this,new OnTokenAcquired(), null);
	}
 
	/**
	 * On acquiring the token from google, we update our Authentication Preferences.
	 * At this point, we need to send the token to our so that it validates our token.
	 */
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			try {
				Bundle bundle = result.getResult();
				Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
				if (launch != null) {
					startActivityForResult(launch, AUTHORIZATION_CODE);
				} else {
					/**
					 * The token has been received successfully.
					 */
					progressDialog.dismiss();
					String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
					authPreferences.setToken(token);
					authenticated();
				}
			} catch (Exception e) {
				log(e.toString());
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * If the token is already present, or of the token has been successfully obtained,
	 * this function will be called.
	 */
	private void authenticated() {
		log(authPreferences.getToken());
		message("Token obtained!");
		
		Intent intent = new Intent(this, LoggedIn.class);
		this.finish();
		startActivity(intent);
	}
	
	/**
	 * isLoggedIn() is used to check if the user is already logged in or not.
	 * @return the state of login.
	 */
	public boolean isLoggedIn(){
		initialise();
		if(authPreferences.IsLoggedIn()){
			return true;
		}
		this.finish();
		Intent intent = new Intent(this, ChooseAccount.class);
		startActivity(intent);
		return false;
	}
	
	/**
	 * logout() method removes all the user information from the Authentication Preferences object, 
	 * and redirects the user to choose an account.
	 */
	public void logout(){
		authPreferences.removeToken();
		((Activity) context).finish();
		context.startActivity( new Intent(context, ChooseAccount.class) );
	}
	/**
	 * Helper methods.
	 */
	private void log(String message){
		Log.e("Error" , message);
	}
	private void message(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}