package com.felipetavares.photomarked.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.Session;
import com.felipetavares.photomarked.service.CheckPhotoService;
import com.felipetavares.photomarked.util.PreferencesAplicationKeys;

public class CheckPhotoStartBroadcast extends BroadcastReceiver {

	public CheckPhotoStartBroadcast() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Session.openActiveSessionFromCache(context);
			if(isStartOnBoot(context)){
				Intent i = new Intent(context, CheckPhotoService.class);
				context.startService(i);
			}
        }
	}

	private boolean isStartOnBoot(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PreferencesAplicationKeys.PREFERENCES.name(), Context.MODE_PRIVATE);
		return sp.getBoolean(PreferencesAplicationKeys.START_ON_BOOT.name(), false);
	}
}
