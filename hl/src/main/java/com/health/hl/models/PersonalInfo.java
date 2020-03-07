package com.health.hl.models;

import java.io.Serializable;

public class PersonalInfo implements Serializable{
	private String userName;
    private String sex;
    private String age;
    private String height;
    private String level;
    private String weight;
    private int id;
    private String currentDate;
    private static PersonalInfo personalInfo;
	public static PersonalInfo get(){
		if(personalInfo==null){
			personalInfo=new PersonalInfo();
		}
		return personalInfo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	
}
