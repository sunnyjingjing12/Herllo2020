package com.health.hl.app;


import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.health.hl.bean.HosUser;
import com.health.hl.entity.PrUserInfoVo;
import com.health.hl.entity.UsertTrainStandard;
import com.health.hl.util.ImageLoaderUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class App extends Application {
	private static Application app;
//	public static final int BLUETOOTH_DATA_CHANNEL = 0; // 蓝牙数据通道 ffe5
//	public static final int SERIAL_DATA_CHANNEL = 1; // 串口数据通道 ffe0
//	public static final int ADC_INPUT = 2; // adc输入（2路）ffd0
//	public static final int RSSI_REPORT = 3; // RSSI报告 ffa0
//	public static final int PWM_OUTPUT = 4; // pwm输出（4路）ffb0
//	public static final int BATTERY_REPORT = 5; // 电池电量报告 180f
//
//	public static final int TURNIMING_OUTPUT = 6; // 定时翻转输出 fff0
//	public static final int LEVEL_COUNTING_PULSE = 7; // 电平脉宽计数 fff0
//	public static final int PORT_TIMING_EVENTS_CONFIG = 8;// 端口定时事件配置 fe00
//	public static final int PROGRAMM_ABLEIO = 9; // 可编程io(8路) fff0
//	public static final int DEVICE_INFORMATION = 10; // 设备信息 180a
//	public static final int MODULE_PARAMETER = 11; // 模块参数设置 ff90
//	public static final int ANTI_HIJACKINGKEY = 12;// 防劫持密钥 ffc0

	public static final String RFSTAR_DEVICE = "rfstar_device";
	public static final String DEVICE_NAME="UmedSSS-";
	public static final String DEVICE_NAME_BP="Bluetooth";
	public static final String DEVICE_NAME_ANFEI="SYSdasda";//HMSoft
	public static final String TAG = "_TAG";
	public static IWXAPI wxApi ;
	public static final String APPID = "wx1059de6acd03653c";//wx1059de6acd03653c
	public static final String AppSecret = "d4624c36b6795d1d99dcf0547af5443d";//d4624c36b6795d1d99dcf0547af5443d
	public AppManager manager = null;
	public  HosUser  hosUser;
	private PrUserInfoVo activeUser; //当前用户
	private static Context context;
	private float adjustPower; //基准压力值
	private UsertTrainStandard activeUserTrainStandard; 
	public Set<Integer> senduids;
	public boolean flag;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		disableAPIDialog();
		app=this;
		ImageLoaderUtil.init(this);
		wxApi = WXAPIFactory.createWXAPI(this,APPID);
		wxApi.registerApp(APPID);
		manager = new AppManager(getApplicationContext());
		context = getApplicationContext();
	}

	private void disableAPIDialog(){
		if (Build.VERSION.SDK_INT < 28)return;
		try {
			Class clazz = Class.forName("android.app.ActivityThread");
			Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
			currentActivityThread.setAccessible(true);
			Object activityThread = currentActivityThread.invoke(null);
			Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
			mHiddenApiWarningShown.setAccessible(true);
			mHiddenApiWarningShown.setBoolean(activityThread, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Application get() {
        return app;
    }
	public HosUser getHosUser() {
		return hosUser;
	}
	
	public void setHosUser(HosUser hosUser) {
		this.hosUser = hosUser;
	}
	
	  /**
     * 获取全局上下文*/
    public static Context getContext() {
        return context;
    }
	
	public PrUserInfoVo getActiveUser() {
		return activeUser;
	}
	
	public void setActiveUser(PrUserInfoVo activeUser) {
		this.activeUser = activeUser;
	}
	
	public float getAdjustPower() {
		return adjustPower;
	}
	
	public void setAdjustPower(float adjustPower) {
		this.adjustPower = adjustPower;
	}
	
	public UsertTrainStandard getActiveUserTrainStandard() {
		return activeUserTrainStandard;
	}
	
	public void setActiveUserTrainStandard(
			UsertTrainStandard activeUserTrainStandard) {
		this.activeUserTrainStandard = activeUserTrainStandard;
	}
	
	
	
}
