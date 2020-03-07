package com.health.hl.http;

import android.content.Context;
import android.content.SharedPreferences;

import com.health.hl.app.App;


/**
 * Created by xavi
 * 2014/10/15.
 */
public class HostProfile {

    public static final String DEFAULT_HOST_IP = "120.24.178.16"; //"120.24.178.16""192.168.4.110" beta.cifpay.com 192.168.0.23
    
    public static final int DEFAULT_HOST_PORT = 8080;//80 //8080 //6080
    public static final String BASE_PATH = "Health";

    public static final String PREFS_NAME = "com.sayes.host-profile";

    public static final String KEY_IP = "ip";
    public static final String KEY_PORT = "port";

    private static HostProfile instance;

    private HostProfile() {
        reload();
    }

    public static HostProfile getInstance() {

        if (instance == null) {
            instance = new HostProfile();
        }
        return instance;
    }

    public void reload() {
        SharedPreferences prefs = App.get().getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );

        ip = prefs.getString(KEY_IP, DEFAULT_HOST_IP);
        port = prefs.getInt(KEY_PORT, DEFAULT_HOST_PORT);
    }

    public boolean save() {

        SharedPreferences prefs = App.get().getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(KEY_IP, ip);
        editor.putInt(KEY_PORT, port);
        return editor.commit();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBaseUrl() {
//        return "http://" + getIp()  + "/" + BASE_PATH;//+ ":" + getPort()
        return "http://" + getIp() + ":" + getPort() + "/" + BASE_PATH;//+ ":" + getPort()
    }
    
    public String getHost() {
        return "http://" + getIp() + ":" + getPort() + "/";
    }

    private String ip;
    private int port;
}
