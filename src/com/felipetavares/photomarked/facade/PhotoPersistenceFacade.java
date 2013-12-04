package com.felipetavares.photomarked.facade;

import android.content.Context;

import com.felipetavares.photomarked.dao.PhotoDao;

public class PhotoPersistenceFacade {

	private static Context context;
	public PhotoPersistenceFacade(Context context) {
		PhotoPersistenceFacade.context = context;
	}
	
	public String[] getArrayAllIdsPhotos(){
		return new PhotoDao(PhotoPersistenceFacade.context).getArrayAllIdsPhotos();
	}
	
	public long insertIdPhoto(String idPhoto){
		return new PhotoDao(PhotoPersistenceFacade.context).insertIdPhoto(idPhoto);
	}
}
