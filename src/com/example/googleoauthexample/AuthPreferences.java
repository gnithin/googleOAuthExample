package com.example.googleoauthexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * AuthPreferences is a class that uses shared preferences to store token and user Information.
 * This Authentication preference will be persistent across all the activities of an application.
 * After the token is validated from the application server, the data it gives can be stored here.  
 */
public class AuthPreferences {
	private static final String KEY_USER = "user";
	private static final String KEY_TOKEN = "token";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_GIVEN_NAME = "given_name";
	private static final String KEY_GENDER = "gender";
	private static final String KEY_LINK = "link";
	
	private SharedPreferences preferences;
 
	public AuthPreferences(Context context) {
		preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
	}
 
	public void setUser(String user) {
		Editor editor = preferences.edit();
		editor.putString(KEY_USER, user);
		editor.commit();
	}
	public void setToken(String password) {
		Editor editor = preferences.edit();
		editor.putString(KEY_TOKEN, password);
		editor.commit();
	}
	public void removeToken(){
		Editor editor = preferences.edit();
		editor.putString(KEY_TOKEN, null);
		editor.putString(KEY_USER, null);
		editor.putString(KEY_USERNAME, null);
		editor.putString(KEY_GENDER, null);
		editor.putString(KEY_LINK, null);
		editor.putString(KEY_GIVEN_NAME, null);
		editor.commit();
	}
	public String getUser() {
		return preferences.getString(KEY_USER, null);
	}
 
	public String getToken() {
		return preferences.getString(KEY_TOKEN, null);
	}
	
	public String getName() {
		return preferences.getString(KEY_USERNAME, null);
	}
	
	public String getGivenName() {
		return preferences.getString(KEY_GIVEN_NAME, null);
	}
	
	public String getGender() {
		return preferences.getString(KEY_GENDER, null);
	}
	
	public String getLink() {
		return preferences.getString(KEY_LINK, null);
	}
	
	public void setUserDetails(String name_, String given_name_, String link_ , String gender_){
		Editor editor = preferences.edit();
		editor.putString(KEY_GENDER, gender_);
		editor.putString(KEY_GIVEN_NAME, given_name_);
		editor.putString(KEY_USERNAME, name_);
		editor.putString(KEY_LINK, link_);
		editor.commit();
	}
	public boolean IsLoggedIn(){
		return (preferences.getString(KEY_USER, null) != null && preferences.getString(KEY_TOKEN, null) != null);
	}
}