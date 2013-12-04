package com.felipetavares.photomarked.dao;

import java.util.ArrayList;
import java.util.List;

import com.felipetavares.photomarked.util.PhotosTableKeys;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PhotoDao extends Dao {


	public PhotoDao(Context ctx) {
		super(ctx);
		open();
	}

	public long insertIdPhoto(String pid) {
		ContentValues campos = new ContentValues();
		campos.put(PhotosTableKeys.COLUMN_ID_PHOTO.getKey(), pid);
		long _id = db.insert(PhotosTableKeys.TABLE_NAME.getKey(), null, campos);
		close();
		return _id;
	}

	public String[] getArrayAllIdsPhotos() {
		String[] idsPhotos = new String[]{};
		Cursor c = db.query(PhotosTableKeys.TABLE_NAME.getKey(), new String[] {PhotosTableKeys.COLUMN_ID.getKey(), PhotosTableKeys.COLUMN_ID_PHOTO.getKey()}, null, null, null, null, null, null);
		
		if (c.moveToFirst()){
			List<String> list = new ArrayList<String>();
			
			do {
				list.add(c.getString(c.getColumnIndex(PhotosTableKeys.COLUMN_ID_PHOTO.getKey())));
			} while (c.moveToNext());
			
			idsPhotos = list.toArray(idsPhotos);
		}
		
		close();
		return idsPhotos; 
	}

}
