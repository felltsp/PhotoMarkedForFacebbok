package com.felipetavares.photomarked.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;
import com.felipetavares.photomarked.R;
import com.felipetavares.photomarked.R.id;
import com.felipetavares.photomarked.R.layout;

//TODO: TROCAR TODA A PARTE DE LOGIN
public class MainActivity extends FragmentActivity {
	 private UserSettingsFragment userSettingsFragment;
	 private static String TAG = "PHOTOMARKED";
	 
	 Session.StatusCallback statusCallback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if(state.equals(SessionState.OPENED)){
				Intent i = new Intent(MainActivity.this, ConfigurationActivity.class);
		        startActivity(i);
			}
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
	}
	
	@Override
	protected void onPause() {
		userSettingsFragment.onPause();
		super.onPause();
	}
}
