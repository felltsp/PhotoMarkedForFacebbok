package com.felipetavares.photomarked.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;
import com.felipetavares.photomarked.R;

public class MainActivity extends FragmentActivity {
	 private UserSettingsFragment userSettingsFragment;
	 
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
        userSettingsFragment.setReadPermissions("user_photos","friends_photos");
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
