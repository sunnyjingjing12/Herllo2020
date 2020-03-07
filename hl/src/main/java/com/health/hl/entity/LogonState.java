package com.health.hl.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.health.hl.app.App;


/**
 * Created by xavi
 * 2014/10/21
 */
public class LogonState {
	
    private boolean rememberPwd;
    private String token;

    private  String picBigPath;
    private  String picSamllPath;

    private String user_name;
    private  String user_password;

    public static final String PREFS_NAME = "com.haoshamain.logon-state";

    public static final String REMEMBER_PWD = "remember-pwd";

    public  static  final  String USER_NAME ="user-name";
    public  static  final  String USER_PASSWORD ="user-password";


    private static LogonState state;

    private LogonState() {
        reload();
    }

    public static LogonState get() {

        if (state == null) {
            state = new LogonState();
        }

        return state;
    }
    /**
     * 读取 SharedPreferences 信息
     */
    public void reload() {

        SharedPreferences prefs = App.get().getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );

        rememberPwd = prefs.getBoolean(REMEMBER_PWD, false);
        user_name = prefs.getString(USER_NAME, "");
        user_password = prefs.getString(USER_PASSWORD, "");
    }
    /**
     * 保存 SharedPreferences 信息
     * @return
     */
    public boolean save() {

        SharedPreferences prefs = App.get().getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(REMEMBER_PWD, rememberPwd);
        editor.putString(USER_NAME,user_name);
        editor.putString(USER_PASSWORD,user_password);
        return editor.commit();
    }
    /**
     * 清除 SharedPreferences 信息
     * @return
     */
    public boolean clear(){
    	SharedPreferences prefs = App.get().getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = prefs.edit();
        return editor.clear().commit();
    }

    public boolean isRememberPwd() {
		return rememberPwd;
	}

	public void setRememberPwd(boolean rememberPwd) {
		this.rememberPwd = rememberPwd;
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPicBigPath() {
        return picBigPath;
    }

    public void setPicBigPath(String picBigPath) {
        this.picBigPath = picBigPath;
    }

    public String getPicSamllPath() {
        return picSamllPath;
    }

    public void setPicSamllPath(String picSamllPath) {
        this.picSamllPath = picSamllPath;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    
}