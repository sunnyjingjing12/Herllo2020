package com.health.hl.entity;

import java.io.Serializable;

public class SkippingInfo implements Serializable {
	private String date;
	// 当前次数
	private int count_a;
	// 当前时间
	private String count_duration;
	private String count_kaluli;
	private int count_sudu ;
	// 当日次数
	private int day_a ;
	private String day_kaluli;
	private int tagNum;
	private String percent;
	//日累计时间
	private int day_time;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getTagNum() {
		return tagNum;
	}
	public void setTagNum(int tagNum) {
		this.tagNum = tagNum;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public int getCount_a() {
		return count_a;
	}
	public void setCount_a(int count_a) {
		this.count_a = count_a;
	}
	public String getCount_duration() {
		return count_duration;
	}
	public void setCount_duration(String count_duration) {
		this.count_duration = count_duration;
	}
	public String getCount_kaluli() {
		return count_kaluli;
	}
	public void setCount_kaluli(String count_kaluli) {
		this.count_kaluli = count_kaluli;
	}
	public int getCount_sudu() {
		return count_sudu;
	}
	public void setCount_sudu(int count_sudu) {
		this.count_sudu = count_sudu;
	}
	public int getDay_a() {
		return day_a;
	}
	public void setDay_a(int day_a) {
		this.day_a = day_a;
	}
	public String getDay_kaluli() {
		return day_kaluli;
	}
	public void setDay_kaluli(String day_kaluli) {
		this.day_kaluli = day_kaluli;
	}
	public int getDay_time() {
		return day_time;
	}
	public void setDay_time(int day_time) {
		this.day_time = day_time;
	}
	
}
