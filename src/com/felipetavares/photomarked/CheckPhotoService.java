package com.felipetavares.photomarked;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Class com o objtivo de verificar se exitem novas fotos marcadas disponiveis para se fazer o download.
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
}
