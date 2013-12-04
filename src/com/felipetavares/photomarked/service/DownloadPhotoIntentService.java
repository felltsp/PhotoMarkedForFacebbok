package com.felipetavares.photomarked.service;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.felipetavares.photomarked.util.ManagerFile;
import com.felipetavares.photomarked.vo.PhotoVO;

public class DownloadPhotoIntentService extends IntentService {

	
	public DownloadPhotoIntentService() {
		super("DownloadPhotoIntentService");
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		

		@SuppressWarnings("unchecked")
		List<PhotoVO> listOfPhotos = (List<PhotoVO>) intent.getSerializableExtra("LINK_PHOTOS_AVAILABLE_FOR_DOWNLOAD");
		
		for(PhotoVO photo : listOfPhotos){
			
			try {
				downloadPhoto(photo);
				ManagerFile.writePhotoInSDCard(photo);
				
			} catch (Exception e) {
				Log.d("PHOTOMARKED", "ERRO AO FAZER O DOWNLOAD DA FOTO : "+ photo , e);
			}
		}
	}

	private void downloadPhoto(PhotoVO photo) throws IOException {
		HttpUriRequest request = new HttpGet(photo.getSrcBig());
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);

		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();

		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);
			photo.setPhoto(bytes);
			
		} else {
			throw new IOException("Download failed, HTTP response code " + statusCode + " - " + statusLine.getReasonPhrase());
		}
	}
}
