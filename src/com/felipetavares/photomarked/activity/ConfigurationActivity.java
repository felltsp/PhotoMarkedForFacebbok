package com.felipetavares.photomarked.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.felipetavares.photomarked.R;
import com.felipetavares.photomarked.service.CheckPhotoService;

public class ConfigurationActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		List<String> listConfiguration = getListConfiguration();
		ListView listViewConfiguration = (ListView) findViewById(R.id.listViewConfigurations);
		listViewConfiguration.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, listConfiguration));
		addEventsInButtonSave();
		addEventsInButtonStop();
		
	}

	private List<String> getListConfiguration(){
		Resources resource = getResources();
		List<String> list = new ArrayList<String>();
		list.add(resource.getString(R.string.download_photo_mobile_network));
		list.add(resource.getString(R.string.download_photo_wifi_network));
		return list;
	}

	private void addEventsInButtonSave(){
		Button btnSave = (Button) findViewById(R.id.idBtnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// no off task in android
				/*Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, 10);
				Intent intent = new Intent(ConfigurationActivity.this, CheckPhotoService.class);
				PendingIntent pIntent = PendingIntent.getService(ConfigurationActivity.this, 0, intent, 0);
				AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60 * time_in_minutes, pIntent);*/
				
				Intent checkPhotoServiceIntent = new Intent(ConfigurationActivity.this, CheckPhotoService.class);
				startService(checkPhotoServiceIntent);
			}
		});
	}
	
	private void addEventsInButtonStop(){
		Button btnStop = (Button) findViewById(R.id.idBtnStop);
		btnStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isCheckPhotoServiceRunning()){
					Intent checkPhotoServiceIntent = new Intent(ConfigurationActivity.this, CheckPhotoService.class);
					stopService(checkPhotoServiceIntent);	
				}
			}
		});
	}
	
	private boolean isCheckPhotoServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (CheckPhotoService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
