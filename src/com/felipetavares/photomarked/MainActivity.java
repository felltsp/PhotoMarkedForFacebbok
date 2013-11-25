package com.felipetavares.photomarked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;

public class MainActivity extends FragmentActivity {
	 private UserSettingsFragment userSettingsFragment;
	 private static String TAG = "PHOTOMARKED";
	 
	 Session.StatusCallback statusCallback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			Log.d(TAG, String.format("New session state: %s", state.toString()));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fragmentManager = getSupportFragmentManager();
        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
        userSettingsFragment.setSessionStatusCallback(statusCallback);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onStart() {
		userSettingsFragment.onStart();
		super.onStart();
		Session session = Session.getActiveSession();
		if(session != null && session.isOpened()){
			
			Log.d(TAG, "MainActivity::onResume | Session ativa e aberta");
			Intent i = new Intent(MainActivity.this, ConfigurationActivity.class);
	        startActivity(i);
			
		}
	}
	
	@Override
	protected void onPause() {
		userSettingsFragment.onPause();
		super.onPause();
	}
}
