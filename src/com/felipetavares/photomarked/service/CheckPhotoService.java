package com.felipetavares.photomarked.service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.felipetavares.photomarked.facade.PhotoPersistenceFacade;
import com.felipetavares.photomarked.util.PreferencesAplicationKeys;
import com.felipetavares.photomarked.vo.PhotoVO;

/**
 * Class with the objective of checking for new tagged pictures available to download.
 * @author felipe.tavares
 *
 */
public class CheckPhotoService extends Service {
	
	private Timer timer = null;
	private TimerTask timerTask = null;
	private long period_in_minutes = 0;
	private PhotoPersistenceFacade photoFacade;
	private Request.Callback requestCallback = new Request.Callback() {

		@Override
		public void onCompleted(Response response) {
			try {
				List<PhotoVO> listOfPhotos = getListPhotoVOAvaliableForDownload(response);

				if(!listOfPhotos.isEmpty()){
					
					Intent intent = new Intent(CheckPhotoService.this, DownloadPhotoIntentService.class);
					intent.putExtra("LINK_PHOTOS_AVAILABLE_FOR_DOWNLOAD", (Serializable) listOfPhotos);
					startService(intent);
				}
				

			} catch (JSONException e) {
				Log.e("PHOTOMARKED", "erro ao fazer o parse do resultado", e);
				e.printStackTrace();
			}
		}

		private List<PhotoVO> getListPhotoVOAvaliableForDownload(Response response) throws JSONException{

			List<PhotoVO> list = new ArrayList<PhotoVO>();
			PhotoVO photo = null;

			JSONArray array = (JSONArray) response.getGraphObject().getInnerJSONObject().get("data");
			
			for(int index=0 ; array != null && index < array.length() ; index++){

				photo = new PhotoVO();
				photo.setPid(array.getJSONObject(index).getString("pid"));
				photo.setName(array.getJSONObject(index).getString("caption"));
				photo.setSrcBig(array.getJSONObject(index).getString("src_big"));
				photo.setSrcSmall(array.getJSONObject(index).getString("src_small"));
				list.add(photo);
				
			}
			
			return list;
		}
	};
	
	public CheckPhotoService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		period_in_minutes = sharedPreferences.getLong(PreferencesAplicationKeys.INTERVAL_VERIFICATION.name(), 10);
		photoFacade = new PhotoPersistenceFacade(getApplicationContext());
		
		final Handler handler = new Handler();
	    timer = new Timer();
	    timerTask = new TimerTask() {
			@Override
			public void run() {
				
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(isInternetActivated()){
							String[] array = photoFacade.getArrayAllIdsPhotos();
							checkPhotoAvaliableForDownload(array);
						}
					}
				});
			}
			
		};
		
		
		super.onCreate();
	}


	private void checkPhotoAvaliableForDownload(String[] idPhotosDownloaded){
		String fqlQuery = getFqlQuery(idPhotosDownloaded);
		
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);
		
		Session session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET, requestCallback);
		Request.executeBatchAsync(request);
		
	}

	private String getFqlQuery(String[] idPhotosDownloaded) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select pid, caption,  src_small, src_big from photo where pid in (SELECT pid from photo_tag where subject = me()) ");
		
		if(idPhotosDownloaded.length > 0){
			
			sb.append("and not ( pid in ( ");
			
			for(String id : idPhotosDownloaded){
				sb.append("'"+ id + "', ");
			}
			
			sb.setCharAt(sb.lastIndexOf(","), ' ');
			sb.append("))");
		}
		
		return sb.toString();
	}

	@Override
	public void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timer.scheduleAtFixedRate(timerTask, 0, getPeriodInMillis());
		return Service.START_STICKY;
	}
	
	private long getPeriodInMillis(){
		return period_in_minutes * 60 * 1000;
	}

	private boolean isInternetActivated() {
		ConnectivityManager connectionManger =  (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectionManger.getActiveNetworkInfo();
		
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		boolean isWifi 		= activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
		
		if((isConnected && isWifiOnly() && isWifi) || (isConnected && !isWifiOnly())){
			return true;
		}
		
		return false;
	}

	private boolean isWifiOnly() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean wifiConfigured = sharedPreferences.getBoolean(PreferencesAplicationKeys.WIFI_ENABLED.name(), false);
		return wifiConfigured;
	}
}
