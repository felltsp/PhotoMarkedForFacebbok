package com.felipetavares.photomarked.service;

import java.io.IOException;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

public class DownloadPhotoIntentService extends IntentService {

	Bitmap[] bitmap = null;
	
	public DownloadPhotoIntentService() {
		super("DownloadPhotoIntentService");
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		

		String[] urls = intent.getStringArrayExtra("LINK_PHOTOS_AVAILABLE_FOR_DOWNLOAD");
		bitmap = new Bitmap[urls.length];
		
		for(int i=0 ; i < urls.length ; i++){
			
			try {
				
				bitmap[i] = DownloadPhotoIntentService.downloadBitmap(urls[i]);	
			} catch (Exception e) {
				Log.d("PHOTOMARKED", "ERRO AO FAZER O DOWNLOAD DA FOTO : "+ urls[i] , e);
			}
		}
		Toast.makeText(getApplicationContext(), "Download das imagens terminados", Toast.LENGTH_LONG).show();;
	}


	// Utiliy method to download image from the internet
	static private Bitmap downloadBitmap(String url) throws IOException {
		HttpUriRequest request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);

		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);

			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,	bytes.length);
			return bitmap;
		} else {
			throw new IOException("Download failed, HTTP response code " + statusCode + " - " + statusLine.getReasonPhrase());
		}
	}
}
