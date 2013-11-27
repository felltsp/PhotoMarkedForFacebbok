package com.felipetavares.photomarked.vo;

import java.io.Serializable;



public class PhotoVO implements Serializable{

	private static final long serialVersionUID = 6180089909289864679L;
	private String pid;
	private String srcSmall;
	private String srcBig;
	private String name;
	private byte[] photo;

	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public String getSrcSmall() {
		return srcSmall;
	}
	public void setSrcSmall(String srcSmall) {
		this.srcSmall = srcSmall;
	}
	public String getSrcBig() {
		return srcBig;
	}
	public void setSrcBig(String srcBig) {
		this.srcBig = srcBig;
	}
	
}
