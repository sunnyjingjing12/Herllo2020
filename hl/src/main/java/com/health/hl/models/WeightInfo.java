package com.health.hl.models;

import java.io.Serializable;

public class WeightInfo implements Serializable{
	private String userName;
	private String time;
	private String weight;
	private String guge;
	private String zhifang;
	private String jirou;
	private String shuifen;
	private String neizang;
	private String BMR;
	private float bmi;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getGuge() {
		return guge;
	}
	public void setGuge(String guge) {
		this.guge = guge;
	}
	public String getZhifang() {
		return zhifang;
	}
	public void setZhifang(String zhifang) {
		this.zhifang = zhifang;
	}
	public String getJirou() {
		return jirou;
	}
	public void setJirou(String jirou) {
		this.jirou = jirou;
	}
	public String getShuifen() {
		return shuifen;
	}
	public void setShuifen(String shuifen) {
		this.shuifen = shuifen;
	}
	public String getNeizang() {
		return neizang;
	}
	public void setNeizang(String neizang) {
		this.neizang = neizang;
	}
	public String getBMR() {
		return BMR;
	}
	public void setBMR(String bMR) {
		BMR = bMR;
	}
	public float getBmi() {
		return bmi;
	}
	public void setBmi(float bmi) {
		this.bmi = bmi;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
