package flapbird;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.health.hl.app.App;
import com.health.hl.app.AppManager.RFStarManageListener;
import com.health.hl.params.CubicBLEDevice;
import com.health.hl.params.BLEDevice.RFStarBLEBroadcastReceiver;
import com.health.hl.recover.RecoverAdjustActivity;
import com.health.hl.service.RFStarBLEService;
import com.health.hl.views.AnfeiAboutDialog;
import com.health.hl.views.RecoverGameOverDialog;
import com.health.hl.views.AnfeiAboutDialog.OnCustomDialogListener;
import com.health.hl.views.RecoverGameOverDialog.OnCustomDialogListener1;
import com.health.hl.R;
import com.zhy.view.GameFlabbyBird;
import com.zhy.view.GameFlabbyBird.GameStatus;
import com.zhy.view.GameFlabbyBird.MyCallInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Flapbird extends Activity implements RFStarManageListener, RFStarBLEBroadcastReceiver, MyCallInterface{
	GameFlabbyBird mGame;
	private int trainlevel;
	private int planlevel;
	private int traintime;

	//蓝牙
	private App app;
	private Dialog mDialog;
	private boolean isConnected = false;
	private final String TAG = "RecoverAdjustActivity";
	private ArrayList<BluetoothDevice> list_device; // 放置搜索到的蓝牙设备
	private Toast customToast;
	public boolean isSearch = true;
	private ProgressDialog progressDialog = null;//蓝牙搜索进度框
	private static PowerManager.WakeLock wakeLock;
	private String mPlan;
	private float adjustPower;
//	private float []adjustpowerdata = new float[20];
	private int adjustCount = 0;
//	private boolean adjustflag = false;
//	private String adjustword="";
	private String netPath;//接口网址
	private String netPara;//接口参数
	private String userId;
	private String mPlanCode;
	private String code; //网络返回码
	private int PCBversion;
	RecoverGameOverDialog gameOverDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置屏幕横屏
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
		trainlevel = Integer.parseInt(this.getIntent().getStringExtra(
				"levelvalue"));
		planlevel = Integer.parseInt(this.getIntent().getStringExtra(
				"Highestlevel"));
		adjustPower = Float.parseFloat(this.getIntent().getStringExtra(
				"adjustpower"));
		traintime = Integer.parseInt(this.getIntent().getStringExtra(
				"timevalue"));
		PCBversion = Integer.parseInt(this.getIntent().getStringExtra(
				"PCBversion"));
		mPlan = this.getIntent().getStringExtra("mplan");
		userId = this.getIntent().getStringExtra("userid");
		mPlanCode = this.getIntent().getStringExtra("mplancode");
		
//		showToast(String.valueOf(traintime));
//		System.out.println("planlevel:"+planlevel);
//		initView();
		initData();
		initEvent();	
		mGame = new GameFlabbyBird(this);
		mGame.trainlevel = trainlevel;
		mGame.Highesttrainlevel = planlevel;
		mGame.traintime = traintime;
//		mGame.traintime = 1;
		mGame.minute = traintime;
//		mGame.minute = 1;
		mGame.planGrade = (traintime/5)*150;
		mGame.maxpower = Float.parseFloat(this.getIntent().getStringExtra(
				"maxpower"));
		mGame.mplan = mPlan;
		mGame.setCallback(this);
		if(trainlevel<=5)
			mGame.setSpeed(6);
		else if(trainlevel<=8)
			mGame.setSpeed(6);
		else
			mGame.setSpeed(6);
		setContentView(mGame);
		gameOverDialog = new RecoverGameOverDialog(Flapbird.this,"报告");
		gameOverDialog.setListener(new OnCustomDialogListener1() {

			@Override
			public void replay(String name) {
				// TODO Auto-generated method stub
				gameOverDialog.hide();
			}

			@Override
			public void goto_adjust(String name) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		gameOverDialog.setCanceledOnTouchOutside(false);
		setDialogHeight(gameOverDialog);
//		gameOverDialog.show();
	}
	
	private void initEvent() {
		// TODO Auto-generated method stub
		//registerMessageReceiver();
		app=(App)getApplication();
		app.manager.setRFstarBLEManagerListener(this);
		// TODO Auto-generated method stub
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		list_device = new ArrayList<BluetoothDevice>();
//		reload();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// 设置屏幕横屏
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}	
		PowerManager pm = (PowerManager) getApplicationContext()
				.getSystemService(getApplicationContext().POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"==KeepScreenOn==");
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
//		sound = new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
//		sound.load(this,R.raw.m004,1);
//		sound.play(1,1, 1, 0, 0,1);
		super.onResume();
	}
	
	/**
	 * 创建断开提示
	 */
	private void createDialog(String title, String msg, String btntext) {
		mDialog = new AlertDialog.Builder(getApplicationContext())
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
		mDialog.show();
	}
	
	private void startScanBlue() {
		app.manager.setRFstarBLEManagerListener(this);
		app.manager.startScanBluetoothDevice();
//		startProgressDialog("正在搜索设备..");
//		gameOverDialog.show();
	}
	
	//连接相应函数，若连接状态或者有数据传输均跳到此函数
	@Override
	public void onReceive(Context context, Intent intent, String macData,
			String uuid) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (RFStarBLEService.ACTION_GATT_CONNECTED.equals(action)) {
			showToast(context.getResources().getString(
					R.string.toast_connect_OK));
			isConnected = true;
			isSearch = true;
			mGame.isRunningflag = true;
			mGame.timeRunningflag = true;
			stopProgressDialog();   //关闭搜索框

//			Log.i(TAG, "----- 连接完成! -----" + "UUID=" + uuid);
		   
		} else if (RFStarBLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
//			Log.i(TAG, "---------- 连接断开");
			//img_blue_state.setBackgroundResource(R.drawable.img_blue_off);
			//stepTimeHandler.removeCallbacks(mTicker);
			//isTimeRun = false;
			showToast(context.getResources().getString(
					R.string.toast_connect_dis));
			isConnected = false;
			mGame.isRunningflag = false;
			mGame.timeRunningflag = false;
			if (isSearch) {
				isSearch = false;
//				bluetooth.setBackground(getResources().getDrawable(R.drawable.img_blue_off));
				startScanBlue();
			}
		} else if (RFStarBLEService.ACTION_GATT_SERVICES_DISCOVERED
				.equals(action)) {
			if (app.manager.cubicBLEDevice != null) {
				//延迟500ms后发送开始采集指令 
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (app.manager.cubicBLEDevice != null)
							app.manager.cubicBLEDevice
							.setNotification("ffe0", "ffe4", true);  //发送蓝牙"读""写"特征码
						mGame.Startflag = true;
					}
				}, 1000);
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (app.manager.cubicBLEDevice != null)
							app.manager.cubicBLEDevice.writeValue("ffe5", "ffe9",
									"LS01A000S".getBytes());
					}
				}, 2000);
//				new Handler().postDelayed(new Runnable() {						
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						if (app.manager.cubicBLEDevice != null)
//							app.manager.cubicBLEDevice.writeValue1(
//									"LS01A000S".getBytes());
//					}
//				}, 2500);
//				new Handler().postDelayed(new Runnable() {						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							if (app.manager.cubicBLEDevice != null)
//								app.manager.cubicBLEDevice.writeValue1(
//										"LS01A000S".getBytes());
//						}
//					}, 3000);
			}
		} else if (RFStarBLEService.ACTION_DATA_AVAILABLE.equals(action)) {

//			if (uuid.contains("ffe4")) {
//				byte[] data = intent
//						.getByteArrayExtra(RFStarBLEService.EXTRA_DATA);
//				if (!(data.length < 5)) {
//					try {
//						String result = new String(data, "utf-8");
//						String resultsub = result.substring(0, 2);
//						Log.i(TAG, result);
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			String result = null;

			byte[] data = intent
			.getByteArrayExtra(RFStarBLEService.EXTRA_DATA);
			if(data.length == 3)
//			result = new String(data);
			{
			int a = (int)  ((data[2] & 0xFF)   
		            | ((data[1] & 0xFF)<<8)   
		            | ((data[0] & 0xFF)<<16));
//			a=(a-100)/100;
//			a=a-1;
			if(PCBversion == 1)
				a =  (int) ((((a*9.8/1000.00) - adjustPower))*1000);
			else
				a =  (int) ((((a*2/1000.00) - adjustPower))*1000);
			if((a<=0)||(a>1000000))
				a=0;
			if(a>=200000)
				a=200000;
			mGame.mBirdY = a;
//			if(adjustflag)
//			{
//				adjustpowerdata[adjustcount] = (float) (a*2/1000.00 - adjustpower);
//				adjustword += adjustpowerdata[adjustcount]+"-";
//				adjustcount ++;
//				if(adjustcount >= 20)
//				{
//					adjustcount = 0;
//					adjustflag = false;	
//					showToast(adjustword);
//					adjustword = "";
//				}
//			}
//			a=a/10000;
//			a=a-54;
//			if(a<=35)
//			{
//				if(a>=0)
//					pb_progress.setProgress(a);
//				else
//					pb_progress.setProgress(0);
//				pb_progress.setSecondaryProgress(0);
//			}else if((a>35)&&(a<=65))
//			{
//				pb_progress.setProgress(35);
//				pb_progress.setSecondaryProgress(a);
//			}else
//			{
//				pb_progress.setProgress(35);
//				pb_progress.setSecondaryProgress(65);
//			}
//			showToast(String.valueOf(mGame.x)+","+String.valueOf(mGame.y));
//			Log.i(TAG, String.valueOf(a));
			}
		}
	}
	
	private void showToast(String text) {
		if (customToast == null) {
			customToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		} else {
			customToast.setText(text);
			customToast.setDuration(Toast.LENGTH_SHORT);
		}
		customToast.show();
	}
	
	@Override
	public void RFstarBLEManageListener(BluetoothDevice device, int rssi,
			byte[] scanRecord) {
		//Log.i(TAG, device.getName() + "\n" + device.getAddress());
		if(device.getName()!=null)
		{
//			Log.i(TAG, device.getName());
			if(device.getName().equals("ls"))
			{
				list_device.add(device);
				app.manager.stopScanBluetoothDevice();
			}
			//System.out.println(device.getName());
		}
	}
	
	/**
	 * 连接设备
	 */
	private void connectDevice(BluetoothDevice device) {
		// app.manager.bluetoothDevice = device;
		// if (app.manager.cubicBLEDevice != null){
		// app.manager.cubicBLEDevice.disconnectedDevice();
		// app.manager.cubicBLEDevice.ungisterReceiver();
		//
		// app.manager.cubicBLEDevice = null;
		// }
		app.manager.cubicBLEDevice = new CubicBLEDevice(
				app.manager.context, device);
		app.manager.cubicBLEDevice.setBLEBroadcastDelegate(this);
	}
	
	@Override
	public void RFstarBLEManageStartScan() {
		// TODO Auto-generated method stub
//		Log.i(TAG, "*************开始搜索");
		// startAnimLoading();
		app.manager.isScanning=true;
		this.list_device.clear();
	}
	
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
		if (list_device.size() < 1) {
			AnfeiAboutDialog BlueDisconnectDialog = null;//蓝牙断开提示框
			BlueDisconnectDialog = new AnfeiAboutDialog(Flapbird.this,"蓝牙");
			setDialogHeight(BlueDisconnectDialog);
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
//			gameOverDialog.show();
//			BlueDisconnectDialog.show();
			return;
		}
		for (int i = 0; i < list_device.size(); i++) {
			//System.out.println(list_device.get(i).getName());
			if (list_device.get(i).getName()
					.equals("ls")) {
				connectDevice(list_device.get(i));
//				bluetooth.setBackground(getResources().getDrawable(R.drawable.img_blue_on));
				return;
			}
		}
	}
	
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(RFStarBLEService.ACTION_GATT_CONNECTED)) {
//				bluetooth.setBackground(getResources().getDrawable(R.drawable.img_blue_on));
			}else if (intent.getAction().equals(RFStarBLEService.ACTION_GATT_DISCONNECTED)) {
//				bluetooth.setBackground(getResources().getDrawable(R.drawable.img_blue_off));
			}
		}

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
		if(mGame.mediaPlayer != null )
		{
			mGame.mediaPlayer.stop();
			mGame.mediaPlayer.release();
			mGame.mediaPlayer = null;
		}
		if(mGame.mediaPlayer_hit != null )
		{
			mGame.mediaPlayer_hit.stop();
			mGame.mediaPlayer_hit.release();
			mGame.mediaPlayer_hit = null;
		}
//		if(!app.flag) {
			if(app.manager.isScanning){
				app.manager.stopScanBluetoothDevice();
			}
//		}
		if (app.manager.cubicBLEDevice != null) {
			if (app.manager.cubicBLEDevice.isConnect) {
//				manager.cubicBLEDevice.closeDevice();
				isSearch=false;
				app.manager.cubicBLEDevice.disconnectedDevice();
				isConnected=false;
				
			}
//			app.manager.cubicBLEDevice=null;
		}
		super.onDestroy();
	}
	
	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	private void reLoad() {
        SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );       
       mPlan = prefs.getString("trainplan", "A");         
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
	


	@Override
	public void method() {
		// TODO Auto-generated method stub
		netPath = "http://119.23.248.54/Ls_Server_web/MsTrain/save";
		netPara = "uId="+userId+"&"+"caseCode="+mPlanCode+"&"+"trainTime="+String.valueOf(traintime-mGame.minute)
				+"&"+"trainLevel="+String.valueOf(trainlevel)+"&"+"trainScore="+String.valueOf(mGame.mGrade)+"&"+"idealTime="
				+String.valueOf(traintime)+"&"+"trainMode="+"1";
		code = null;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					code = postDownloadJson(netPath,netPara);
//						codeanalysis(code);
				} catch (Exception e) {
					// TODO Auto-generated catch block
//						e.printStackTrace();
				}
			}
		}).start();
		new Handler().postDelayed(new Runnable() {						
			@Override
			public void run() {
	            finish();
			}
		}, 1000);
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
//            a = conn.getResponseCode();
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
            reader.close();
//            a = conn.getResponseCode();
            return inputLine;
    }
    
	@Override
	public void adjust() {
//		// TODO Auto-generated method stub
//		netPath = "http://119.23.248.54/Ls_Server_web/MsTrain/save";
//		netPara = "uId=" + userId + "&" + "caseCode=" + mPlanCode + "&"
//				+ "trainTime=" + String.valueOf(traintime) + "&"
//				+ "trainLevel=" + String.valueOf(trainlevel) + "&"
//				+ "trainScore=" + String.valueOf(mGame.mGrade) + "&"
//				+ "idealTime=" + String.valueOf(traintime) + "&" + "trainMode="
//				+ "1";
//		code = null;
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					code = postDownloadJson(netPath, netPara);
//					// codeanalysis(code);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					// e.printStackTrace();
//				}
//			}
//		}).start();
//		mGame.t.interrupt();
//		mGame.isRunningflag = false;
//		mGame.timeRunningflag = false;
		//显示查看报告对话框
		RecoverGameOverDialog gameOverDialog = null;
		gameOverDialog = new RecoverGameOverDialog(Flapbird.this,"报告");
		gameOverDialog.grade = String.valueOf(mGame.mGrade);
		if(mGame.gameOverflag == true)
		{
			gameOverDialog.replayFlag = true;
////			gameOverDialog.BtnReplay.setText("再玩一次");
		}
		else
		{
			gameOverDialog.replayFlag = false;
		}
		gameOverDialog.setListener(new OnCustomDialogListener1() {

			@Override
			public void replay(String name) {
				// TODO Auto-generated method stub
				mGame.mGrade = 0;
				mGame.lastmGrade = 0;
				mGame.gameOverflag = false;
				mGame.mStatus = GameStatus.WAITTING;
//				mGame.initPos();
			}

			@Override
			public void goto_adjust(String name) {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
				  		if(app.manager.isScanning){
				  			app.manager.stopScanBluetoothDevice();
				  		}
				  		if (app.manager.cubicBLEDevice != null) {
				  			if (app.manager.cubicBLEDevice.isConnect) {
//				  				manager.cubicBLEDevice.closeDevice();
				  				isSearch=false;
				  				app.manager.cubicBLEDevice.disconnectedDevice();
				  				isConnected=false;
				  				
				  			}
//				  			app.manager.cubicBLEDevice=null;
//				  			app.flag = true;
				  			
				  		}		
						finish();
					}
				}, 1000);
			}
		});
		gameOverDialog.setCanceledOnTouchOutside(false);
		setDialogHeight(gameOverDialog);
		gameOverDialog.show();
//		showToast("正在上传数据");
//		finish();
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
	
//    返回键处理
    @Override
    public void onBackPressed() {
		//显示查看报告对话框
		RecoverGameOverDialog gameOverDialog = null;
		gameOverDialog = new RecoverGameOverDialog(Flapbird.this,"报告");
		gameOverDialog.grade = String.valueOf(mGame.mGrade);
		if(mGame.gameOverflag == true)
		{
			gameOverDialog.replayFlag = true;
////			gameOverDialog.BtnReplay.setText("再玩一次");
		}
		else
		{
			gameOverDialog.replayFlag = false;
		}
		gameOverDialog.setListener(new OnCustomDialogListener1() {

			@Override
			public void replay(String name) {
				// TODO Auto-generated method stub
				mGame.mGrade = 0;
				mGame.lastmGrade = 0;
				mGame.gameOverflag = false;
				mGame.mStatus = GameStatus.WAITTING;
//				mGame.initPos();
			}

			@Override
			public void goto_adjust(String name) {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
				  		if(app.manager.isScanning){
				  			app.manager.stopScanBluetoothDevice();
				  		}
				  		if (app.manager.cubicBLEDevice != null) {
				  			if (app.manager.cubicBLEDevice.isConnect) {
//				  				manager.cubicBLEDevice.closeDevice();
				  				isSearch=false;
				  				app.manager.cubicBLEDevice.disconnectedDevice();
				  				isConnected=false;
				  				
				  			}
//				  			app.manager.cubicBLEDevice=null;
//				  			app.flag = true;
				  			
				  		}	
				        finish();
					}
				}, 1000);
			}
		});
		gameOverDialog.setCanceledOnTouchOutside(false);
		setDialogHeight(gameOverDialog);
		gameOverDialog.show();
//         super.onBackPressed();//注释掉这行,back键不退出activity
//        showToast("返回键");
//    	this.finish();
    }
	
	
}
