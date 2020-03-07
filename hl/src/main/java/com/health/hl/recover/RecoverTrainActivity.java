package com.health.hl.recover;

/** page：用户测试/训练页面
 *  aditor：zhou
 *  date:20170412
 *  describe:
 *  用户肌力测试和盆底训练复用此界面，通过lastactivity1标记进行区分
 *  1、肌力测试
 *  首先测试出用户的最大肌力,然后根据最大肌力和测试波形测试用户的肌力等级
 *  测试时间为5分钟,对于每一类肌肉测试5次,取平均值
 *  I类肌维持秒数表示用户I类肌等级,训练强度为最大肌力60%
 *  II类肌快速收放次数表示用户II类肌等级,训练强度为最大肌力60%
 *  2、盆地训练
 *  有4种训练波形，训练强度根据用户选择的训练等级不同而不同
 *  训练时统计总的收缩时间和有效的收缩次数
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.health.hl.R;
import com.health.hl.app.App;
import com.health.hl.app.AppManager.RFStarManageListener;
import com.health.hl.params.BLEDevice.RFStarBLEBroadcastReceiver;
import com.health.hl.params.CubicBLEDevice;
import com.health.hl.service.RFStarBLEService;
import com.health.hl.util.ToastUtils;
import com.health.hl.views.AnfeiAboutDialog;
import com.health.hl.views.RecoverReportDialog;
import com.health.hl.views.RecoverReportDialog.OnCustomDialogListener;
import com.health.hl.views.RecoverTrainDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class RecoverTrainActivity extends Activity implements OnClickListener,RFStarManageListener, RFStarBLEBroadcastReceiver {

	private LineChart recoverChart;   //训练波形图
	private Handler mHandler;         //定时线程
	private int trainLevel;           //训练等级,暂无使用
	private ToastUtils customToast;        //消息框
	private int slowCount = 0;        //I类肌计数
	private int quickCount = 0;       //II类肌计数
	private float quickData[] = new float[5];// 压力数据存档
	private float slowData[] = new float[5];// 压力数据存档
	private int quickLevel = 0; // 快肌等级
	private boolean quickLevelFirstTime = true; // 快肌等级
	private int slowLevel; // 慢肌等级
	private int trainScore; // 训练评分
	private String trainPlan; // 训练方案
	private String mPlan; // 训练方案
	private String mPlanCode; // 训练方案编码
	private String userId; // 用户id
	private String userWeight; // 用户体重 20190826
	private String userPhone; // 用户手机号  20190826
	private String netPath;//接口网址
	private String netPara;//接口参数
	private String code; //网络返回码
	private boolean netFlag = false;
	private boolean testMuscleFlag = false;  //测试肌肉波形切换
	private boolean firstRemainFlag = true; //是否为第一次打开APP标记
	private String filename = "userinfo";    //本地SP文件名称
	public static final String IMLEVEL = "Imlevel";   //I类肌等级
	public static final String IIMLEVEL = "IImlevel";  //II类肌等级
	public static final String MAXPOWER = "maxpower";  //最大肌力
	public static final String TRAINPLAN = "trainplan";  //II类肌等级
	public static final String SOUND = "sound";         //声音标记
	public static final String DESCRIBE = "describe";   //是否已提醒用户使用方法标记
	public static final String QUICK1 = "quick1";       //第一次II类肌测试结果
	public static final String QUICK2 = "quick2";		//第二次II类肌测试结果
	public static final String QUICK3 = "quick3";		//第三次II类肌测试结果
	public static final String QUICK4 = "quick4";		//第四次II类肌测试结果
	public static final String QUICK5 = "quick5";		//第五次II类肌测试结果
	public static final String SLOW1 = "slow1";			//第一次I类肌测试结果
	public static final String SLOW2 = "slow2";			//第二次I类肌测试结果
	public static final String SLOW3 = "slow3";			//第三次I类肌测试结果
	public static final String SLOW4 = "slow4";			//第四次I类肌测试结果
	public static final String SLOW5 = "slow5";			//第五次I类肌测试结果
	public static final String HISTROY1_I = "histroy1_I";	//第一次I类肌历史记录
	public static final String HISTROY2_I = "histroy2_I";	//第二次I类肌历史记录
	public static final String HISTROY3_I = "histroy3_I";	//第三次I类肌历史记录
	public static final String HISTROY4_I = "histroy4_I";	//第四次I类肌历史记录
	public static final String HISTROY5_I = "histroy5_I";	//第五次I类肌历史记录
	public static final String HISTROY1_II = "histroy1_II"; //第一次II类肌历史记录
	public static final String HISTROY2_II = "histroy2_II"; //第二次II类肌历史记录
	public static final String HISTROY3_II = "histroy3_II";	//第三次II类肌历史记录
	public static final String HISTROY4_II = "histroy4_II";	//第四次II类肌历史记录
	public static final String HISTROY5_II = "histroy5_II";	//第五次II类肌历史记录
	public static final String LASTDATA = "lastdata";       //总共已有多少个肌力历史记录

	// 定义Y坐标轴阈值,已弃用
	private static final Float levelYAxis_1 = (float) 0.6;
	private static final Float levelYAxis_2 = (float) 1.2;
	private static final Float levelYAxis_3 = (float) 2.5;
	private static final Float levelYAxis_4 = (float) 5.0;
	private static final Float levelYAxis_5 = (float) 6.0;
	private static final Float levelYAxis_6 = (float) 10.0;
	private static final Float levelYAxis_7 = (float) 12.0;
	private static final Float levelYAxis_8 = (float) 15.0;
	private static final Float levelYAxis_9 = (float) 20.0;
	private static final Float levelYAxis_10 = (float) 25.0;

	// 定义不同等级Y阈值,已弃用
	private static final Float levelY_1 = (float) 0.33;
	private static final Float levelY_2 = (float) 0.66;
	private static final Float levelY_3 = (float) 1.3;
	private static final Float levelY_4 = (float) 2.6;
	private static final Float levelY_5 = (float) 3.9;
	private static final Float levelY_6 = (float) 5.9;
	private static final Float levelY_7 = (float) 7.9;
	private static final Float levelY_8 = (float) 9.9;
	private static final Float levelY_9 = (float) 11.9;
	private static final Float levelY_10 = (float) 14.4;

	// 波形种类
	private int type = 1;// 训练波形
	private int[] trainTypeSet = new int[10];// 训练波形方案波形组
	private int trainTypeCount = 0; //训练模式下第几个波形
	private float highTypeSet = 1;  //高波模式下的倍数
	private int typeCount = 0;// 训练波形重复次数计数
	private int typecountmodel = 2;// 训练波形重复模板次数
	private int beginCount = 0;// 开始准备三秒
	private boolean beginEndFlag = false;   //首次进入界面标记
	private boolean typeChoose = false;     //训练波形在1和6之间切换标记
	private int trainTime = 0;// 训练时间
	private float powerData = 0;// 下位机上传的压力值
	private float[] powerDataSet= new float[300];// 下位机上传的压力值
	private float maxPower = 0;// 最大压力值
	private float powerPara = (float) 0.4;// 训练力度系数
	private float lastMaxPower = 0;// 最大压力值
//	private float maxpower1 = 0;// 最大压力值
	private int timeCount = 0;// 训练时间计算
	private int minute = 0;// 计算剩余分钟；
	private int second = 0;// 计算剩余秒
	private int secondCount = 0;// 用于计数，每计数10次为一秒
	private int durationCount = 0;// 持续时间计算
	private int durationCount_train = 0;// 持续时间计算,用于肌力训练
	private int num_train = 0;// 收放次数计算,用于肌力训练、
	private int maxPowerCount = 1;// 用于计算最大肌力，采集2次即进入测试
	private float time_MuscleI = 0;// 计算I类肌的持续时间
//	private int time_MuscleII = 0;// 计算二类肌的次数
	private int addProgress = 0;// 计算多少秒加1%进度
	private int nowProgress = 0;// 当前进度的百分比
	private int progressCount = 0;// 增加百分比计数
	private ImageView pageBack;// 返回按钮
	private TextView show_maxPower;// 最大压力
	private TextView show_nowPower;// 当前压力
	private TextView show_duration;// 持续时间
//	private TextView show_nowscore;// 训练评分
	private float allPowerData[] = new float[300];// 压力数据存档
	private TextView show_nowMinute;// 剩余时间（分钟）
	private TextView show_nowSecond;// 剩余时间（秒）
	private ProgressBar timeProgress;// 剩余时间进度条
	private MediaPlayer mediaPlayer; // 语音
	private boolean MediaPlayflag = true;  //是否播放语音标记
	private boolean MediaPlayingflag = false;  //是否正在播放语音标记
	private int lastActivity = 1;   //采集最大肌力和测试波形切换标记
	private int lastActivity1;   //区分是测试还是训练的标记
	private boolean timeADCflag = false;//是否计数持续时间标记，当模板波形要求有收缩时实际收缩才计入有效的持续
	private ImageView blueTooth;    //蓝牙标记
	private static PowerManager.WakeLock wakeLock;	
	private ProgressDialog progressDialog = null;//蓝牙搜索进度框
	
	//训练模式
	private int contractionTime;           //I类肌收缩时间目标值
	private int contractionNum;           //II类肌收缩次数目标值

	//蓝牙
	private App app;
	private Dialog mdialog;
	private boolean isConnected = false;
//	private final String TAG = "RecoverAdjustActivity";
	private ArrayList<BluetoothDevice> list_device; // 放置搜索到的蓝牙设备
	public boolean isSearch = true;    //是否在搜索
	private ImageView show_sound;      //静音图标
	private TextView show_frequency;   //II类肌收缩次数
	private LinearLayout show_nowPower_ll;  //训练模式下把部分文字显示隐藏
	private TextView show_nowPower_title;   //训练模式下修改当前肌力标题
	private TextView show_maxPower_title;   //训练模式下修改最大肌力标题
	private TextView show_duration_title;	//训练模式下修改持续时间标题
	private TextView show_frequency_title;  //训练模式下修改收缩次数标题
	private TextView show_maxpower_unit;    //训练模式下修改最大肌力单位
	private float adjustPower;			//压力基准值
	private TextView show_title;       //页面标题
	private int PCBversion = 0;
	private LinearLayout show_con_ll;
	private LinearLayout show_goal_title_ll;
	private LinearLayout show_goal_ll;
	private TextView show_goalI;
	private TextView show_goalII;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	@Override
	protected void onDestroy() {
//		mHandler.removeCallbacks(runnable);
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if(!app.flag) {
			if(app.manager.isScanning){
				app.manager.stopScanBluetoothDevice();
			}
		}
		if (app.manager.cubicBLEDevice != null) {
			if (app.manager.cubicBLEDevice.isConnect) {
				app.manager.cubicBLEDevice.disconnectedDevice();
				isConnected=false;
			}
			app.manager.cubicBLEDevice=null;
		}
		if(mHandler != null)
		{
			mHandler.removeCallbacks(runnable);
			mHandler = null;
		}
//		if(BlueDisconnectDialog != null)
//		{
//			BlueDisconnectDialog = null;
//		}
		super.onDestroy();
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
		if(!firstRemainFlag)
		{
			if (app.manager.hasBle) {
				if (mdialog != null && mdialog.isShowing())
					mdialog.dismiss();
				if (!isConnected)
					if (!app.manager.isEdnabled(this)) {
						app.manager.startScanBluetoothDevice();
						startProgressDialog("正在搜索设备");
					}
			}
		}
		super.onResume();
	}

	private void initView() {
		setContentView(R.layout.activity_recover_train);
	}

	private void initData() {
		//初始化压力值
		powerData = (float) 0;   
		//线性表格
		recoverChart = (LineChart) findViewById(R.id.line_recover_chart);
		//返回按钮
		pageBack = (ImageView) findViewById(R.id.iv_recover_train_back);
		pageBack.setOnClickListener(this);

		//蓝牙标记
		blueTooth = (ImageView) findViewById(R.id.iv_recover_train_bluetooth);
		blueTooth.setOnClickListener(this);
		//是否静音显示
		show_sound = (ImageView) findViewById(R.id.iv_recover_train_sound);
		show_sound.setOnClickListener(this);
		//最大肌力显示
		show_maxPower = (TextView) findViewById(R.id.tv_recover_maxpower);
		//最大肌力标题，用户在波形训练时要切换显示
		show_maxPower_title = (TextView) findViewById(R.id.tv_recover_max);
		//最大肌力单位标题，用户在波形训练时要切换显示
		show_maxpower_unit = (TextView) findViewById(R.id.tv_recover_maxkgf);
		//当前肌力标题
		show_nowPower_title = (TextView) findViewById(R.id.tv_recover_now);
		//当前肌力显示
		show_nowPower = (TextView) findViewById(R.id.tv_recover_nowpower);
		//当前肌力显示线性布局，用户在波形训练时隐藏显示
		show_nowPower_ll = (LinearLayout) findViewById(R.id.ll_recover_nowpower);
		//持续时间
		show_duration = (TextView) findViewById(R.id.tv_recover_duration);
		//持续时间标题，用户在波形训练时要切换显示
		show_duration_title = (TextView) findViewById(R.id.tv_recover_con);
		//当前次数
		show_frequency = (TextView) findViewById(R.id.tv_recover_frequency);
		//最大肌力标题，用户在波形训练时要切换显示
		show_frequency_title = (TextView) findViewById(R.id.tv_recover_num);

		//剩余时间分钟
		show_nowMinute = (TextView) findViewById(R.id.tv_recover_train_minute);
		//剩余时间秒钟
		show_nowSecond = (TextView) findViewById(R.id.tv_recover_train_second);
		//页面标题
		show_title = (TextView) findViewById(R.id.tv_recover_train_title);
		//当为训练时肌力值显示去掉
//		show_power = (RelativeLayout) findViewById(R.id.rl_recover_power);
		timeProgress = (ProgressBar) findViewById(R.id.pb_recover_train_traintime);
		//当前肌力显示线性布局，用户在波形训练时隐藏
		show_con_ll = (LinearLayout) findViewById(R.id.ll_recover_con);
		//当前目标值显示线性布局，用户在波形训练时显示
		show_goal_title_ll = (LinearLayout) findViewById(R.id.ll_recover_goal_title);
//		show_goal_ll = (LinearLayout) findViewById(R.id.ll_recover_goal);
		//I类肌时间收缩时间目标值
		show_goalI = (TextView) findViewById(R.id.tv_recover_goalI);
		show_goalII = (TextView) findViewById(R.id.tv_recover_goalII);
		//读取传入数据
		trainLevel = Integer.parseInt(this.getIntent().getStringExtra(
				"levelvalue"));
		trainTime = Integer.parseInt(this.getIntent().getStringExtra(
				"timevalue"));
		lastActivity1 = Integer.parseInt(this.getIntent().getStringExtra(
				"lastactivity"));
		adjustPower = Float.parseFloat(this.getIntent().getStringExtra(
				"adjustpower"));
		userId = this.getIntent().getStringExtra("userid");
		userWeight = this.getIntent().getStringExtra(
				"userweight");
		userPhone = this.getIntent().getStringExtra(
				"userphone");
		PCBversion = Integer.parseInt(this.getIntent().getStringExtra(
				"PCBversion"));
//		showToast(userid);
		//重新读取数据
		reLoad();
	    //测试
		if (lastActivity1 == 1) {
			//播放测试语音
			mediaPlayer = MediaPlayer.create(this, R.raw.daoshu);
			trainTime = 5;    //默认测试5分钟
			if(PCBversion == 1)
				trainLevel = 9;   //默认测试强度为9级,即最大肌力默认20kgf
			else
				trainLevel = 6;   //默认测试强度为6级,即最大肌力默认10kgf
			maxPower = 0;
			show_maxPower.setText(String.valueOf(maxPower));
			show_title.setText("肌力测试");
			//初始化图表
			initChart(trainLevel);     
			setData(0, 0, trainLevel);
			recoverChart.animateX(0);
		}
		//训练
		else {
			maxPower = Float.parseFloat(this.getIntent().getStringExtra(
					"maxpower"));
			mPlan = this.getIntent().getStringExtra("mplan");
			mPlanCode = this.getIntent().getStringExtra("mplancode");
			//播放训练语音
			mediaPlayer = MediaPlayer.create(this, R.raw.begintrain);
			show_title.setText("肌力训练");
			//根据训练等级不同,设置不同的训练难度,即占最大肌力的比例不同
			switch (trainLevel) {
			case 1:  //40%
				powerPara = (float) 0.4;
				break;
			case 2:  //50%
				powerPara = (float) 0.5;
				break;
			case 3:  //60%
				powerPara = (float) 0.6;
				break;
			case 4:  //70%
				powerPara = (float) 0.7;
				break;
			case 5:  //75%
				powerPara = (float) 0.75;
				break;
			case 6:  //80%
				powerPara = (float) 0.8;
				break;
			case 7:  //85%
				powerPara = (float) 0.85;
				break;
			case 8:  //90%
				powerPara = (float) 0.9;
				break;
			case 9:  //95%
				powerPara = (float) 0.95;
				break;
			case 10: //100%
				powerPara = (float) 1;
				break;
			default:
				break;
			}
			//2为训练标记,直接开始训练波形,不需要采集最大肌力
			lastActivity = 2;
			//初始化图表
			initChart(trainLevel);     
			//设置图表坐标轴,当最大肌力小于10时就设置为10,大于10时则设置为1.2倍
//			if(maxpower >= 10)
			recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower*1.2));
//			else 
//				recoverchart.getAxisLeft().setAxisMaxValue((float) (10*1.2));
			//清理模板数组
			yVals.clear();
			//训练初始化波形为2
			setParameter();
			type = trainTypeSet[0];
			//设置模板波形
//			if(maxpower >= 10)
				setModelyValsType(type, levelYAxis_1, (float) (maxPower*powerPara));
//			else
//				setModelyValsType(type, levelYAxis_1, (float) (10*powerpara));
			//设置数据到图表中
			setData(0, 0, trainLevel);
			//显示
			recoverChart.animateX(0);
			//当前肌力显示隐藏
			show_nowPower_ll.setVisibility(View.GONE);
			show_con_ll.setVisibility(View.GONE);
			show_goal_title_ll.setVisibility(View.VISIBLE);
			//当为波形测试时，标题显示切换
			show_maxPower_title.setText("I类肌收缩持续时间：");
			show_frequency_title.setText("II类肌有效收缩次数：");
			show_maxpower_unit.setText("秒");
			//设计目标值
			contractionTime *= trainTime/5;
			contractionNum *= trainTime/5;
			show_goalI.setText(String.valueOf(contractionTime));
			show_goalII.setText(String.valueOf(contractionNum));
		}
		//新建蓝牙设备数组
		list_device = new ArrayList<BluetoothDevice>();
		//新建线程
		mHandler = new Handler();
		//显示测试时间
		show_nowMinute.setText(String.valueOf(trainTime));
		//分钟
		minute = trainTime;
		//设置倒计时进度条的秒数
		addProgress = trainTime * 60 / 100;
		//蓝牙-晶
		app=(App)getApplication();
		app.manager.setRFstarBLEManagerListener(this);
	}

	//搜索蓝牙框-晶
	private void initEvent() {
		// TODO Auto-generated method stub
		if (app.manager.hasBle) {
			if (mdialog != null && mdialog.isShowing())
				mdialog.dismiss();
			if (!isConnected)
				if (!app.manager.isEdnabled(this)) {
					app.manager.startScanBluetoothDevice();
					startProgressDialog("正在搜索设备");
				}
		}
	}

	/** 蓝牙函数
	 * 创建断开提示
	 */
	private void createDialog(String title, String msg, String btntext) {
		mdialog = new AlertDialog.Builder(getApplicationContext())
				.setTitle(title)
				.setMessage(msg)
				.setPositiveButton(btntext,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
//								manager.isEdnabled(this);
								startScanBlue();
								arg0.dismiss();
							}

						})
				.setNegativeButton(
						getApplicationContext().getResources().getString(R.string.tips_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								arg0.dismiss();
							}
						}).create();
		mdialog.show();
	}
	
	/** 蓝牙函数
	 * 扫描
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
			//蓝牙连接成功-J
			if (RFStarBLEService.ACTION_GATT_CONNECTED.equals(action)) {
				stopProgressDialog();
				showToast(context.getResources().getString(
						R.string.toast_connect_OK));
				beginEndFlag = false;
				isConnected = true;
				isSearch = true;
			}
			//蓝牙没有连接上，就删除消息队列，更换兰蓝牙填充背景-J
			else if (RFStarBLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
				mHandler.removeCallbacks(runnable);
				isConnected = false;
				if (isSearch) {
					isSearch = false;
					blueTooth.setBackground(getResources().getDrawable(R.drawable.icon_bluetooth_disconnect));
					startScanBlue();
				}
			}
			else if (RFStarBLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				if (app.manager.cubicBLEDevice != null)//蓝牙连接上了设备
				{
					if(lastActivity1 == 1)//测试
						showToast("准备测试，倒数3秒钟");
					else//训练
						showToast("训练开始，请按照训练波形进行训练，倒数3秒钟");
					if(MediaPlayflag)
					{
						mediaPlayer.start();
						MediaPlayingflag = true;
						mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
							//Log.d("tag", "播放完毕");
							//根据需要添加自己的代码。。。
								MediaPlayingflag = false;
							}
						});
					}
					 new Handler().postDelayed(new Runnable() {							
						 //500ms延时后发送采集数据的指令到下位机
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (app.manager.cubicBLEDevice != null)
									app.manager.cubicBLEDevice
									.setNotification("ffe0", "ffe4", true);
							}
					 }, 1000);
					 new Handler().postDelayed(new Runnable() {
						 //500ms延时后发送采集数据的指令到下位机
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (app.manager.cubicBLEDevice != null)
									app.manager.cubicBLEDevice.writeValue("ffe5", "ffe9",
											"LS01A000S".getBytes());
								if(mHandler != null)
									mHandler.postDelayed(runnable, 100);
							}
					}, 2000);
				}
			}
			else if (RFStarBLEService.ACTION_DATA_AVAILABLE.equals(action)) {
				byte[] data = intent
				.getByteArrayExtra(RFStarBLEService.EXTRA_DATA);
				//判断数据是否正常
				if(data.length == 3)
				{
					//对采集到的压力数据进行处理
					int b = (int)  ((data[2] & 0xFF)   
				            | ((data[1] & 0xFF)<<8)   
				            | ((data[0] & 0xFF)<<16));
					
					//对蓝牙传回的压力值进行数据处理
					if(PCBversion == 1)
						powerData = (float) ((b*9.8/1000.00) - adjustPower);
					else
						powerData = (float) ((b*2/1000.00) - adjustPower);
//					showToast(String.valueOf(powerdata));
				}
			}
		}
	
	@Override
	public void RFstarBLEManageListener(BluetoothDevice device, int rssi,
			byte[] scanRecord) {
			if(device.getName()!=null)
			{
//				Log.i(TAG, device.getName());
				//判断设备名称为ls则进行连接
				if(device.getName().equals("ls"))
				{
					list_device.add(device);
					app.manager.stopScanBluetoothDevice();
				}
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
//			Log.i(TAG, "*************开始搜索");
			app.manager.isScanning=true;
			this.list_device.clear();
		}
		
	@Override
	public void RFstarBLEManageStopScan() {
			// TODO Auto-generated method stub
			//停止显示扫描进度框
			stopProgressDialog();
			app.manager.isScanning = false;
			//如果没有搜索到设备则显示重连提示框
			if (list_device.size() < 1) {
				//创建蓝牙断开对话框
				AnfeiAboutDialog BlueDisconnectDialog = null;//蓝牙断开提示框
				BlueDisconnectDialog = new AnfeiAboutDialog(RecoverTrainActivity.this,"蓝牙");
				setDialogHeight(BlueDisconnectDialog);
				BlueDisconnectDialog.setListener(new com.health.hl.views.AnfeiAboutDialog.OnCustomDialogListener() {
					
					@Override
					public void back(String name) {
						// TODO Auto-generated method stub
							startScanBlue();
					}
				});
				BlueDisconnectDialog.show();
				return;
			}
			for (int i = 0; i < list_device.size(); i++) {
				//System.out.println(list_device.get(i).getName());
				if (list_device.get(i).getName()
						.equals("ls")) {
					connectDevice(list_device.get(i));
					blueTooth.setBackground(getResources().getDrawable(R.drawable.connect));
					return;
				}
			}
		}

	//更换蓝牙的图标-J
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
		
	@Override
	protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
		}
		
	int a = 0;

	Runnable runnable = new Runnable() {
		//波形显示线程
		@Override
		public void run() {
			mHandler.postDelayed(this, 100);
			//如果为第一次进入界面,先把语音播报播完再开始进行肌力测试
			if(!beginEndFlag)
			{
				//先把介绍语音播放完再开始
//				if(mediaPlayer!=null)
//				{
					if (MediaPlayingflag != true) {//语音不在播报
						secondCount++;
						if (secondCount >= 10) {
							//从5秒开始倒数,5,4,3,2,1..如果lastactivity1为1则是测试,为2则是训练
							if(lastActivity1 == 1)
							{
								String a = String.valueOf(3 - beginCount) + "秒后开始测试";
								showToast(a);
							}else
							{
								String a = String.valueOf(3 - beginCount) + "秒后开始训练";
								showToast(a);
							}
							beginCount++;
							secondCount = 0;
							//当大于5秒时则开始波形
							if (beginCount >= 3) {
//								customToast.cancel();
								beginCount = 0;
								xVals1.clear();
								yVals1.clear();
								beginEndFlag = true;
							}
						}
					}
				}
			else {
					//存储压力值
					powerDataSet[a]=powerData;
					//如果压力值异常则处理
					if(powerData>100)
						powerData=0;
				    //设置数据显示格式为0.0
					DecimalFormat decimalFormat = new DecimalFormat("0.0"); 
					//如果本次数据大于记录的最大肌力则更新最大肌力数值
					if ((powerData > maxPower)&&(powerData > 0)&&(powerData < 100))
					{
						maxPower = powerData;
						if(lastActivity1 == 1)
							show_maxPower.setText(decimalFormat.format(maxPower));
					}
					//播放收缩、放松语音
					playTrainSound(a);
					//显示当前肌力
					if(lastActivity1 == 1)
					{
						if((powerData<=0)||(powerData>500))
						{
							show_nowPower.setText(decimalFormat.format(0.0));
						}else
						{
							show_nowPower.setText(decimalFormat.format(powerData));
						}
					}
					//当模板波形输出为高是开放数据采集,即此时压力值高于模板波形幅度才算有效持续时间
					OpenTimeCount(a);
					//设置数据并显示图表
					setData(a, powerData, trainLevel);
					recoverChart.animateX(0);
					//倒计时
					if (lastActivity != 1)// 测试波形
					{
						secondCount++;
						if (secondCount >= 10) {
							secondCount = 0;
							if (second != 0) {
								second--;
							} else {
								minute--;
								second = 59;
								show_nowMinute.setText(String.valueOf(minute));
							}
							show_nowSecond.setText(String.valueOf(second));
							progressCount++;
							if (progressCount >= addProgress) {
								progressCount = 0;
								nowProgress++;
								timeProgress.setProgress(nowProgress);
							}
						}
					}
					a++;
					//当一个波形完成(30秒),即a计数到300
					if (a >= 300) {
						a = 0;
						//肌力基准值自适应算法
						adjustPower();
						//若为肌力测试
						if(lastActivity1 == 1)
						{
						//如果为2时表示正在测试波形,倒计时才开始倒数
						if (lastActivity == 2)
							timeCount++;
						//测试波形切换标记
						typeChoose = !typeChoose;
						//倒计时结束
						if (timeCount >= (trainTime * 2)) {//5min走10帧 /jing
							timeCount = 0;
							//停止线程
							mHandler.removeCallbacks(runnable);
							//显示查看报告对话框
							RecoverReportDialog a = new RecoverReportDialog(RecoverTrainActivity.this,"报告");
							a.setListener(new OnCustomDialogListener() {
								
								@Override //重新测试
								public void back(String name) {
									reTest();
								}
								@Override //查看报告	
								public void goto_report(String name) {
									//计算肌力等级
									if(app.manager.isScanning){
										app.manager.stopScanBluetoothDevice();
									}
									if (app.manager.cubicBLEDevice != null) {
										if (app.manager.cubicBLEDevice.isConnect) {
//											manager.cubicBLEDevice.closeDevice();
											isSearch=false;
											app.manager.cubicBLEDevice.disconnectedDevice();
											isConnected=false;			
										}
//										app.manager.cubicBLEDevice=null;
									}
									muscleEstimate();
									new Handler().postDelayed(new Runnable() {						
										@Override
										public void run() {
											// TODO Auto-generated method stub
//											codeAnalysis(code);
												Intent intent1 = new Intent(RecoverTrainActivity.this,
														RecoverAnalysisActivity.class);
												intent1.putExtra("userid",userId);
												intent1.putExtra("userweight",userWeight);
												intent1.putExtra("userphone",userPhone);//晶
												startActivity(intent1);
//											}
										}
									}, 1000);
								}
							});
							setDialogHeight(a);
							a.show();
						}else  {   //未计时结束,进行下一段波形
							xVals1.clear();
							yVals1.clear();
							yVals.clear();
							
						    /**当本次最大肌力大于上一次的最大肌力,则要继续测试用户的最
						     * 大肌力,如果相同则表示用户已经达到最大肌力,其最大肌力无
						     * 法再提升
						     */
//							if(maxPower!=lastMaxPower)
							if(maxPowerCount != 0)
								maxPowerCount--;
							else
							{
								//成功采集最大肌力,设置标记,开始进入测试波形
								lastActivity = 2;
								if(type == 7)
									showToast("成功采集最大肌力");
							}
							if (lastActivity == 1)
							{
								type = 7;  //7为采集最大肌力波形
								//设置坐标轴最大值为最大肌力的1.2倍
								if(maxPower <=10)
								{
									if(PCBversion == 0)
										recoverChart.getAxisLeft().setAxisMaxValue((float) 10.0);
									else
										recoverChart.getAxisLeft().setAxisMaxValue((float) 20.0);
									setModelyVals(trainLevel, type);
								}
								else
								{
									recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower*1.2));
									setModelyValsType(type, levelYAxis_1, maxPower);
								}	
							}else if (lastActivity == 2) {
								if (testMuscleFlag == false) {
									type = 1;  //I类肌测试波形,5秒的持续时间
									testMuscleFlag = true;
									if(quickLevelFirstTime)
										quickLevelFirstTime = false;
									else
									{
										//存储每次的I类肌测试结果
										quickData[quickCount] = quickLevel;
//										showToast(String.valueOf(quickdata[quickcount]));
										quickCount++;
										quickLevel = 0;
										show_frequency.setText(String.valueOf(quickLevel));
									}
								} else {
									type = 6;  //II类肌测试波形,5次快速收放
									testMuscleFlag = false;
									//累计I累计的持续时间,然后取5次的平均值来评估肌力等级
									if(durationCount*0.1>=5.0)
									{
										//若大于5秒则判断此次为5秒
										time_MuscleI += 5.0;
										slowData[slowCount] = (float) 5.0;
									}
									else
									{
										time_MuscleI += (float) (durationCount*0.1);
										slowData[slowCount] = (float) (durationCount*0.1);
									}
//									showToast(String.valueOf(slowdata[slowcount]));
									slowCount++;
								}
								//如果maxpower = 0则重新测试最大肌力，如果不等于0则把测试波形幅度设为60%，Y轴幅度设为120%
								if(maxPower!=0)
								{
									recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower*1.2));
									setModelyValsType(type, levelYAxis_1, (float) (maxPower*0.6));
								}else
								{
									type = 7;    //重新采集最大肌力
									recoverChart.getAxisLeft().setAxisMaxValue((float) 10.0);
									setModelyVals(trainLevel, type);
									lastActivity = 1;
								}
							} else {
							}
							//复位持续时间
							durationCount = 0;
							show_duration.setText(decimalFormat.format(durationCount * 0.1));
							System.out.println("value:" + type);
						}

					}else    //训练
					{
						timeCount ++ ;
						if (timeCount >= (trainTime * 2)) {
							timeCount = 0;
							//停止线程
							mHandler.removeCallbacks(runnable);
							//显示训练结束消息框
							showToast("训练结束");
							
							netPath = "http://119.23.248.54/Ls_Server_web/MsTrain/save";
							netPara = "uId="+userId+"&"+"caseCode="+mPlanCode+"&"+"trainTime="+String.valueOf(trainTime)
									+"&"+"trainLevel="+String.valueOf(trainLevel)+"&"+"trainScore="+"0"+"&"+"idealTime="
									+String.valueOf(trainTime)+"&"+"trainMode="+"2";
							code = null;
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										code = postDownloadJson(netPath,netPara);
//										codeanalysis(code);
									} catch (Exception e) {
										// TODO Auto-generated catch block
									}
								}
							}).start();
						}else {
							xVals1.clear();
							yVals1.clear();
							yVals.clear();	

							trainTypeCount++;//训练模式下的第几个波形
							if(trainTypeCount >9)
							{
								trainTypeCount = 0;
							}
							type = trainTypeSet[trainTypeCount];//训练波形方案波形组
							if((trainTypeCount == 2)||(trainTypeCount == 5))
							{
								recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower*1.2*highTypeSet));//highTypeSet高波模式下的倍数
								setModelyValsType(type, levelYAxis_1, (float) (maxPower*powerPara*highTypeSet));
							}else
							{
								recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower*1.2));
								setModelyValsType(type, levelYAxis_1, (float) (maxPower*powerPara));
							}
							//复位持续时间
						}
					}
					}
				}
			}
		};

	/**
	 * 初始化曲线图
	 */
	private void initChart(int level) {
		recoverChart.setDescription("");
		recoverChart.setDrawGridBackground(false);

		XAxis xAxis = recoverChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		// xAxis.setTypeface(mTf);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(true);

		YAxis leftAxis = recoverChart.getAxisLeft();
		// leftAxis.setTypeface(mTf);
		leftAxis.setLabelCount(5, false);
		// 根据不同训练等级设置Y坐标轴
		switch (level) {
		case 1:
			leftAxis.setAxisMaxValue(levelYAxis_1);
			break;
		case 2:
			leftAxis.setAxisMaxValue(levelYAxis_2);
			break;
		case 3:
			leftAxis.setAxisMaxValue(levelYAxis_3);
			break;
		case 4:
			leftAxis.setAxisMaxValue(levelYAxis_4);
			break;
		case 5:
			leftAxis.setAxisMaxValue(levelYAxis_5);
			break;
		case 6:
			leftAxis.setAxisMaxValue(levelYAxis_6);
			break;
		case 7:
			leftAxis.setAxisMaxValue(levelYAxis_7);
			break;
		case 8:
			leftAxis.setAxisMaxValue(levelYAxis_8);
			break;
		case 9:
			leftAxis.setAxisMaxValue(levelYAxis_9);
			break;
		case 10:
			leftAxis.setAxisMaxValue(levelYAxis_10);
			break;
		default:
			leftAxis.setAxisMaxValue(30);
			break;
		}

		leftAxis.setAxisMinValue(0);

		// 禁止右边Y坐标轴显示
		recoverChart.getAxisRight().setEnabled(false);
		// 禁止X坐标轴显示
		recoverChart.getXAxis().setEnabled(false);
		// 禁止图表缩放
		recoverChart.setScaleXEnabled(false);
		recoverChart.setScaleYEnabled(false);
		recoverChart.setFocusable(false);
		recoverChart.setFocusableInTouchMode(false);
		// recoverchart.setClickable(false);
		// 设置画图标轮廓线
		recoverChart.setDrawBorders(true);
		// 设置图标不能识别触摸手势
		recoverChart.setTouchEnabled(false);
		if (lastActivity1 == 1) {
			type = 7;
		} else if(lastActivity1 == 2)
		{
			type = 1;
		}
		else{
			Random a = new Random();
			type = a.nextInt(6);
			System.out.println("value:" + type);
			type += 1;
		}
		setModelyVals(level, type);
		// setData(100, 10);
		recoverChart.animateX(0);
	}

	Handler handler = new Handler() {
	};

	// X坐标
	ArrayList<String> xVals1 = new ArrayList<String>();
	// 实际测量压力值数据
	ArrayList<Entry> yVals1 = new ArrayList<Entry>();

	// 画图函数
	private void setData(int a, float data, int trainlevel) {
		// xVals.add(a+"");
		xVals1.clear();
		//先填入300个X坐标
		for (int i = 0; i < 300; i++) {
			xVals1.add("");
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		//如果data大于坐标轴最大值则显示为最大值
		if(data <= 0)
			yVals1.add(new Entry(0, a));
		else if(data <= recoverChart.getAxisLeft().getAxisMaxValue())
			yVals1.add(new Entry(data, a));
		else 
			yVals1.add(new Entry(recoverChart.getAxisLeft().getAxisMaxValue(), a));
		//如果是肌力测试,则当data大于maxpower的60%则属于有效数值，增加持续时间
		if(lastActivity1 == 1)
		{
			if ((data >= maxPower*0.6)&&(lastActivity == 2)&&(timeADCflag)) 
			{
				durationCount += 1;
				if(type == 1)
					show_duration.setText(decimalFormat.format(durationCount * 0.1));
		    }
		}else  //如果是盆底训练则需要根据等级判断data是否大于maxpower*powerpara的值
			if((data >= maxPower*powerPara)&&(lastActivity == 2)&&(timeADCflag))
			{
				//总持续时间
				durationCount_train += 1;
				//单次波形的持续时间,用于判断有效收放次数
				durationCount += 1;
				show_maxPower.setText(decimalFormat.format(durationCount_train * 0.1));
			}
		//根据不同波形进行有效收放次数的显示
		switch (type) {
		case 2:
			if((a == 80)||(a == 180)||(a == 280))
			{
				if(durationCount*0.1>=4)
				{
					num_train++;
					show_frequency.setText(String.valueOf(num_train));
				}
				durationCount = 0;
			}
			break;
		case 3:
			if((a == 120)||(a == 280))
			{
				if(durationCount*0.1>=6)
				{
					num_train++;
					show_frequency.setText(String.valueOf(num_train));
				}
				durationCount = 0;
			}
			break;
		case 4:
			if((a == 120)||(a == 260))
			{
				if(durationCount*0.1>=8)
				{
					num_train++;
					show_frequency.setText(String.valueOf(num_train));
				}
				durationCount = 0;
			}
			break;
		case 5:
			if((a == 50)||(a == 100)||(a == 150)||(a == 200)||(a == 250))
			{
				if(durationCount*0.1>=1)
				{
					num_train++;
					show_frequency.setText(String.valueOf(num_train));
				}
				durationCount = 0;
			}
			break;
		case 6:
			if((a == 40)||(a == 100)||(a == 160)||(a == 220)||(a == 280))
			{
				if(lastActivity1 == 1)
				{
					if(durationCount>=1)
					{
						quickLevel++;
						show_frequency.setText(String.valueOf(quickLevel));
					}
				}else
					if(durationCount*0.1>=1)
					{
						num_train++;
						show_frequency.setText(String.valueOf(num_train));
					}
				durationCount = 0;
			}
			break;
		case 7:
			if((a == 50)||(a == 150)||(a == 250))
			{
				if(lastActivity1 == 2)
				{
					if(durationCount*0.1>=1)
					{
						num_train++;
						show_frequency.setText(String.valueOf(num_train));
					}
				}
				durationCount = 0;
			}
			break;
		default :
			break;	
		}

		LineDataSet set1 = new LineDataSet(yVals1, "实际曲线");
		LineDataSet set2 = new LineDataSet(yVals, "训练曲线");
		// 实际压力值曲线设置
		set1.enableDashedLine(10f, 0f, 0f);
		set1.setColor(0xFFF186FD);
		set1.setLineWidth(3f);
		set1.setDrawCircles(false);
		set1.setDrawCircleHole(false);
		set1.setDrawValues(false);
		// 模板训练曲线设置
		set2.enableDashedLine(10f, 0f, 0f);
		set2.setColor(0xFF7C9AFF);
		set2.setLineWidth(3f);
		set2.setDrawCircles(false);
		set2.setDrawCircleHole(false);
		set2.setDrawValues(false);

		//先放set2,再放set1,把set1(实际波形)显示在上层
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set2); // add the datasets
		dataSets.add(set1);
		LineData datachart = new LineData(xVals1, dataSets);
		// set data 如果y坐标大于x坐标则报错
		if ((yVals.size() <= xVals1.size()) || (yVals1.size() <= xVals1.size()))
			recoverChart.setData(datachart);

	}
	
	//开启持续时间标记
	private void OpenTimeCount(int xVals) 
	{
		//timeADCflag为true时开启采集，为false关闭采集，即在休息期即使当前肌力达到要求也不会计算持续时间
		switch(type)
		{
			case 1:
				if(xVals == 110)
					timeADCflag = true;
				else if(xVals == 160)
					timeADCflag = false;
				break;
			case 2:
				if((xVals == 20)||(xVals == 120)||(xVals == 220))
					timeADCflag = true;
				else if((xVals == 80)||(xVals == 180)||(xVals == 280))
					timeADCflag = false;
				break;
			case 3:
				if((xVals == 40)||(xVals == 200))
					timeADCflag = true;
				else if((xVals == 120)||(xVals == 280))
					timeADCflag = false;
				break;
			case 4:
				if((xVals == 20)||(xVals == 160))
					timeADCflag = true;
				else if((xVals == 120)||(xVals == 260))
					timeADCflag = false;
				break;
			case 5:
				break;
			case 6:
				if((xVals == 20)||(xVals == 80)||(xVals == 140)||(xVals == 200)||(xVals == 260))
					timeADCflag = true;
				else if((xVals == 40)||(xVals == 100)||(xVals == 160)||(xVals == 220)||(xVals == 280))
					timeADCflag = false;
				break;
			case 7:
				if((xVals == 20)||(xVals == 120)||(xVals == 220))
					timeADCflag = true;
				else if((xVals == 50)||(xVals == 150)||(xVals == 250))
					timeADCflag = false;
				break;
			case 8:
				if((xVals == 20)||(xVals == 120)||(xVals == 220))
					timeADCflag = true;
				else if((xVals == 80)||(xVals == 180)||(xVals == 280))
					timeADCflag = false;
				break;
			case 9:
				if((xVals == 40)||(xVals == 180))
					timeADCflag = true;
				else if((xVals == 120)||(xVals == 260))
					timeADCflag = false;
				break;
			case 10:
				if((xVals == 40)||(xVals == 170))
					timeADCflag = true;
				else if((xVals == 130)||(xVals == 260))
					timeADCflag = false;
				break;
			case 11:
				if((xVals == 30)||(xVals == 170))
					timeADCflag = true;
				else if((xVals == 130)||(xVals == 270))
					timeADCflag = false;
				break;
			case 12:
				if((xVals == 30)||(xVals == 160))
					timeADCflag = true;
				else if((xVals == 140)||(xVals == 270))
					timeADCflag = false;
				break;
			case 13:
				if((xVals == 20)||(xVals == 160))
					timeADCflag = true;
				else if((xVals == 140)||(xVals == 280))
					timeADCflag = false;
				break;
			default:
				timeADCflag = false;
				break;
		}		
	}
	
	// 模板波形数据，需分不同等级
	ArrayList<Entry> yVals = new ArrayList<Entry>();
	private String sound;
	private String describe;
	private int lastdata;
	private String histroy1_I;
	private String histroy2_I;
	private String histroy3_I;
	private String histroy4_I;
	private String histroy5_I;
	private String histroy1_II;
	private String histroy2_II;
	private String histroy3_II;
	private String histroy4_II;
	private String histroy5_II;

	//设置波形模板
	private void setModelyVals(int level, int type) {
		switch (level) {
		case 1:
			setModelyValsType(type, levelYAxis_1, levelY_1);
			break;
		case 2:
			setModelyValsType(type, levelYAxis_2, levelY_2);
			break;
		case 3:
			setModelyValsType(type, levelYAxis_3, levelY_3);
			break;
		case 4:
			setModelyValsType(type, levelYAxis_4, levelY_4);
			break;
		case 5:
			setModelyValsType(type, levelYAxis_5, levelY_5);
			break;
		case 6:
			setModelyValsType(type, levelYAxis_6, levelY_6);
			break;
		case 7:
			setModelyValsType(type, levelYAxis_7, levelY_7);
			break;
		case 8:
			setModelyValsType(type, levelYAxis_8, levelY_8);
			break;
		case 9:
			setModelyValsType(type, levelYAxis_9, levelY_9);
			break;
		case 10:
			setModelyValsType(type, levelYAxis_10, levelY_10);
			break;
		default:
			break;
		}
	}

	// 模板波形数据，需分不同等级
	private void setModelyValsType(int type, float levelYAxis, float levelY) {
		switch (type) {
		// 波形1  I类肌测试波形  持续5秒
		case 1:
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 110 && i >= 100) {
					val = levelY / 10 * i - 10 * levelY;
				} else if (i < 160 && i > 110) {
					val = levelY;
				} else if (i <= 170 && i >= 160) {
					val = (-levelY) / 10 * i + 17 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		// 波形2 I类肌测试波形  持续4秒
		case 2:
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 30 && i >= 20) {
					val = levelY / 10 * i - 2 * levelY;
				}else if(i <= 130 && i >= 120){
					val = levelY / 10 * i - 12 * levelY;
				}else if(i <= 230 && i >= 220){
					val = levelY / 10 * i - 22 * levelY;
				}else if ((i <= 70 && i >= 30)||(i <= 170 && i >= 130)||(i <= 270 && i >= 230)) {
					val = levelY;
				}else if (i < 80 && i > 70) {
					val = (-levelY) / 10 * i + 8 * levelY;
				}else if (i <= 180 && i >= 170) {
					val = (-levelY) / 10 * i + 18 * levelY;
				}else if (i <= 280 && i >= 270) {
					val = (-levelY) / 10 * i + 28 * levelY;
				}else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;

		// 波形3 I类肌测试波形  持续6秒
		case 3:
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 50 && i >= 40) {
					val = levelY / 10 * i - 4 * levelY;
				}else if (i <= 210 && i >= 200) {
					val = levelY / 10 * i - 20 * levelY;
				}else if ((i < 110 && i > 50)||(i < 270 && i > 210)) {
					val = levelY;
				}else if (i <= 120 && i >= 110) {
					val = (-levelY) / 10 * i + 12 * levelY;
				}else if (i <= 280 && i >= 270) {
					val = (-levelY) / 10 * i + 28 * levelY;
				}else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
			// 波形3 I类肌测试波形  持续8秒
		case 4:
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 30 && i >= 20) {
					val = levelY / 10 * i - 2 * levelY;
				}else if (i <= 170 && i >= 160) {
					val = levelY / 10 * i - 16 * levelY;
				}else if ((i < 110 && i > 30)||(i < 250 && i > 170)) {
					val = levelY;
				}else if (i <= 120 && i >= 110) {
					val = (-levelY) / 10 * i + 12 * levelY;
				}else if (i <= 260 && i >= 250) {
					val = (-levelY) / 10 * i + 26 * levelY;
				}else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 5:
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 60 && i >= 50) {
					val = levelY / 10 * i - 5 * levelY;
				} else if ((i < 100 && i > 60) || (i < 240 && i > 200)) {
					val = levelY;
				} else if (i <= 110 && i >= 100) {
					val = (-levelY) / 10 * i + 11 * levelY;
				} else if (i <= 200 && i >= 190) {
					val = levelY / 10 * i - 19 * levelY;
				} else if (i <= 250 && i >= 240) {
					val = (-levelY) / 10 * i + 25 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 6:   //II类肌训练波形,5次快速收放
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 30 && i >= 20) {
					val = levelY / 10 * i - 2 * levelY;
				} else if (i < 90 && i >= 80) {
					val = levelY / 10 * i - 8 * levelY;
				} else if (i < 150 && i >= 140) {
					val = levelY / 10 * i - 14 * levelY;
				} else if (i < 210 && i >= 200) {
					val = levelY / 10 * i - 20 * levelY;
				} else if (i < 270 && i >= 260) {
					val = levelY / 10 * i - 26 * levelY;
				}
				  else if (i <= 40 && i >= 30) {
					val = (-levelY) / 10 * i + 4 * levelY;
				} else if (i <= 100 && i >= 90) {
					val = (-levelY) / 10 * i + 10 * levelY;
				} else if (i <= 160 && i >= 150) {
					val = (-levelY) / 10 * i + 16 * levelY;
				} else if (i <= 220 && i >= 210) {
					val = (-levelY) / 10 * i + 22 * levelY;
				} else if (i <= 280 && i >= 270) {
					val = (-levelY) / 10 * i + 28 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 7:			// 波形7 II类肌训练波形,3次快速收放
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 30 && i >= 20) {
					val = levelY / 10 * i - 2 * levelY;
				} else if (i < 130 && i >= 120) {
					val = levelY / 10 * i - 12 * levelY;
				} else if (i < 230 && i >= 220) {
					val = levelY / 10 * i - 22 * levelY;
				} else if ((i <= 40 && i >= 30) 
						|| (i <= 140 && i >= 130) 
						|| (i <= 240 && i >= 230)) {
					val = levelY;
				} else if (i <= 50 && i > 40) {
					val = (-levelY) / 10 * i + 5 * levelY;
				} else if (i <= 150 && i > 140) {
					val = (-levelY) / 10 * i + 15 * levelY;
				} else if (i <= 250 && i > 240) {
					val = (-levelY) / 10 * i + 25 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 8://训练波形：基波
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 30 && i >= 20) {
					val = levelY / 10 * i - 2 * levelY;
				} else if (i < 130 && i >= 120) {
					val = levelY / 10 * i - 12 * levelY;
				} else if (i < 230 && i >= 220) {
					val = levelY / 10 * i - 22 * levelY;
				} else if ((i <= 70 && i >= 30) 
						|| (i <= 170 && i >= 130) 
						|| (i <= 270 && i >= 230)) {
					val = levelY;
				} else if (i <= 80 && i > 70) {
					val = (-levelY) / 10 * i + 8 * levelY;
				} else if (i <= 180 && i > 170) {
					val = (-levelY) / 10 * i + 18 * levelY;
				} else if (i <= 280 && i > 240) {
					val = (-levelY) / 10 * i + 28 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 9://训练波形：长波1
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 50 && i >= 40) {
					val = levelY / 10 * i - 4 * levelY;
				} else if (i < 190 && i >= 180) {
					val = levelY / 10 * i - 18 * levelY;
				} else if ((i <= 110 && i >= 50)
						|| (i <= 250 && i >= 190)) {
					val = levelY;
				} else if (i <= 120 && i > 110) {
					val = (-levelY) / 10 * i + 12 * levelY;
				} else if (i <= 260 && i > 250) {
					val = (-levelY) / 10 * i + 26 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 10://训练波形：长波2
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 50 && i >= 40) {
					val = levelY / 10 * i - 4 * levelY;
				} else if (i < 180 && i >= 170) {
					val = levelY / 10 * i - 17 * levelY;
				} else if ((i <= 120 && i >= 50)
						|| (i <= 250 && i >= 180)) {
					val = levelY;
				} else if (i <= 130 && i > 120) {
					val = (-levelY) / 10 * i + 13 * levelY;
				} else if (i <= 260 && i > 250) {
					val = (-levelY) / 10 * i + 26 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 11://训练波形：长波3
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 40 && i >= 30) {
					val = levelY / 10 * i - 3 * levelY;
				} else if (i < 180 && i >= 170) {
					val = levelY / 10 * i - 17 * levelY;
				} else if ((i <= 120 && i >= 40)
						|| (i <= 260 && i >= 180)) {
					val = levelY;
				} else if (i <= 130 && i > 120) {
					val = (-levelY) / 10 * i + 13 * levelY;
				} else if (i <= 270 && i > 260) {
					val = (-levelY) / 10 * i + 27 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 12://训练波形：长波4
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 40 && i >= 30) {
					val = levelY / 10 * i - 3 * levelY;
				} else if (i < 170 && i >= 160) {
					val = levelY / 10 * i - 16 * levelY;
				} else if ((i <= 130 && i >= 40)
						|| (i <= 260 && i >= 170)) {
					val = levelY;
				} else if (i <= 140 && i > 130) {
					val = (-levelY) / 10 * i + 14 * levelY;
				} else if (i <= 270 && i > 260) {
					val = (-levelY) / 10 * i + 27 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		case 13://训练波形：长波5
			for (int i = 0; i < 300; i++) {
				float val = 0;
				if (i <= 30 && i >= 20) {
					val = levelY / 10 * i - 2 * levelY;
				} else if (i < 170 && i >= 160) {
					val = levelY / 10 * i - 16 * levelY;
				} else if ((i <= 130 && i >= 30)
						|| (i <= 270 && i >= 170)) {
					val = levelY;
				} else if (i <= 140 && i > 130) {
					val = (-levelY) / 10 * i + 14 * levelY;
				} else if (i <= 280 && i > 270) {
					val = (-levelY) / 10 * i + 28 * levelY;
				} else {
					val = 0;
				}
				yVals.add(new Entry(val, i));
			}
			break;
		default:
			break;
		}
	}

	
    //播放收缩、放松语音函数,在波形到来前1秒提示收缩,波形结束前一秒提示放松
	private void playTrainSound(int xvals) {
		switch (type) {
		case 1:
			if(xvals == 90)
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if(xvals == 150)
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 2:
			if((xvals == 10)||(xvals == 110)||(xvals == 210))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 60)||(xvals == 160)||(xvals == 260))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 3:
			if((xvals == 30)||(xvals == 190))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 100)||(xvals == 260))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}	
			break;
		case 4:
			if((xvals == 10)||(xvals == 150))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 100)||(xvals == 240))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 5:
			if((xvals == 40)||(xvals == 180))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 90)||(xvals == 230))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 6:
			if((xvals == 10)||(xvals == 70)||(xvals == 130)||(xvals == 190)||(xvals == 250))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 30)||(xvals == 90)||(xvals == 150)||(xvals == 210)||(xvals == 270))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 7:
			if((xvals == 10)||(xvals == 110)||(xvals == 210))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 40)||(xvals == 140)||(xvals == 240))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 8:
			if((xvals == 10)||(xvals == 110)||(xvals == 210))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 60)||(xvals == 160)||(xvals == 260))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 9:
			if((xvals == 30)||(xvals == 170))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 100)||(xvals == 240))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 10:
			if((xvals == 30)||(xvals == 160))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 110)||(xvals == 240))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 11:
			if((xvals == 20)||(xvals == 160))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 110)||(xvals == 250))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 12:
			if((xvals == 20)||(xvals == 150))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 120)||(xvals == 250))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 13:
			if((xvals == 10)||(xvals == 150))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 120)||(xvals == 260))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		case 14:
			if((xvals == 10)||(xvals == 110)||(xvals == 210))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.shousuo);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			if((xvals == 60)||(xvals == 160)||(xvals == 260))
			{
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				mediaPlayer = MediaPlayer.create(this, R.raw.fangsong);
				if(MediaPlayflag)
					mediaPlayer.start();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.iv_recover_train_back:
			onBackPressed();
			break;
		case R.id.iv_recover_train_bluetooth: //点击开始扫描蓝牙
			if (app.manager.hasBle) {
				if (mdialog != null && mdialog.isShowing())
					mdialog.dismiss();
				if (!isConnected)
					if (!app.manager.isEdnabled(this)) {
						app.manager.startScanBluetoothDevice();
					}
			}
			break;
		case R.id.iv_recover_train_sound: //静音设置
			SharedPreferences prefs = App.get().getSharedPreferences(filename,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			if (MediaPlayflag) {
				MediaPlayflag = false;
				show_sound.setBackground(getResources().getDrawable(R.drawable.icon_recover_trainpage_sound0ff));
				editor.putString(SOUND, String.valueOf(0));
			}else
			{
				MediaPlayflag = true;
				show_sound.setBackground(getResources().getDrawable(R.drawable.icon_recover_trainpage_soundon));
				editor.putString(SOUND, String.valueOf(1));
			}
			editor.commit();
			break;
		default:
			break;
		}
	}

	//显示消息栏2.0 20190926晶
	public void showToast(String text) {
		//		ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
		try {
			ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
			customToast.setGravity(Gravity.CENTER, 0, 0);
			//        customToast.show();
		} catch (Exception e) {}
	}

	// 肌肉等级评估
	private void muscleEstimate() {
		if (lastActivity == 2) {
			//把第五次的测量值存入数组
			quickData[4] = quickLevel;
//			quickdata[1] = quicklevel;
//			for(int i=0;i<(quickdata.length-1);i++)
			for(int i=0;i<4;i++)
			{
				quickLevel += quickData[i];
			}
//			showToast(String.valueOf(quickdata[0])+","+String.valueOf(quickdata[1])+","+String.valueOf(quickdata[2])+","+String.valueOf(quickdata[3])+","+String.valueOf(quickdata[4])+",");
//			System.out.println("quicklevel:"+quicklevel);
			quickLevel /= 5;   //总次数除以5之后获得平均等级
//			quicklevel /= 2;   //总次数除以5之后获得平均等级
			//如果为0级则设置为1级
//			if(quicklevel == 0)
//				quicklevel = 1;
//			slowlevel = 1;
			
			/**  I类肌等级判断：
			               持续时间小于2秒为1级    
			               持续时间小于3秒为2级
			               持续时间小于4秒为3级
			               持续时间小于5秒为4级    
			               持续时间大于等于5秒为5级                        
			*/
//			System.out.println("time_MuscleI:"+time_MuscleI);
			time_MuscleI /= 5;
			if((time_MuscleI>=0)&&(time_MuscleI<1))
				slowLevel = 0;
			else if((time_MuscleI>=1)&&(time_MuscleI<2))
				slowLevel = 1;
			else if((time_MuscleI>=2)&&(time_MuscleI<3))
				slowLevel = 2;
			else if((time_MuscleI>=3)&&(time_MuscleI<4))
				slowLevel = 3;
			else if((time_MuscleI>=4)&&(time_MuscleI<5))
				slowLevel = 4;
			else if(time_MuscleI>=5)
				slowLevel = 5;
			else
				slowLevel = 1;
			//根据I类肌和II类肌的等级判断训练方案
			TrainPlan();
			//存储数据
			Boolean b = dataSave();
//			System.out.println("a");

		} else {
			trainScore = trainScore / (trainTime * 2);
		}
	}

	//存储肌力等级
	public boolean dataSave() {
		
		netPath = "http://119.23.248.54/Ls_Server_web/mstest/save";
		netPara = "uId="+userId+"&"+"oneMuscle="+String.valueOf(slowLevel)+"&"+"twoMuscle="+String.valueOf(quickLevel)
				+"&"+"maxPower="+String.valueOf(maxPower)+"&meTestChlids=[{'cOneMuscle':"+(int)slowData[0]+",'cTwoMuscle':"
				+(int)quickData[0]+"},{'cOneMuscle':"+(int)slowData[1]+",'cTwoMuscle':"+(int)quickData[1]+"},{'cOneMuscle':"
				+(int)slowData[2]+",'cTwoMuscle':"+(int)quickData[2]+"},{'cOneMuscle':"+(int)slowData[3]+",'cTwoMuscle':"+
				(int)quickData[3]+"},{'"+"cOneMuscle':"+(int)slowData[4]+",'cTwoMuscle':"+(int)quickData[4]+"}]";
//		showToast(netpath+"?"+netpara);
		code = null;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					code = postDownloadJson(netPath,netPara);
//					codeanalysis(code);
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		}).start();
		new Handler().postDelayed(new Runnable() {						
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				if(!code.equals("200"))
//				{
//					showToast("网络环境差，读取数据超时！");
//				}
			}
		}, 1000);
//		if(lastdata<6)
//			editor.putString(LASTDATA,String.valueOf(lastdata+1));
		return true;
	}
	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170327
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	public void reLoad() {
        SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );
       
       sound  = prefs.getString("sound", "1");
       describe  = prefs.getString("describe", "1");
       lastdata = Integer.valueOf(prefs.getString("lastdata", "1"));
       histroy1_I  = prefs.getString("histroy1_I", "1");
       histroy2_I  = prefs.getString("histroy2_I", "1");
       histroy3_I  = prefs.getString("histroy3_I", "1");
       histroy4_I  = prefs.getString("histroy4_I", "1");
       histroy5_I  = prefs.getString("histroy5_I", "1");
       histroy1_II  = prefs.getString("histroy1_II", "1");
       histroy2_II  = prefs.getString("histroy2_II", "1");
       histroy3_II  = prefs.getString("histroy3_II", "1");
       histroy4_II  = prefs.getString("histroy4_II", "1");
       histroy5_II  = prefs.getString("histroy5_II", "1");
       if(sound == "1")
       {
    	   MediaPlayflag = true;
		   show_sound.setBackground(getResources().getDrawable(R.drawable.icon_recover_trainpage_soundon));
       }
       else
       {
    	   MediaPlayflag = false; 
		   show_sound.setBackground(getResources().getDrawable(R.drawable.icon_recover_trainpage_sound0ff));
       }
       if(describe == "1")
       {
			RecoverTrainDialog DescribeDialog = null;
			DescribeDialog = new RecoverTrainDialog(RecoverTrainActivity.this,
					"介绍");
			setDialogHeight(DescribeDialog);
			DescribeDialog
					.setListener(new com.health.hl.views.RecoverTrainDialog.OnCustomDialogListener() {

						@Override
						public void back(String name) {
							// TODO Auto-generated method stub
							// retest();
							SharedPreferences prefs = App.get().getSharedPreferences(filename,
									Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = prefs.edit();
							//存储介绍状态
							editor.putString(DESCRIBE, String.valueOf(0));
							editor.commit();
							initEvent();

						}
					});
			DescribeDialog.show();
       }else
       {
    	   	firstRemainFlag = false;
       }
       }
	
	/**
	 * 设置dialog高度
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
	 * 重新测试
	 */
	private void reTest() {
		quickLevel = 0; // 快肌等级
		quickLevelFirstTime = true;
		slowCount = 0;
		quickCount = 0;
//		private int slowscore; // 慢肌评分
		slowLevel = 0; // 慢肌等级
		trainScore = 0; // 训练评分
		testMuscleFlag = false;  //测试肌肉波形切换
		// 波形种类
		type = 1;// 训练波形
		beginCount = 0;// 开始准备五秒
		beginEndFlag = false;   //首次进入界面标记
		typeChoose = false;     //训练波形在1和6之间切换标记
		trainTime = 0;// 训练时间
//		powerdata = 0;// 下位机上传的压力值
		maxPower = 0;// 最大压力值
		lastMaxPower = 0;// 最大压力值
//		private float maxpower1 = 0;// 最大压力值
		timeCount = 0;// 训练时间计算
		minute = 0;// 计算剩余分钟；
		second = 0;// 计算剩余秒
		secondCount = 0;// 用于计数，每计数10次为一秒
		durationCount = 0;// 持续时间计算
		time_MuscleI = 0;// 计算I类肌的持续时间
//		private int time_MuscleII = 0;// 计算二类肌的次数
		addProgress = 0;// 计算多少秒加1%进度
		nowProgress = 0;// 当前进度的百分比
		progressCount = 0;// 增加百分比计数
				
		lastActivity = 1;
		timeADCflag = false;//是否计数持续时间标记，当模板波形要求有收缩时实际收缩才计入有效的持续
		powerData = (float) 0;
		mediaPlayer = MediaPlayer.create(this, R.raw.starttest);
		trainTime = 5;    //默认测试5分钟
		trainLevel = 6;   //默认测试强度为6级,即最大肌力默认10kgf
//		text_mlevel.setText("肌力水平:");
		showToast("正在搜索设备");
		
		//初始化图表
		a = 0;
		xVals1.clear();
		yVals1.clear();
		yVals.clear();
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		durationCount = 0;
		show_duration.setText(decimalFormat.format(durationCount * 0.1));
		initChart(trainLevel);     
		setData(0, 0, trainLevel);
		recoverChart.animateX(0);
		show_nowMinute.setText(String.valueOf(trainTime));
		minute = trainTime;
		addProgress = trainTime * 60 / 100;
		mHandler.removeCallbacks(runnable);
//		mHandler.removeCallbacks(runnable);
		//开始搜索蓝牙
		if (app.manager.hasBle) {
			if (mdialog != null && mdialog.isShowing())
				mdialog.dismiss();
			if (!isConnected)
			{
				if (!app.manager.isEdnabled(this)) {
					app.manager.startScanBluetoothDevice();
				}
			}else
			{
				showToast("准备测试，倒数5秒钟");
				if(MediaPlayflag)
					mediaPlayer.start();
				mHandler.postDelayed(runnable, 100);
			}
		}
		
	}
	
	/**
	 * 开始dialog
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
	 * 停止dialog
	 */
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	/**
	 * 训练方案筛选
	 */
	private void TrainPlan() {
		switch (quickLevel) {
		case 0:
			switch (slowLevel) {
			case 0:
				trainPlan = "A";
				break;
			case 1:
				trainPlan = "A";
				break;
			case 2:
				trainPlan = "B";
				break;
			case 3:
				trainPlan = "C";
				break;
			case 4:
				trainPlan = "D";
				break;
			case 5:
				trainPlan = "E";
				break;			
			default:
				trainPlan = "A";
				break;
			}
			break;
		case 1:
			switch (slowLevel) {
			case 0:
				trainPlan = "A";
				break;
			case 1:
				trainPlan = "A";
				break;
			case 2:
				trainPlan = "B";
				break;
			case 3:
				trainPlan = "C";
				break;
			case 4:
				trainPlan = "D";
				break;
			case 5:
				trainPlan = "E";
				break;			
			default:
				trainPlan = "A";
				break;
			}
			break;
		case 2:
			switch (slowLevel) {
			case 0:
				trainPlan = "A";
				break;
			case 1:
				trainPlan = "F";
				break;
			case 2:
				trainPlan = "G";
				break;
			case 3:
				trainPlan = "H";
				break;
			case 4:
				trainPlan = "I";
				break;
			case 5:
				trainPlan = "J";
				break;			
			default:
				trainPlan = "F";
				break;
			}
			break;
		case 3:
			switch (slowLevel) {
			case 0:
				trainPlan = "A";
				break;
			case 1:
				trainPlan = "K";
				break;
			case 2:
				trainPlan = "L";
				break;
			case 3:
				trainPlan = "M";
				break;
			case 4:
				trainPlan = "N";
				break;
			case 5:
				trainPlan = "O";
				break;			
			default:
				trainPlan = "K";
				break;
			}
			break;
		case 4:
			switch (slowLevel) {
			case 0:
				trainPlan = "A";
				break;
			case 1:
				trainPlan = "P";
				break;
			case 2:
				trainPlan = "Q";
				break;
			case 3:
				trainPlan = "R";
				break;
			case 4:
				trainPlan = "S";
				break;
			case 5:
				trainPlan = "T";
				break;			
			default:
				trainPlan = "P";
				break;
			}
			break;
		case 5:
			switch (slowLevel) {
			case 0:
				trainPlan = "A";
				break;
			case 1:
				trainPlan = "U";
				break;
			case 2:
				trainPlan = "V";
				break;
			case 3:
				trainPlan = "W";
				break;
			case 4:
				trainPlan = "X";
				break;
			case 5:
				trainPlan = "Y";
				break;			
			default:
				trainPlan = "U";
				break;
			}
			break;
		default:
			trainPlan = "A";
			break;
		}
	}
	
	/**
	 * 肌力数值统计及调整肌力,对于每一帧波形最后1秒即10个压力值进行冒泡排序,然后取中值作为调整值
	 */
	private void adjustPower() {
		//冒泡排序
		float temp = 0;
//		float minpowertotal = 0;
		//冒泡排序
		for(int i=0;i<powerDataSet.length-1;i++)
		{
			for(int j=290;j<299-i;j++)
			{
				if(powerDataSet[j]>powerDataSet[j+1])
				{
					temp = powerDataSet[j];
					powerDataSet[j]=powerDataSet[j+1];  
					powerDataSet[j+1]=temp;
				}
			}
		}
		//压力基准值调整
 		adjustPower += powerDataSet[295];
	}
	
	//设置训练参数
	private void setParameter() {
			if(mPlan.equals("A"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 9;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 9;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.1;
				contractionTime = 96;
				contractionNum = 10;
			}else if(mPlan.equals("B"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 10;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 10;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.1;
				contractionTime = 100;
				contractionNum = 10;
			}else if(mPlan.equals("C"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 11;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 11;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.1;
				contractionTime = 104;
				contractionNum = 10;
			}else if(mPlan.equals("D"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 12;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 12;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.1;
				contractionTime = 108;
				contractionNum = 10;
			}else if(mPlan.equals("E"))
			{

				trainTypeSet[0] = 8;
				trainTypeSet[1] = 13;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 13;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.1;
				contractionTime = 112;
				contractionNum = 10;
			}else if(mPlan.equals("F"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 9;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 9;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.15;
				contractionTime = 96;
				contractionNum = 10;
			}else if(mPlan.equals("G"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 10;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 10;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.15;
				contractionTime = 100;
				contractionNum = 10;
			}else if(mPlan.equals("H"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 11;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 11;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.15;
				contractionTime = 104;
				contractionNum = 10;
			}else if(mPlan.equals("I"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 12;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 12;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.15;
				contractionTime = 108;
				contractionNum = 10;
			}else if(mPlan.equals("J"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 13;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 13;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.15;
				contractionTime = 112;
				contractionNum = 10;
			}else if(mPlan.equals("K"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 9;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 9;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.2;
				contractionTime = 96;
				contractionNum = 10;
			}else if(mPlan.equals("L"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 10;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 10;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.2;
				contractionTime = 100;
				contractionNum = 10;
			}else if(mPlan.equals("M"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 11;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 11;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.2;
				contractionTime = 104;
				contractionNum = 10;
			}else if(mPlan.equals("N"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 12;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 12;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.2;
				contractionTime = 108;
				contractionNum = 10;
			}else if(mPlan.equals("O"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 13;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 13;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.2;
				contractionTime = 112;
				contractionNum = 10;
			}else if(mPlan.equals("P"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 9;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 9;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.25;
				contractionTime = 96;
				contractionNum = 10;
			}else if(mPlan.equals("Q"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 10;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 10;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.25;
				contractionTime = 100;
				contractionNum = 10;
			}else if(mPlan.equals("R"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 11;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 11;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.25;
				contractionTime = 104;
				contractionNum = 10;
			}else if(mPlan.equals("S"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 12;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 12;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.25;
				contractionTime = 108;
				contractionNum = 10;
			}else if(mPlan.equals("T"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 13;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 13;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.25;
				contractionTime = 112;
				contractionNum = 10;
			}else if(mPlan.equals("U"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 9;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 9;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.3;
				contractionTime = 96;
				contractionNum = 10;
			}else if(mPlan.equals("V"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 10;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 10;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.3;
				contractionTime = 100;
				contractionNum = 10;
			}else if(mPlan.equals("W"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 11;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 11;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.3;
				contractionTime = 104;
				contractionNum = 10;
			}else if(mPlan.equals("X"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 12;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 12;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.3;
				contractionTime = 108;
				contractionNum = 10;
			}else if(mPlan.equals("Y"))
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 13;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 13;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.3;
				contractionTime = 112;
				contractionNum = 10;
			}else
			{
				trainTypeSet[0] = 8;
				trainTypeSet[1] = 9;
				trainTypeSet[2] = 8;
				trainTypeSet[3] = 8;
				trainTypeSet[4] = 6;
				trainTypeSet[5] = 8;
				trainTypeSet[6] = 8;
				trainTypeSet[7] = 9;
				trainTypeSet[8] = 6;
				trainTypeSet[9] = 8;
				highTypeSet = (float) 1.1;
				contractionTime = 96;
				contractionNum = 10;
			}
//			System.out.println("mplan"+mplan);
//			System.out.println("Pipelength:"+Pipelength);
//			System.out.println("Pipenum:"+Pipenum);
		}	

	/**
	 * fun：向服务器发送存储信息 aditor：zhou date:20170911 describe: path为接口网址，post为接口参数，使用的post方式，
	 */
	public static String postDownloadJson(String path,String post) throws Exception{
			int a = 0;
			//建立连接
			URL url = new URL(path);
			//打开连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//设置连接超时
			conn.setConnectTimeout(5000);
			//设置为POST模式
			conn.setRequestMethod("POST");
			//打开输出接口
			conn.setDoOutput(true);
			//打开输入接口
			conn.setDoInput(true);
			conn.setUseCaches(false);
			//获取输出引擎
			OutputStream outStream = conn.getOutputStream();
			//存入数据
			outStream.write(post.getBytes());
			outStream.flush();
			outStream.close();
			//获取服务器返回信息
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine = null;
			inputLine = reader.readLine();
			//截取CODE码用于识别状况
			String inputcode = inputLine.substring(8, 11);
			reader.close();
//	            a = conn.getResponseCode();
			return inputcode;
	}

	private void codeAnalysis(String code)
	{
	    	if(code.equals("200,"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_savesuccee));
	    	}else if(code.equals("400,"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_requestfail));
	    	}else if(code.equals("401,"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_requestillegal));
	    	}else if(code.equals("403,"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_requestclose));
	    	}else if(code.equals("404,"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_datamiss));
	    	}else if(code.equals("500,"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_serverswrong));
	    	}else if(code.equals("1001"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_clientwrong));
	    	}else if(code.equals("1002"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_parawrong));
	    	}else if(code.equals("1003"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_phoneexist));
	    	}else if(code.equals("1004"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_dataempty));
	    	}else if(code.equals("1005"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_updatefail));
	    	}else if(code.equals("1006"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_savefail));
	    	}else if(code.equals("1007"))
	    	{
	    		showToast(getApplicationContext().getResources().getString(
						R.string.recover_account_main_usetnotexist));
	    	}else
	    	{}
	    	if((code.equals("200,"))||(code.equals("1003")))
	    	{
	    		netFlag = true;
	    	}else
	    	{
	    		netFlag = false;
	    	}
	    }
	    
	//返回键处理
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
//	  				manager.cubicBLEDevice.closeDevice();
	  				isSearch=false;
	  				app.manager.cubicBLEDevice.disconnectedDevice();
	  				isConnected=false;
	  				
	  			}
//	  			app.manager.cubicBLEDevice=null;
	  			app.flag = true;
	  			
	  		}	
	         super.onBackPressed();//注释掉这行,back键不退出activity
//	        showToast("返回键");
//	    	this.finish();
	    }
	    
}
