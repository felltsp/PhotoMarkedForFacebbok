package com.felipetavares.photomarked.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;
import com.felipetavares.photomarked.R;
import com.felipetavares.photomarked.service.CheckPhotoService;

public class MainActivity extends FragmentActivity {

	private UserSettingsFragment userSettingsFragment;

	Session.StatusCallback statusCallback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if(state.equals(SessionState.OPENED)){
				openApplicationConfiguration(); 
			}else if(state.equals(SessionState.CLOSED)){
				Intent i = new Intent(MainActivity.this, CheckPhotoService.class);
				stopService(i);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		if(Session.getActiveSession()!= null && Session.getActiveSession().isOpened()){

			openApplicationConfiguration();

		}else{

			FragmentManager fragmentManager = getSupportFragmentManager();
			userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
			userSettingsFragment.setReadPermissions("user_photos","friends_photos");
			userSettingsFragment.setSessionStatusCallback(statusCallback);

		}

	}

	private void openApplicationConfiguration(){
		Intent i = new Intent(MainActivity.this, ConfigurationActivity.class);
		startActivity(i);						
	}

}
