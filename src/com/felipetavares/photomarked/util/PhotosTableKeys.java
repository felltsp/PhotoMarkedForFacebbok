package com.felipetavares.photomarked.util;

public enum PhotosTableKeys {

	TABLE_NAME("photos_downloaded"), COLUMN_ID("_id"), COLUMN_ID_PHOTO("id_photo") ;
	
	private String key;
	PhotosTableKeys(String value){
		this.key = value;
	}
	
	public String getKey() {
		return key;
	}
	
}
