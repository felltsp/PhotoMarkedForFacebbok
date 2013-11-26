package com.felipetavares.photomarked.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

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
            Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_LONG).show();
            super.onCreate();
     }

     @Override
     public void onDestroy() {
            Toast.makeText(getApplicationContext(), "Service Destroy", Toast.LENGTH_LONG).show();
            super.onDestroy();
     }

     @Override
     public int onStartCommand(Intent intent, int flags, int startId) {
            Toast.makeText(getApplicationContext(), "Service Running ", Toast.LENGTH_LONG).show();
            return super.onStartCommand(intent, flags, startId);
     }
}
