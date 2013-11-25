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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
        userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.d(TAG, String.format("New session state: %s", state.toString()));
            }
        });
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		userSettingsFragment.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "MainActivity:onActivityResult | Vendo quando o metodo Ž chamado");
        super.onActivityResult(requestCode, resultCode, data);
        Intent i = new Intent(MainActivity.this, ConfiguracaoActivity.class);
        startActivity(i);
	}
	
}
