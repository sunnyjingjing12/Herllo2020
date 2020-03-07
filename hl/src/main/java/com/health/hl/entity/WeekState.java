package com.health.hl.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.health.hl.app.App;


/**
 * Created by xavi
 * 2014/10/21
 */
public class WeekState {
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
	private String tagNum;
	

    public static final String PREFS_NAME = "com.haoshamain.week-state";

    public static final String MON = "mon";
    public  static  final  String TUE ="tue";
    public  static  final  String WED ="wed";
    public static final String THU = "thu";
    public  static  final  String FRI ="fri";
    public  static  final  String SAT ="sat";
    public  static  final  String SUN ="sun";
    public  static  final  String TAGNUM ="tagnum";
    private static WeekState state;

    private WeekState() {
        reload();
    }

    public static WeekState get() {

        if (state == null) {
            state = new WeekState();
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

        mon = prefs.getString(MON, "0");
        tue = prefs.getString(TUE, "0");
        wed = prefs.getString(WED, "0");
        thu = prefs.getString(THU, "0");
        fri = prefs.getString(FRI, "0");
        sat = prefs.getString(SAT, "0");
        sun = prefs.getString(SUN, "0");
        tagNum= prefs.getString(TAGNUM, "0");
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

        editor.putString(MON,mon);
        editor.putString(TUE,tue);
        editor.putString(WED,wed);
        editor.putString(THU,thu);
        editor.putString(FRI,fri);
        editor.putString(SAT,sat);
        editor.putString(SUN,sun);
        editor.putString(TAGNUM,tagNum);
        return editor.commit();
    }

	public String getTagNum() {
		return tagNum;
	}

	public void setTagNum(String tagNum) {
		this.tagNum = tagNum;
	}

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