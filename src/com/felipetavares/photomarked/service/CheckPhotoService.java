package com.felipetavares.photomarked.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.felipetavares.photomarked.util.PreferencesAplicationKeys;

/**
 * Class with the objective of checking for new tagged pictures available to download.
 * @author felipe.tavares
 *
 */
public class CheckPhotoService extends Service {

	public CheckPhotoService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(getApplicationContext(), "Service Destroy", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(isInternetActivated()){
			Toast.makeText(getApplicationContext(), "Internet conectada", Toast.LENGTH_LONG).show();	
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private boolean isInternetActivated() {
		ConnectivityManager connectionManger =  (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectionManger.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		if(isConnected && isConfiguratedInternet(activeNetwork)){
			return true;
		}
		
		return false;
	}

	private boolean isConfiguratedInternet(NetworkInfo activeNetwork) {
		
		boolean isConfigured = false;
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean wifiConfigured = sharedPreferences.getBoolean(PreferencesAplicationKeys.WIFI_ENABLED.name(), false);
		boolean mobileConfigured = sharedPreferences.getBoolean(PreferencesAplicationKeys.MOBILE_ENABLED.name(), false);
		
		if(wifiConfigured){
			isConfigured = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
			
		}else if(mobileConfigured){
			isConfigured = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
			
		}
		
		return isConfigured;
	}
}
