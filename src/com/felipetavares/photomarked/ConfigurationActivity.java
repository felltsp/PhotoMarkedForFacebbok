package com.felipetavares.photomarked;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ConfigurationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		List<String> listConfiguration = getListConfiguration();
		ListView listViewConfiguration = (ListView) findViewById(R.id.listViewConfigurations);
		listViewConfiguration.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, listConfiguration));
	}

	private List<String> getListConfiguration(){
		Resources resource = getResources();
		List<String> list = new ArrayList<String>();
		list.add(resource.getString(R.string.download_photo_mobile_network));
		list.add(resource.getString(R.string.download_photo_wifi_network));
		return list;
	}
}
