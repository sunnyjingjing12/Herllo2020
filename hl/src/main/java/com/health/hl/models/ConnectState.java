package com.health.hl.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.health.hl.app.App;


/**
 * Created by xavi
 * 2015/4/27
 */
public class ConnectState {
	
    private String device_name;
    private  String device_address;

    public static final String PREFS_NAME = "com.sayes.sayesportable.models.connect-state";

    public  static  final  String DEVICE_NAME ="device-name";
    public  static  final  String DEVICE_ADDRESS ="device-address";


    private static ConnectState state;

    private ConnectState() {
        reload();
    }

    public static ConnectState get() {

        if (state == null) {
            state = new ConnectState();
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
        device_name = prefs.getString(DEVICE_NAME, "");
        device_address = prefs.getString(DEVICE_ADDRESS, "");
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

        editor.putString(DEVICE_NAME,device_name);
        editor.putString(DEVICE_ADDRESS,device_address);
        return editor.commit();
    }

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getDevice_address() {
		return device_address;
	}

	public void setDevice_address(String device_address) {
		this.device_address = device_address;
	}

    

    
}