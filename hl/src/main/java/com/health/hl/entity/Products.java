package com.health.hl.entity;

import java.io.Serializable;

public class Products implements Serializable{
	private String name;
	private int icon_s_id;
	private int icon_n_id;
	private int ic_content;
	private boolean isUpdate;
	private String package_name;
	private String update_path;
	private int [] img_array;
	private String content;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIcon_s_id() {
		return icon_s_id;
	}
	public void setIcon_s_id(int icon_s_id) {
		this.icon_s_id = icon_s_id;
	}
	public int getIcon_n_id() {
		return icon_n_id;
	}
	public void setIcon_n_id(int icon_n_id) {
		this.icon_n_id = icon_n_id;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getUpdate_path() {
		return update_path;
	}
	public void setUpdate_path(String update_path) {
		this.update_path = update_path;
	}
	public int[] getImg_array() {
		return img_array;
	}
	public void setImg_array(int[] img_array) {
		this.img_array = img_array;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIc_content() {
		return ic_content;
	}
	public void setIc_content(int ic_content) {
		this.ic_content = ic_content;
	}
	
	
}
