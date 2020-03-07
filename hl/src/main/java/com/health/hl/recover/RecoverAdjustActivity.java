package com.health.hl.recover;

/** page：用户调整页面
 *  aditor：zhou
 *  date:20170412
 *  describe:
 *  根据用户重量调整座椅的高度,使得正常坐下时压力为0,此界面可选择进入测试或者游戏.
 *  需要与下位机进行蓝牙连接
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.health.hl.R;
import com.health.hl.app.App;
import com.health.hl.app.AppManager.RFStarManageListener;
import com.health.hl.params.BLEDevice.RFStarBLEBroadcastReceiver;
import com.health.hl.params.CubicBLEDevice;
import com.health.hl.service.RFStarBLEService;
import com.health.hl.util.ToastUtils;
import com.health.hl.view.colorprogressbar;
import com.health.hl.views.AnfeiAboutDialog;
import com.health.hl.views.AnfeiAboutDialog.OnCustomDialogListener;
import com.health.hl.views.RecoverTrainlevelDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import flapbird.Flapbird;


public class RecoverAdjustActivity extends Activity implements OnClickListener,OnCheckedChangeListener,RFStarManageListener, RFStarBLEBroadcastReceiver{


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	private int trainLevel;//训练等级
	private String level; //训练等级字符串
	private int trainType;//训练模式
	private String type; //训练模式字符串
	private int trainTime;//训练时间
	private String time; //训练时间字符串
	private int lastActivity; //判断是要进行测试还是要进行训练	
	private Button nextStep;  //下一步，进入测试或者进入游戏
	private ImageView adjustBack;  //返回按钮
	private ImageView blueTooth;   //蓝牙图标
	private MediaPlayer mediaPlayer;  //语音播放
	private colorprogressbar pb_progress;       //压力条
	private static PowerManager.WakeLock wakeLock;
	
	private ProgressDialog progressDialog = null;//蓝牙搜索进度框	
	private String planLevel;           //最大训练等级
	
	//蓝牙
	private App app;          //获取上下文
	private Dialog mDialog;   //对话框
	private boolean isConnected = false;    //是否连接标记
	private final String TAG = "RecoverAdjustActivity";    //信息打印标识
	private ArrayList<BluetoothDevice> list_device;   //放置搜索到的蓝牙设备
	private ToastUtils customToast;          //消息框
	public boolean isSearch = true;     //是否在搜索标记
	private String userWeight;          //用户体重
	private String userPhone;          //用户体重
	private String maxPower;            //最大肌力
	private String mPlan;               //训练方案
	private String mPlanCode;               //训练方案
	private String userId;               //训练方案
	private Button startGame;           //开始游戏(训练)按钮
	private float a;
	
	private String userData;            //服务器返回数据存储的对象
	private TextView tv_battery;        //电量显示
	private int PCBversion = 1;         //1为新版本，0为久版本
	private float proMax = 100;         //0为新版本，1为久版本
//	private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
	private static final int PRIVATE_CODE = 1315;//开启GPS权限
    private LocationManager lm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		showGPSContacts();
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		//获取APP上下文
		app=(App)getApplication();
		//开始测试按钮   
		nextStep = (Button) findViewById(R.id.btn_recover_nextstep);
		nextStep.setOnClickListener(this);
		nextStep.setClickable(false);
		//开始训练按钮
		startGame = (Button) findViewById(R.id.btn_recover_startgame);
		startGame.setOnClickListener(this);
		startGame.setClickable(false);
		//返回按钮
		adjustBack = (ImageView) findViewById(R.id.iv_recover_adjustment_back);
		adjustBack.setOnClickListener(this);
		//蓝牙图标
		blueTooth = (ImageView) findViewById(R.id.iv_recover_adjust_bluetooth);
		blueTooth.setOnClickListener(this);
		//电池电量
		tv_battery = (TextView) findViewById(R.id.tv_adjustment_bettary);
		tv_battery.setVisibility(View.GONE);
		//读取历史数据
		reload();
	}

	private void initData() {
		// TODO Auto-generated method stub

		//获取上一界面传值,在于服务器接口对接时需要用到
		lastActivity = Integer.parseInt(this.getIntent().getStringExtra("activity"));
		userId = this.getIntent().getStringExtra("userid");
		maxPower = this.getIntent().getStringExtra("maxpower");
		mPlan = this.getIntent().getStringExtra("mplan");
		mPlanCode = this.getIntent().getStringExtra("mplancode");
		userWeight = this.getIntent().getStringExtra("userweight");
		userPhone = this.getIntent().getStringExtra("userphone");
		if(lastActivity==1)
		{
			trainLevel = 5;
			trainTime = 5;
			trainType = 1;
			level = "5";
			type = "1";
			time = "5";
		}
        //蓝牙设备列表
		list_device = new ArrayList<BluetoothDevice>();
		//压力条
		pb_progress = (colorprogressbar) findViewById(R.id.pb_recover_adjust_power);
		pb_progress.setMaxCount(100);  //最大值100
		pb_progress.setCurrentCount(0);  //最小值0
		proMax = (float) (Float.valueOf(userWeight)*0.12*9.8);
		pb_progress.setMaxCount(proMax);
//		BlueDisconnectDialog.hide();
	}

	private void initView() {
		// TODO Auto-generated method stub		
		setContentView(R.layout.activity_recover_adjustment);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// 设置不自动锁屏
		PowerManager pm = (PowerManager) getApplicationContext()
				.getSystemService(getApplicationContext().POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "==KeepScreenOn==");
		wakeLock.acquire();

		//开始搜索蓝牙
		if (app.manager.hasBle) {
			if (mDialog != null && mDialog.isShowing())
				mDialog.dismiss();
			if (!isConnected)
				if (!app.manager.isEdnabled(this)) {
					startScanBlue();
				}
		}
		super.onResume();
	}

//	/**
//	 * 创建断开提示
//	 */
//	private void createDialog(String title, String msg, String btntext) {
//		mDialog = new AlertDialog.Builder(getApplicationContext())
//				.setTitle(title)
//				.setMessage(msg)
//				.setPositiveButton(btntext,
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								// TODO Auto-generated method stub
//                                //重新搜索蓝牙
//								startScanBlue();
//								arg0.dismiss();
//							}
//
//						})
//				.setNegativeButton(
//						getApplicationContext().getResources().getString(R.string.tips_cancel),
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								// TODO Auto-generated method stub
//								arg0.dismiss();
//							}
//						}).create();
//		mDialog.show();
//	}
	
	/** fun：蓝牙扫描
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  开始蓝牙扫描，获取下位机广播
	 */
	private void startScanBlue() {
		app.manager.setRFstarBLEManagerListener(this);
		app.manager.startScanBluetoothDevice();
		startProgressDialog("正在搜索设备..");
	}
	
	/** fun：蓝牙响应
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  连接相应函数，若连接状态或者有数据传输均跳到此函数。
	 *  分为连接成功、连接断开、收发数据3种状态
	 */
	@Override
	public void onReceive(Context context, Intent intent, String macData, String uuid) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		//成功连接蓝牙
		if (RFStarBLEService.ACTION_GATT_CONNECTED.equals(action)) {
			showToast(context.getResources().getString(
					R.string.toast_connect_OK));
			isConnected = true;   
			isSearch = true;	   
		} else if (RFStarBLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
//			showToast(context.getResources().getString(
//					R.string.toast_connect_dis));  //显示断开消息框
			isConnected = false;
			pb_progress.setCurrentCount(0);
			if (isSearch) {
				isSearch = false;
				blueTooth.setBackground(getResources().getDrawable(R.drawable.icon_bluetooth_disconnect));
				startScanBlue();   //重新搜索蓝牙
			}
		} else if (RFStarBLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
			if (app.manager.cubicBLEDevice != null) {
				//读取调节高低语音
				mediaPlayer = MediaPlayer.create(this, R.raw.adjust1);
				//播放语音
				mediaPlayer.start();
				//延迟500ms后发送开始采集指令 
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice
//							.setNotification("ffe0", "ffe4", true);  //发送蓝牙"读""写"特征码
//					}
//				}, 1500);
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (app.manager.cubicBLEDevice != null)
							app.manager.cubicBLEDevice
							.setNotification("ffe0", "ffe4", true);  //发送蓝牙"读""写"特征码
					}
				}, 1000);
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (app.manager.cubicBLEDevice != null)
							app.manager.cubicBLEDevice.writeValue("ffe5", "ffe9",
									"LS01C000S".getBytes());
					}
				}, 1500);
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (app.manager.cubicBLEDevice != null)
							app.manager.cubicBLEDevice.writeValue("ffe5", "ffe9",
									"LS01A000S".getBytes());
						nextStep.setClickable(true);
						startGame.setClickable(true);
					}
				}, 2000);
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice.writeValue1(
//									"LS01C000S".getBytes());
//					}
//				}, 2500);
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice.writeValue1(
//									"LS01A000S".getBytes());
//					}
//				}, 3000);
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice.writeValue1(
//									"LS01A000S".getBytes());
//					}
//				}, 3500);
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice.writeValue1(
//									"LS01A000S".getBytes());
//					}
//				}, 4000);
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice.writeValue1(
//									"LS01A000S".getBytes());
//					}
//				}, 5000);
			    }
		} else if (RFStarBLEService.ACTION_DATA_AVAILABLE.equals(action)) {
		
            //获取下位机返回的数据
			byte[] data = intent.getByteArrayExtra(RFStarBLEService.EXTRA_DATA);
			new Handler().postDelayed(new Runnable() {						
				@Override
				public void run() {
					stopProgressDialog();   //关闭搜索框	
				}
			}, 3000);
			if(data.length == 3)
			{
				//2进制转为10进制
			a = (float)  ((data[2] & 0xFF)   
		            | ((data[1] & 0xFF)<<8)   
		            | ((data[0] & 0xFF)<<16));
			//压力值/10000
			if(PCBversion == 0)
			{
				//如果为负数则置为0
				a=a*2/1000;
				if(a>100)
					a=0;
			}
			else
			{
				a= (float) ((a*9.8)/1000);
			}
			showToast(String.valueOf((float)Math.round(a*10)/10));

            //压力条显示压力值
			pb_progress.setCurrentCount(a);
			}else if(data.length == 4)
			{
				PCBversion = 1;
				tv_battery.setVisibility(View.VISIBLE);
				a = 0;
				a = (float)  ((data[3] & 0xFF)   
			            | ((data[2] & 0xFF)<<8)   
			            | ((data[1] & 0xFF)<<16)
			            | ((data[0] & 0xFF)<<24));
				a = (float) (((a+400)/1000-3.3)/2.7)*100;
				if(a>=100)
					a=100;
				if(a<=0)
					a=0;
				int numBattery = Math.round(a);
				if(a<20)
				{
					tv_battery.setTextSize(13);
					tv_battery.setText("电量低");
				}else
				{
					tv_battery.setText(numBattery+"%");
				}
//				startGame.setClickable(true);
//				nextStep.setClickable(true);
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice.writeValue1(
//									"LS01A000S".getBytes());
//						isConnected = true;
//						isSearch = true;
//					}
//				}, 1000);
//				public void run() {
//				// TODO Auto-generated method stub
//				if (app.manager.cubicBLEDevice != null)
//					app.manager.cubicBLEDevice.writeValue1(
//							"LS01A000S".getBytes());
//				isConnected = true;
//				isSearch = true;
//			}
//		}, 4000);
//				if (app.manager.cubicBLEDevice != null)
//				app.manager.cubicBLEDevice.writeValue1(
//						"LS01A000S".getBytes());
			}
		}
	}
	
	//显示消息
//	private void showToast(String text) {
//		if (customToast == null) {
//			customToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//		} else {
//			customToast.setText(text);
//			customToast.setDuration(Toast.LENGTH_SHORT);
//		}
//		customToast.setGravity(Gravity.CENTER, 0, 0);
//		customToast.show();
//	}
	public void showToast(String text) {
//		ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
		try {
        ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
        customToast.setGravity(Gravity.CENTER, 0, 0);
//        customToast.show();
		} catch (Exception e) {}
	}
	
    //蓝牙广播接收者
	@Override
	public void RFstarBLEManageListener(BluetoothDevice device, int rssi,
			byte[] scanRecord) {
		//Log.i(TAG, device.getName() + "\n" + device.getAddress());
		if(device.getName()!=null)
		{
			//判断如果蓝牙设备名称为ls就进行连接
			int a = device.getBondState();
			if(device.getName().equals("ls"))
			{
				list_device.add(device);
				app.manager.stopScanBluetoothDevice();
//				device.getAddress();
			}
			//System.out.println(device.getName());
		}
	}
	
	/**
	 * 连接设备
	 */
	private void connectDevice(BluetoothDevice device) {
		app.manager.cubicBLEDevice = new CubicBLEDevice(
				app.manager.context, device);
		app.manager.cubicBLEDevice.setBLEBroadcastDelegate(this);
	}
	
	@Override
	public void RFstarBLEManageStartScan() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
//		Log.i(TAG, "*************开始搜索");
		// startAnimLoading();
		app.manager.isScanning=true;
		//清楚蓝牙设备列表
		this.list_device.clear();
	}
	
	/**
	 * 停止扫描
	 */
	@Override
	public void RFstarBLEManageStopScan() {
		// TODO Auto-generated method stub
//		Log.i(TAG, "*************搜索完成");
//		if (destroyflag) {
//			return;
//		}
		//停止蓝牙搜秒对话框
		stopProgressDialog();
		app.manager.isScanning = false;
		//如果未搜索到任何蓝牙设备则显示提醒对话框
		if (list_device.size() < 1) {
			//创建蓝牙断开对话框
			AnfeiAboutDialog BlueDisconnectDialog = null;//蓝牙断开提示框
			BlueDisconnectDialog = new AnfeiAboutDialog(RecoverAdjustActivity.this,"蓝牙");
			setDialogHeight(BlueDisconnectDialog);
//			BlueDisconnectDialog.show();
			BlueDisconnectDialog.setListener(new OnCustomDialogListener() {
				
				@Override
				public void back(String name) {
					// TODO Auto-generated method stub
					//点击重新搜索按钮后重新搜索蓝牙
//					if (isSearch) {
//						isSearch = false;
//						bluetooth.setBackground(getResources().getDrawable(R.drawable.img_blue_off));
						startScanBlue();
//					}
				}
			});
			BlueDisconnectDialog.show();
//			BlueDisconnectDialog.show();
			return;
		}
		//如果设备列表不为空则进行设备连接
		for (int i = 0; i < list_device.size(); i++) {
			//System.out.println(list_device.get(i).getName());
			if (list_device.get(i).getName()
					.equals("ls")) {
				connectDevice(list_device.get(i));
				//设置蓝牙背景状态
				blueTooth.setBackground(getResources().getDrawable(R.drawable.connect));
				return;
			}
		}
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.btn_recover_nextstep: //开始测试
			nextStep.setClickable(false);
			if(app.manager.isScanning){
				app.manager.stopScanBluetoothDevice();
			}
			if (app.manager.cubicBLEDevice != null) {
				if (app.manager.cubicBLEDevice.isConnect) {
//					manager.cubicBLEDevice.closeDevice();
					isSearch=false;
					app.manager.cubicBLEDevice.disconnectedDevice();
					isConnected=false;					
				}
//				app.manager.cubicBLEDevice=null;
			}
			app.flag = true;
//			isDialogShow = false;
			if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			}
			new Handler().postDelayed(new Runnable() {						
				@Override
				public void run() {
					// TODO Auto-generated method stub
					nextStep.setClickable(true);
					Intent intent1 = null;
					intent1 = new Intent(RecoverAdjustActivity.this,
							RecoverTrainActivity.class);
					System.out.println("trainlevel:"+trainLevel);
					lastActivity = 1;
					//把训练等级、计划等级、训练时间、状态、压力基准值、用户ID传到下一个界面
					intent1.putExtra("levelvalue",String.valueOf(trainLevel));
					intent1.putExtra("Highestlevel",planLevel);
					intent1.putExtra("timevalue",String.valueOf(trainTime));
					intent1.putExtra("lastactivity",String.valueOf(lastActivity));
					intent1.putExtra("adjustpower",String.valueOf(a));
					intent1.putExtra("userid",userId);
					intent1.putExtra("PCBversion",String.valueOf(PCBversion));
					intent1.putExtra("userweight",userWeight);
					intent1.putExtra("userphone",userPhone);//晶
					startActivity(intent1);
				}
			}, 1000);
			break;
		case R.id.btn_recover_startgame: //开始训练
			if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			}
			if(!maxPower.equals("-"))
			{
				RecoverTrainlevelDialog TrainlevelDialog = null;
				//弹出设置训练等级和训练时间对话框
				TrainlevelDialog = new RecoverTrainlevelDialog(RecoverAdjustActivity.this,
						mPlan);
				setDialogHeight(TrainlevelDialog);
				TrainlevelDialog.trainlevel = trainLevel;
				TrainlevelDialog.traintime = trainTime;
				TrainlevelDialog.traintype = trainType;
				TrainlevelDialog.setListener(new com.health.hl.views.RecoverTrainlevelDialog.OnCustomDialogListener() {

							@Override
							public void back(String name) {
								// TODO Auto-generated method stub
								// retest();
								reload1();
								if(app.manager.isScanning){
									app.manager.stopScanBluetoothDevice();
								}
								if (app.manager.cubicBLEDevice != null) {
									if (app.manager.cubicBLEDevice.isConnect) {
//										manager.cubicBLEDevice.closeDevice();
										isSearch=false;
										app.manager.cubicBLEDevice.disconnectedDevice();
										isConnected=false;
										
									}
//									app.manager.cubicBLEDevice=null;
								}
//								app.flag = true;
								if(trainType == 1)
								{
									new Handler().postDelayed(new Runnable() {						
										@Override
										public void run() {
									Intent intent1 = null;
									intent1 = new Intent(RecoverAdjustActivity.this,
											Flapbird.class);
									//把训练方案、训练等级、计划等级、训练时间、状态、压力基准值、用户ID传到下一个界面
									intent1.putExtra("levelvalue",String.valueOf(trainLevel));
									intent1.putExtra("Highestlevel",String.valueOf(trainLevel));
									intent1.putExtra("timevalue",String.valueOf(trainTime));
									intent1.putExtra("adjustpower",String.valueOf(a));
									intent1.putExtra("maxpower",maxPower);
									intent1.putExtra("mplan",mPlan);
									intent1.putExtra("mplancode",mPlanCode);
									intent1.putExtra("userid",userId);
									intent1.putExtra("PCBversion",String.valueOf(PCBversion));
									intent1.putExtra("userweight",userWeight);
	//								intent1.putExtra("lastactivity",String.valueOf(lastactivity));
									startActivity(intent1);
										}
									}, 1000);
								}else
								{
									app.flag = true;
									new Handler().postDelayed(new Runnable() {						
										@Override
										public void run() {
									Intent intent1 = null;
									intent1 = new Intent(RecoverAdjustActivity.this,
											RecoverTrainActivity.class);
									lastActivity = 2;
									//把训练方案、训练等级、计划等级、训练时间、状态、压力基准值、用户ID传到下一个界面
									intent1.putExtra("levelvalue",String.valueOf(trainLevel));
									intent1.putExtra("Highestlevel",String.valueOf(trainLevel));
									intent1.putExtra("timevalue",String.valueOf(trainTime));
									intent1.putExtra("lastactivity",String.valueOf(lastActivity));
									intent1.putExtra("adjustpower",String.valueOf(a));
									intent1.putExtra("maxpower",maxPower);
									intent1.putExtra("mplan",mPlan);
									intent1.putExtra("mplancode",mPlanCode);
									intent1.putExtra("userid",userId);
									intent1.putExtra("PCBversion",String.valueOf(PCBversion));
									intent1.putExtra("userweight",userWeight);
									intent1.putExtra("userphone",userPhone);//晶
									startActivity(intent1);
										}
									}, 1000);
								}
							}
						});
				TrainlevelDialog.show();
			}else
			{
				showToast("尚未进行肌力测试，无法进行训练");
			}
			break;
		case R.id.iv_recover_adjustment_back:  //返回上一页
			adjustBack.setClickable(false);
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
				}
			if(app.manager.isScanning){
				app.manager.stopScanBluetoothDevice();
			}
			if (app.manager.cubicBLEDevice != null) {
				if (app.manager.cubicBLEDevice.isConnect) {
//					manager.cubicBLEDevice.closeDevice();
					isSearch=false;
					app.manager.cubicBLEDevice.disconnectedDevice();
					isConnected=false;
					
				}
//				app.manager.cubicBLEDevice=null;
			}
			new Handler().postDelayed(new Runnable() {						
				@Override
				public void run() {
			Intent intent2 = null;
			intent2 = new Intent(RecoverAdjustActivity.this,
						RecoverMainActivity.class);
			startActivity(intent2);
			finish();
				}
			}, 1000);
			break;	
		case R.id.iv_recover_adjust_bluetooth: //点击开始扫描蓝牙
			if (app.manager.hasBle) {
				if (mDialog != null && mDialog.isShowing())
					mDialog.dismiss();
				if (!isConnected)
					if (!app.manager.isEdnabled(this)) {
						app.manager.setRFstarBLEManagerListener(this);
						app.manager.startScanBluetoothDevice();
						startProgressDialog("正在搜索设备..");
					}
			}
			break;
		default:
			break;
		}
	}
	
	//函数暂无作用
    @Override  
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
    	switch(buttonView.getId())
    	{
    	default:
    	break;
    	}
    } 
	


    /** fun：广播处理
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  处理接收到的广播
	 */
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(RFStarBLEService.ACTION_GATT_CONNECTED)) {
				blueTooth.setBackground(getResources().getDrawable(R.drawable.connect));
			}else if (intent.getAction().equals(RFStarBLEService.ACTION_GATT_DISCONNECTED)) {
				blueTooth.setBackground(getResources().getDrawable(R.drawable.icon_bluetooth_disconnect));
			}
		}

	}
	

	
	//读取当前训练等级
	public void ButtonEnable()
	{
		SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );
        //读取当前训练等级
        System.out.println("planlevel:"+planLevel);
	}
	
	//重设按钮,已弃用
	private void ButtonReset()
	{
		SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );
       
        planLevel = prefs.getString("trainlevel", "1");
        //trainlevel = Integer.parseInt(planlevel);
        System.out.println("planlevel:"+planLevel);
	}
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
//		app.manager.setRFstarBLEManagerListener(null);
//		destroyflag = true;
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}	
		if(app.manager.isScanning){
			app.manager.stopScanBluetoothDevice();
		}
		if (app.manager.cubicBLEDevice != null) {
			if (app.manager.cubicBLEDevice.isConnect) {
//				manager.cubicBLEDevice.closeDevice();
				isSearch=false;
				app.manager.cubicBLEDevice.disconnectedDevice();
				isConnected=false;
				
			}
			app.manager.cubicBLEDevice=null;
		}	
		super.onDestroy();
	}
	
	/**
	 * 设置dialog高度
	 * 
	 * @param dialog
	 */
	private void setDialogHeight(Dialog dialog) {
		Window dialogWindow = dialog.getWindow();
		WindowManager m = dialogWindow.getWindowManager();
		Display d = m.getDefaultDisplay();
		android.view.WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// lp.width = 300; // 宽度
//        lp.width = 200;
		lp.height = (int) (d.getHeight() * 0.4); // 高度
		dialogWindow.setAttributes(lp);
	}
	
	/**
	 * 开始蓝牙搜索dialog
	 */
	private void startProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setCancelable(true);
			progressDialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface arg0) {

				}
			});

		}
		if (progressDialog.isShowing()) {
			progressDialog.setMessage(msg);
			return;
		}
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(msg);
		progressDialog.show();
	}
	
	/**
	 * 停止蓝牙搜索dialog
	 */
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	public void reload() {
        SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );       
       userWeight  = prefs.getString("weight", "0");
//       maxpower = prefs.getString("maxpower", "-");
//       mplan = prefs.getString("trainlevel", "-");    
       planLevel = prefs.getString("trainlevel", "5");
       trainType = Integer.parseInt(prefs.getString("traintype", "0"));
       trainTime = Integer.parseInt(prefs.getString("traintime","5"));
       trainLevel = Integer.parseInt(planLevel);
//       traintype = Integer.parseInt(prefs.getString("traintype", "1"));
//       showToast(userid);
       //读取网络数据
//       new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					userData = getJson("http://119.23.248.54/Ls_Server_web/MsTrain/findMsTrainHabitByuId?uId="+userId);
////					if(!userdata.equals("wrong"))
//					//数据解析
//					datashow(userData);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//				}
//			}
//		}).start();
//       new Handler().postDelayed(new Runnable() {						
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				//赋值
//				trainLevel = Integer.parseInt(level);
//				trainTime = Integer.parseInt(time);
//				trainType = Integer.parseInt(type);
//			}
//		}, 2000);
    }
	
	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	public void reload1() {
        SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );       
       userWeight  = prefs.getString("weight", "0");
//       maxpower = prefs.getString("maxpower", "-");
//       mplan = prefs.getString("trainlevel", "-");    
       planLevel = prefs.getString("trainlevel", "1");
//       traintype = Integer.parseInt(prefs.getString("traintype", "1"));
       trainTime = Integer.parseInt(prefs.getString("traintime","5"));
       trainLevel = Integer.parseInt(planLevel);
       trainType = Integer.parseInt(prefs.getString("traintype", "1"));
//       showToast(userid);
       //读取网络数据
    }
    /**
	 * fun：向服务器获取存储信息 aditor：zhou date:20170911 describe: path为接口网址，使用的GET方式，
	 */
    public String getJson(String path) throws Exception{
        	int a = 0;
//        	path = "http://119.23.248.54/Ls_Server_web/userinfo/findUserByPhone?phone=13696026440";
            String inputLine = null;
        	//建立连接
        	URL url = new URL(path);
        	//打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接超时
            conn.setConnectTimeout(5000);
            //设置为POST模式
            conn.setRequestMethod("GET");
            //读取服务器返回码,若为200则与服务器连接成功
            a = conn.getResponseCode();
            if(a == 200)
            {
	                //获取服务器返回信息
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            inputLine = reader.readLine();
	            reader.close();
            }else
            {
            	userData = "";
            	return "wrong";
            }
//            a = conn.getResponseCode();
            return inputLine;
    }
	
    /**
	 * fun：服务器返回数据 aditor：zhou date:20170911 describe: 使用遍历方式,data为服务器返回的数据
	 */
    private void datashow(String data)
    {
		trainLevel = 1;
    	int a=0;
    	a = data.indexOf("trainLevel");
    	a += 11;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					level = data.charAt(i)+"";
    				else
    					level += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
//    	trainTime = 5;
    	a = data.indexOf("trainTime");
    	a += 10;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					time = data.charAt(i)+"";
    				else
    					time += (int)data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = data.indexOf("trainMode");
    	a += 10;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					type = data.charAt(i)+"";
    				else
    					type += (int)data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    }
    
//    返回键处理
  @Override
  public void onBackPressed() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}	
		if(app.manager.isScanning){
			app.manager.stopScanBluetoothDevice();
		}
		if (app.manager.cubicBLEDevice != null) {
			if (app.manager.cubicBLEDevice.isConnect) {
//				manager.cubicBLEDevice.closeDevice();
				isSearch=false;
				app.manager.cubicBLEDevice.disconnectedDevice();
				isConnected=false;
				
			}
			app.manager.cubicBLEDevice=null;
		}	
       super.onBackPressed();//注释掉这行,back键不退出activity
//      showToast("返回键");
//  	this.finish();
  }
  /**
   * 检测GPS、位置权限是否开启
   */
  public void showGPSContacts() {
      lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
      boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
      if (ok) {//开了定位服务
          if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
              if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                      != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
                  ActivityCompat.requestPermissions(this, new String []{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
              } else {
//                  getLocation();//getLocation为定位方法
              }
          } else {
//              getLocation();//getLocation为定位方法
          }
      } else {
          Toast.makeText(this, "系统检测到未开启GPS定位服务,请开启", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent();
          intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivityForResult(intent, PRIVATE_CODE);
      }
  }



  
}

