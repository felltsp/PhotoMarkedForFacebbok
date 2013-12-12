package com.felipetavares.photomarked.activity;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.felipetavares.photomarked.R;
import com.felipetavares.photomarked.service.CheckPhotoService;
import com.felipetavares.photomarked.util.PreferencesAplicationKeys;

public class ConfigurationActivity extends Activity {
	
	private CheckedTextView checkTextWifiOnly 		= null;
	private CheckedTextView checkTextStartOnBoot 	= null;
	private TextView intervalOfVerification 		= null;
	
	TextView intervalOfVerificationSelected	= null; 
	SortedMap<Long, String> timeOfInterval	= null;
	int positionTimeIntervalSelected		= 0;
	AlertDialog dialogIntervalSelected 		= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		addEventsInConfigurationWifi();
		addEventsInConfigurationStartOnBoot();
		dialogIntervalSelected = createAlertDialogSelectTimeInterval();
		addEventsInConfigurationInterval();
		addEventsInButtonStart();
		addEventsInButtonStop();
	}

	private void addEventsInConfigurationStartOnBoot() {
		checkTextStartOnBoot = (CheckedTextView) findViewById(R.id.idConfigurationStartOnBoot);
		checkTextStartOnBoot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkTextStartOnBoot.setChecked(!checkTextStartOnBoot.isChecked());
			}
		});
		
		checkTextWifiOnly.setChecked(getSharedPreferences(PreferencesAplicationKeys.PREFERENCES.name(), MODE_PRIVATE).getBoolean(PreferencesAplicationKeys.WIFI_ONLY.name(), false));
	}

	private void addEventsInConfigurationWifi() {
		checkTextWifiOnly = (CheckedTextView) findViewById(R.id.idConfigurationWifiOnly);
		checkTextWifiOnly.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkTextWifiOnly.setChecked(!checkTextWifiOnly.isChecked());
			}
		});
		
		checkTextWifiOnly.setChecked(getSharedPreferences(PreferencesAplicationKeys.PREFERENCES.name(), MODE_PRIVATE).getBoolean(PreferencesAplicationKeys.START_ON_BOOT.name(), false));
	}
	

	private void addEventsInConfigurationInterval() {
		intervalOfVerificationSelected = (TextView) findViewById(R.id.idConfigurationShowInterval);
		intervalOfVerification = (TextView) findViewById(R.id.idConfigurationInterval);
		intervalOfVerification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogIntervalSelected.show();
			}
		});
		
		Long key = PreferenceManager.getDefaultSharedPreferences(this).getLong(PreferencesAplicationKeys.INTERVAL_VERIFICATION.name(), 10L);
		intervalOfVerificationSelected.setText(timeOfInterval.get(key));
	}

	private AlertDialog createAlertDialogSelectTimeInterval() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ConfigurationActivity.this);
		builder.setTitle(R.string.title_dialog_interval_of_verification);
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		prepareTimeOfInterval();
		ListAdapter listChoiceInterval = new ArrayAdapter<String>(ConfigurationActivity.this, android.R.layout.simple_list_item_single_choice, new ArrayList<String>(timeOfInterval.values()));
		builder.setSingleChoiceItems(listChoiceInterval, positionTimeIntervalSelected, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int position) {
				positionTimeIntervalSelected = position;
				long key = (Long)timeOfInterval.keySet().toArray()[positionTimeIntervalSelected];
				intervalOfVerificationSelected.setText(timeOfInterval.get(key));
				dialog.dismiss();
			}

		});
		return builder.create();
	}

	private void prepareTimeOfInterval() {
		timeOfInterval = new TreeMap<Long, String>();
		timeOfInterval.put(10L, "10min");
		timeOfInterval.put(30L, "30min");
		timeOfInterval.put(60L, "60min");
		timeOfInterval.put(120L, "02h");
		timeOfInterval.put(480L, "08h");
		timeOfInterval.put(1440L, "24h");
	}

	private void addEventsInButtonStart(){
		Button btnSave = (Button) findViewById(R.id.idBtnStart);
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveConfigurations();
				Intent checkPhotoServiceIntent = new Intent(ConfigurationActivity.this, CheckPhotoService.class);
				checkPhotoServiceIntent.putExtra("session", Session.getActiveSession());
				startService(checkPhotoServiceIntent);
				Toast.makeText(getApplicationContext(), R.string.message_start_service, Toast.LENGTH_SHORT).show();
				v.setEnabled(false);
			}
		});
		
		if(isCheckPhotoServiceRunning()){
			btnSave.setEnabled(false);
		}
	}
	
	protected void saveConfigurations() {
		savePreferences(PreferencesAplicationKeys.WIFI_ONLY.name(), checkTextWifiOnly.isChecked());
		savePreferences(PreferencesAplicationKeys.START_ON_BOOT.name(), checkTextStartOnBoot.isChecked());
		savePreferences(PreferencesAplicationKeys.INTERVAL_VERIFICATION.name(), (Long)timeOfInterval.keySet().toArray()[positionTimeIntervalSelected]);
	}

	private void addEventsInButtonStop(){
		Button btnStop = (Button) findViewById(R.id.idBtnStop);
		btnStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isCheckPhotoServiceRunning()){
					Intent checkPhotoServiceIntent = new Intent(ConfigurationActivity.this, CheckPhotoService.class);
					stopService(checkPhotoServiceIntent);
					((Button) findViewById(R.id.idBtnStart)).setEnabled(true);
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
	
	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = getSharedPreferences(PreferencesAplicationKeys.PREFERENCES.name(), MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	private void savePreferences(String key, Long value) {
		SharedPreferences sharedPreferences = getSharedPreferences(PreferencesAplicationKeys.PREFERENCES.name(), MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("position", positionTimeIntervalSelected);
		outState.putBoolean("isShow", dialogIntervalSelected.isShowing());
		outState.putCharSequence("intervalSelected", intervalOfVerificationSelected.getText());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null){
			positionTimeIntervalSelected = savedInstanceState.getInt("position");
			intervalOfVerificationSelected.setText(savedInstanceState.getCharSequence("intervalSelected"));
			dialogIntervalSelected = createAlertDialogSelectTimeInterval();
			
			if(savedInstanceState.getBoolean("isShow")){
				dialogIntervalSelected.show();
			}
			
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
}
