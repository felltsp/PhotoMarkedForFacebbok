package com.felipetavares.photomarked.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.felipetavares.photomarked.R;
import com.felipetavares.photomarked.vo.PhotoVO;

public abstract class ManagerFile {

	private static File pathSdCard = Environment.getExternalStorageDirectory().getAbsoluteFile();
	
	public static void writePhotosInSDCard(List<PhotoVO> listOfPhotos){
		File directory = new File(pathSdCard + "/PhotoMarked");
		directory.mkdir();
		
		try {
			
			FileOutputStream fOut;
			for(PhotoVO photo : listOfPhotos){
				fOut = new FileOutputStream(new File(directory, getNamePhoto(photo.getName(), photo.getPid())));
				fOut.write(photo.getPhoto());
				fOut.flush();
				fOut.close();			
			}
		
		} catch (FileNotFoundException e) {
			Log.e("PHOTOMARKED", "");
			
		} catch (IOException e) {
			Log.e("PHOTOMARKED", "Erro ao gravar o arquivo no disco", e);
		}
	}

	private static String getNamePhoto(String name, String photoId) {
		String newName = name;
		if(newName == null || "".equals(newName)){
			newName = Resources.getSystem().getString(R.string.photo).concat("_"+photoId);
		}
		return newName.concat(".jpg") ;
	}
}
