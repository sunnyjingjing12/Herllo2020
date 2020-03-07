package com.health.hl.entity;

import java.io.Serializable;

public class WeekInfo implements Serializable{
	/**
	 * 星期一：全写Monday缩写Mon.
	 * 
	 * 星期二：全写Tuesday缩写Tue.
	 * 
	 * 星期三：全写Wednesday缩写Wed.
	 * 
	 * 星期四：全写Thursday缩写Thu.
	 * 
	 * 星期五：全写Friday缩写Fri.
	 * 
	 * 星期六：全写Saterday缩写Sat.
	 * 
	 * 星期日：全写Sunday缩写Sun
	 */
	private String mon;
	private String tue;
	private String wed;
	private String thu;
	private String fri;
	private String sat;
	private String sun;
	public String getMon() {
		return mon;
	}
	public void setMon(String mon) {
		this.mon = mon;
	}
	public String getTue() {
		return tue;
	}
	public void setTue(String tue) {
		this.tue = tue;
	}
	public String getWed() {
		return wed;
	}
	public void setWed(String wed) {
		this.wed = wed;
	}
	public String getThu() {
		return thu;
	}
	public void setThu(String thu) {
		this.thu = thu;
	}
	public String getFri() {
		return fri;
	}
	public void setFri(String fri) {
		this.fri = fri;
	}
	public String getSat() {
		return sat;
	}
	public void setSat(String sat) {
		this.sat = sat;
	}
	public String getSun() {
		return sun;
	}
	public void setSun(String sun) {
		this.sun = sun;
	}
	
}
