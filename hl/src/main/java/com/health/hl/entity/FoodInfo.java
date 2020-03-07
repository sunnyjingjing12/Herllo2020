package com.health.hl.entity;

import java.io.Serializable;

public class FoodInfo implements Serializable,Comparable<FoodInfo>{
	private String date;
	private Integer belong;
	private String name;
	private int img_id;
	private int calorie;
	private String unit;
	private float num;
	public FoodInfo(){
		
	}
	public FoodInfo(Integer belong,String name,int img_id,String unit,int calorie,float num){
		this.name=name;
		this.img_id=img_id;
		this.calorie=calorie;
		this.unit=unit;
		this.num=num;
		this.belong=belong;
	}
	public int getImg_id() {
		return img_id;
	}
	public void setImg_id(int img_id) {
		this.img_id = img_id;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public float getNum() {
		return num;
	}
	public void setNum(float num) {
		this.num = num;
	}
	
	public Integer getBelong() {
		return belong;
	}
	public void setBelong(Integer belong) {
		this.belong = belong;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public int compareTo(FoodInfo arg0) {
		// TODO Auto-generated method stub
		return this.getBelong().compareTo(arg0.getBelong());
	}
	
}
