package com.health.hl.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.health.hl.app.App;

/**
 * Created by xavi 2015/4/27
 */
public class SettingInfo {

	// private String device_name;
	// private String device_address;
	private String userName;
	private String sex;
	private String age;
	private String height;
	private String level;
	private String weight;
	// private String saveTime;
	public static final String USER_NAME = "user-name";
	public static final String SEX = "sex";
	public static final String AGE = "age";
	public static final String HEIGHT = "height";
	public static final String LEVEL = "level";
	public static final String WEIGHT = "weight";
	// public static final String SAVE_TIME ="saveTime";

	public static final String PREFS_NAME = "com.sayes.balance.models.setting_info";

	// public static final String DEVICE_NAME ="device-name";
	// public static final String DEVICE_ADDRESS ="device-address";

	private static SettingInfo state;

	private SettingInfo() {
		reload();
	}

	public static SettingInfo get() {

		if (state == null) {
			state = new SettingInfo();
		}

		return state;
	}

	/**
	 * 读取 SharedPreferences 信息
	 */
	public void reload() {

		SharedPreferences prefs = App.get().getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		// device_name = prefs.getString(DEVICE_NAME, "");
		// device_address = prefs.getString(DEVICE_ADDRESS, "");
		userName = prefs.getString(USER_NAME, " ");
		sex = prefs.getString(SEX, "0");
		height = prefs.getString(HEIGHT, "175");
		age = prefs.getString(AGE, "30");
		level = prefs.getString(LEVEL, "0");
		weight = prefs.getString(WEIGHT, "60");
		// saveTime=prefs.getString(SAVE_TIME, "");
	}

	/**
	 * 保存 SharedPreferences 信息
	 * 
	 * @return
	 */
	public boolean save() {

		SharedPreferences prefs = App.get().getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();

		// editor.putString(DEVICE_NAME,device_name);
		// editor.putString(DEVICE_ADDRESS,device_address);
		editor.putString(USER_NAME, userName);
		editor.putString(SEX, sex);
		editor.putString(HEIGHT, height);
		editor.putString(AGE, age);
		editor.putString(LEVEL, level);
		editor.putString(WEIGHT, weight);
		// editor.putString(SAVE_TIME,saveTime);
		return editor.commit();
	}

	public void clearData() {
		SharedPreferences prefs = App.get().getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		prefs.edit().clear().commit();
	}

	// public String getDevice_name() {
	// return device_name;
	// }
	//
	// public void setDevice_name(String device_name) {
	// this.device_name = device_name;
	// }
	//
	// public String getDevice_address() {
	// return device_address;
	// }
	//
	// public void setDevice_address(String device_address) {
	// this.device_address = device_address;
	// }

	public String getUserName() {
		return userName;
	}

	// public String getSaveTime() {
	// return saveTime;
	// }
	//
	// public void setSaveTime(String saveTime) {
	// this.saveTime = saveTime;
	// }

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

}