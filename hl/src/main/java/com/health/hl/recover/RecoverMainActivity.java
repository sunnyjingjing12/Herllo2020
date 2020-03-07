package com.health.hl.recover;

/** page：用户信息主页
 *  aditor：zhou
 *  date:20170412
 *  describe:
 *  从本地文件读取用户信息进行显示，从此界面可以进入设备使用
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.health.hl.R;
import com.health.hl.app.App;
import com.health.hl.models.Config;
import com.health.hl.util.ToastUtils;
import com.health.hl.views.DownloadDialog;
import com.health.hl.views.RecoverAdduserDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class RecoverMainActivity extends Activity implements
OnClickListener{


	private ImageView setAccount;    //添加用户按钮
	DecimalFormat decimalFormat = new DecimalFormat("0.0");  //数据显示格式
	public boolean isSearch = true;    //是否在扫描蓝牙
	private Button startTest;  //开始使用设备按钮
	private Button recoverdevice;//康复器
	private Button trainer;//训练仪

	private String userPhone;   //用户电话
	private String userName;   //用户名
	private String version = "1.1.1.1";   //用户名
	private String userWeight;  //用户体重
	private String userHeight;  //用户身高
	private String userAge;     //用户年龄
//	private String userSex;     //用户性别
	private String ImLevel;     //I类肌等级
	private String IImLevel;    //II类肌等级
	//网络接口相关
	private String userId="0";//用户id
	private String userAid="0";//用户aId
	private String code; //网络返回码
	private String netPath;//接口网址
	private String netPara;//接口参数
	private String userData;//接口参数
	private String versionData;//接口参数
	private String muscleData;//接口参数
	private TextView et_userName;   //用户名内容显示
	private TextView et_userWeight; //用户体重内容显示
	private TextView et_userHeight; //用户身高内容显示
	private TextView et_userAge;    //用户年龄内容显示
	private TextView et_mLevel;     //肌力等级内容显示
	private TextView et_maxPower;   //最大肌力内容显示
	private TextView et_trainPlan;  //训练方案内容显示
	private String mPlan = "-";       //训练方案
	private String mPlanCode = "-";   //训练方案编码
	private String maxPower = "-";    //最大肌力
	private Button checkReport; //查看报告按钮
	private TextView et_userWeightName;   //用户体重标题
	private TextView et_userHeightName;   //用户高度标题
	private TextView et_userAgeName;      //用户年龄标题
	private Toast customToast; // 消息显示框
	private ImageView goToRanking;
	// 文件存储
	private File updateDir = null;
	private File updateFile = null;
	private LocationManager lm;
	private static final int PRIVATE_CODE = 1315;//开启GPS权限
 
	
	// 这样的下载代码很多，我就不做过多的说明
	int downloadCount = 0;
	int currentSize = 0;
	long totalSize = 0;
	int updateTotalSize = 0;
	
	DownloadDialog a = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		showGPSContacts();//添加地址权限判断20190822晶
		initView();
		initData();
		initEvent();
		
	}
	
	
	@Override
	protected void onResume() {
		reLoad();   //读取本地用户数据
		super.onResume();		
	}

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initView() {
		setContentView(R.layout.activity_recover_main);
	}

	
	
	private void initData() {	
		//开始测试按钮
		startTest = (Button) findViewById(R.id.btn_recover_starttest);
		startTest.setOnClickListener(this);
		//查看报告按钮201909
		checkReport = (Button) findViewById(R.id.btn_recover_report);
		checkReport.setOnClickListener(this);
		//康复器按钮201910
		recoverdevice =findViewById(R.id.btn_recover_recoverdevice);
		recoverdevice.setOnClickListener(this);
		//训练仪按钮201910
		trainer =findViewById(R.id.btn_recover_trainer);
		trainer.setOnClickListener(this);
        //添加用户按钮
		setAccount = (ImageView)findViewById(R.id.iv_recover_main_account);
		setAccount.setOnClickListener(this);
		//用户基本信息显示
		et_userName = (TextView)findViewById(R.id.et_recover_main_name);
		et_userWeight = (TextView)findViewById(R.id.et_recover_main_weight);
		et_userWeightName = (TextView)findViewById(R.id.et_recover_main_weightname);
		et_userHeight = (TextView)findViewById(R.id.et_recover_main_height);
		et_userHeightName = (TextView)findViewById(R.id.et_recover_main_heightname);
		et_userAge = (TextView)findViewById(R.id.et_recover_main_age);
		et_userAgeName = (TextView)findViewById(R.id.et_recover_main_agename);
		et_mLevel = (TextView)findViewById(R.id.et_recover_main_mlevel);
		et_maxPower = (TextView)findViewById(R.id.et_recover_main_maxpower);
		et_trainPlan = (TextView)findViewById(R.id.et_recover_main_trainplan);
		//排行榜按钮
		goToRanking = (ImageView)findViewById(R.id.iv_recover_main_ranking);
		goToRanking.setOnClickListener(this);
		goToRanking.setVisibility(View.GONE);
	}

	private void initEvent() {
		// TODO Auto-generated method stub
//		updateApplication();
	}

	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0.getId()) {
		//进入测试界面
		case R.id.btn_recover_starttest:
			   //如果未添加用户则显示添加用户提醒框
		       if(userWeight == "-")
		       {
					//创建添加用户对话框
					RecoverAdduserDialog AddUserDialog = null;//蓝牙断开提示框
					AddUserDialog = new RecoverAdduserDialog(RecoverMainActivity.this,"添加用户");
					setDialogHeight(AddUserDialog);
					AddUserDialog.show();
		       }else
		       {
					Intent intent1 = new Intent(RecoverMainActivity.this,
							RecoverAdjustActivity.class);
					//必须要传用户ID、最大肌力、训练方案、训练方案编码到下一个页面
					intent1.putExtra("userid",userId);
					intent1.putExtra("maxpower",maxPower);
					intent1.putExtra("mplan",mPlan);
					intent1.putExtra("mplancode",mPlanCode);
					intent1.putExtra("activity", "1");//1表示盆底肌测试
					intent1.putExtra("userweight",userWeight);//1表示盆底肌测试
					intent1.putExtra("userphone",userPhone);//晶
					startActivity(intent1);
//		    	   showToast(maxpower);
		       }
			break;

			//进入查看报告
			case R.id.btn_recover_report:
				Intent intent1 = new Intent(RecoverMainActivity.this,
						RecoverAnalysisActivity.class);
				//进入查看报告界面,必须要传用户ID到下一个页面
				intent1.putExtra("userid",userId);//date：20190820，author：晶，传入用户资料
				intent1.putExtra("userphone",userPhone);//晶
				intent1.putExtra("userweight",userWeight);//1表示盆底肌测试
				intent1.putExtra("mplancode",mPlanCode);
				startActivity(intent1);
			break;

			//进入康复器界面（进行活动界面跳转）
			case R.id.btn_recover_recoverdevice:
				Intent intent2 = new Intent(RecoverMainActivity.this,
						RecoverDeviceActivity.class);
				startActivity(intent2);
			break;

			//进入新建账户界面
			case R.id.iv_recover_main_account:
				Intent intent3 = new Intent(RecoverMainActivity.this,
						RecoverAccountActivity.class);
				startActivity(intent3);
			break;
			//进入排行榜界面
			case R.id.iv_recover_main_ranking:
			       if(userWeight == "-")
			       {
						//创建添加用户对话框
						RecoverAdduserDialog AddUserDialog = null;//蓝牙断开提示框
						AddUserDialog = new RecoverAdduserDialog(RecoverMainActivity.this,"添加用户");
						setDialogHeight(AddUserDialog);
						AddUserDialog.show();
			       }else
			       {
						Intent intent4 = new Intent(RecoverMainActivity.this,
								RecoverRankingActivity.class);
						//传用户ID到排行榜页面
						intent4.putExtra("userid",userId);
						startActivity(intent4);
			       }
			break;
			default:
			break;
		}
		
	}
	
	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	public void reLoad() {
        SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );
       
       userPhone = prefs.getString("phone", "-");
       userName = "-";
       userWeight = "-";
       userHeight = "-";
       userAge = "-";
       ImLevel = "-";
       IImLevel = "-";
       maxPower = "-";
       mPlan = "-";

       //从网络读取用户信息
       if(!userPhone.equals("-"))
		{
			userName = "-";
			userWeight = "-";
			userHeight = "-";
			userAge = "-";
			startTest.setClickable(false);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						userData = getJson("http://119.23.248.54/Ls_Server_web/userinfo/findUserByPhone?phone="+userPhone);
						userDataShow(userData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
//						e.printStackTrace();
					}
				}
			}).start();
			//延迟一秒根据用户基本信息再读取用户肌力测试信息
			new Handler().postDelayed(new Runnable() {						
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(!userName.equals("-"))
					{
						et_userName.setText(userName);
					    et_userWeight.setText(userWeight);
					    et_userHeight.setText(userHeight);
					    et_userAge.setText(userAge);
					}else
					{
						showToast("网络环境差，读取数据超时！正在重新读取。。。");
						reLoad();
					}
				    userData = "0";
				    new Thread(new Runnable() {				
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								muscleData = getJson("http://119.23.248.54/Ls_Server_web/mstest/findMsTestRecordByuId?uId="+userId);
								testDataShow(muscleData);
							} catch (Exception e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
							}
						}
					}).start();
				}
			}, 1000);
			new Handler().postDelayed(new Runnable() {						
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(Character.isDigit(ImLevel.charAt(0)))
					{
						et_mLevel.setText("I类"+ImLevel+"级"+"  "+"II类"+IImLevel+"级");
						et_maxPower.setText(decimalFormat.format(Double.valueOf(maxPower))+"N");
						et_trainPlan.setText(mPlan+"方案");
						checkReport.setVisibility(View.VISIBLE);
				    	checkReport.setClickable(true);
				    	LinearLayout.LayoutParams layoutParams;
				    	layoutParams = (LinearLayout.LayoutParams) startTest.getLayoutParams();
				    	layoutParams.setMargins(19, 0, 0, 0);
				    	startTest.setLayoutParams(layoutParams);
					}
					startTest.setClickable(true);
				}
			}, 2000);
			
		}
       
       //未添加用户信息,则单位内容隐藏
       if(userWeight.equals("-"))
       {
    	   et_userHeightName.setVisibility(View.GONE);
    	   et_userWeightName.setVisibility(View.GONE);
    	   et_userAgeName.setVisibility(View.GONE);
       }else
       {
    	   et_userHeightName.setVisibility(View.VISIBLE);
    	   et_userWeightName.setVisibility(View.VISIBLE);
    	   et_userAgeName.setVisibility(View.VISIBLE);
       }
       
       et_userName.setText(userName);
       et_userWeight.setText(userWeight);
       et_userHeight.setText(userHeight);
       et_userAge.setText(userAge);
       //如果未进行肌力测试,肌力相关单位内容隐藏
       if(!maxPower.equals("-"))
    	   et_maxPower.setText(decimalFormat.format(Double.valueOf(maxPower))+"N");
       else
    	   et_maxPower.setText("-");
       
       //如果未进行测试,报告按钮隐藏
       if(!mPlan.equals("-"))
       {
    	   et_trainPlan.setText(mPlan+"方案");
//    	   checkreport.setVisibility(View.VISIBLE);
//    	   checkreport.setClickable(true);
//    	   LinearLayout.LayoutParams layoutParams;
//    	   layoutParams = (LinearLayout.LayoutParams) starttest.getLayoutParams();
//    	   layoutParams.setMargins(19, 0, 0, 0);
//    	   starttest.setLayoutParams(layoutParams);
       }
       else
       {
    	   et_trainPlan.setText("-");
    	   checkReport.setVisibility(View.GONE);
    	   checkReport.setClickable(false);
       }

       if(!ImLevel.equals("-"))
    	   et_mLevel.setText("I类"+ImLevel+"级"+"  "+"II类"+IImLevel+"级");
       else
    	   et_mLevel.setText("-");
       
       
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
		lp.height = (int) (d.getHeight() * 0.4); // 高度
		dialogWindow.setAttributes(lp);
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
            a = conn.getResponseCode();
//            a = 200;
            if(a == 200)
            {
	                //获取服务器返回信息
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            int inputcode = 0;
	            inputLine = reader.readLine();
//	                //截取CODE码用于识别状况
//	            inputcode = inputLine.indexOf("age");
//	            char b= inputLine.charAt(inputcode);
//	            char c= inputLine.charAt(0);
	            reader.close();
            }else
            {
            	return "0";
            }
//            a = conn.getResponseCode();
            return inputLine;
    }
	
    /**
   	 * fun：服务器返回数据 aditor：zhou date:20170911 describe: 使用遍历方式,data为服务器返回的数据
   	 */
    private void userDataShow(String data)
    {
		userAid = "0";
		userId = "0";
    	int a=0;
    	a = data.indexOf("userName");
    	a += 10;
    	if(data.charAt(a)=='"')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(data.charAt(i)!='"')
    			{
    				if(i==(a+1))
    					userName = data.charAt(i)+"";
    				else
    					userName += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = data.indexOf("age");
    	a += 4;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					userAge = data.charAt(i)+"";
    				else
    					userAge += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = data.indexOf("height");
    	a += 7;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					userHeight = data.charAt(i)+"";
    				else
    					userHeight += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = data.indexOf("weight");
    	a += 7;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					userWeight = data.charAt(i)+"";
    				else
    					userWeight += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = data.indexOf("currentState");
    	a += 14;
    	a = data.indexOf("id");
    	a += 3;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				userId += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = data.indexOf("aId");
    	a += 4;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				userAid += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    }
    
    /**
   	 * fun：服务器返回数据 aditor：zhou date:20170911 describe: 使用遍历方式,data为服务器返回的数据
   	 */
    private void testDataShow(String data)
    {
//    	Imlevel = "";
//    	IImlevel = "";
//		maxpower = "";
//		mplan = "";

	    	int a=0;
	    	a = data.indexOf("oneMuscle");
	    	a += 10;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(Character.isDigit(data.charAt(i)))
	    			{
	    				if(i==(a+1))
	    					ImLevel = data.charAt(i)+"";
	    				else
	    					ImLevel += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = data.indexOf("twoMuscle");
	    	a += 10;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(Character.isDigit(data.charAt(i)))
	    			{
	    				if(i==(a+1))
	    					IImLevel = data.charAt(i)+"";
	    				else
	    					IImLevel += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = data.indexOf("caseName");
	    	a += 10;
	    	if(data.charAt(a)=='"')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!='"')
	    			{
	    				if(i==(a+1))
	    					mPlan = data.charAt(i)+"";
	    				else
	    					mPlan += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = data.indexOf("caseCode");
	    	a += 10;
	    	if(data.charAt(a)=='"')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(Character.isDigit(data.charAt(i)))
	    			{
	    				if(i==(a+1))
	    					mPlanCode = data.charAt(i)+"";
	    				else
	    					mPlanCode += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = data.indexOf("maxPower");
	    	a += 9;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!=',')
	    			{
	    				if(i==(a+1))
	    					maxPower = data.charAt(i)+"";
	    				else
	    					maxPower += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
    	}
    
  //显示消息
//  	private void showToast(String text) {
//  		if (customToast == null) {
//  			customToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//  		} else {
//  			customToast.setText(text);
//  			customToast.setDuration(Toast.LENGTH_LONG);
//  		}
//  		customToast.setGravity(Gravity.CENTER, 0, 0);
//  		customToast.show();
//  	}
  public void showToast(String text) {
	  //		ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
	  try {
		  ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
		  customToast.setGravity(Gravity.CENTER, 0, 0);
		  //        customToast.show();
	  } catch (Exception e) {}
  }
    
  	/** fun：检查是否需要更新APP
	 *  aditor：zhou
	 *  date:20180905
	 *  describe:
	 *  对比版本号，看是否需要更新APP
	 */
	public void updateApplication() {
		reLoadVersion();
		SystemClock.sleep(1000);
		if((!version.equals("1.1.1.4"))) {
//			Log.i("hgncxzy", "==============================");
			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("软件升级")
					.setMessage("发现新版本,建议立即更新以体验新功能.安装包大小7M，预计更新时间10秒钟")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									a = new DownloadDialog(RecoverMainActivity.this, "正在下载");
									a.show();
									if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
											.getExternalStorageState())) {
										updateDir = new File(Environment.getExternalStorageDirectory(),
												Config.saveFileName);
										updateFile = new File(updateDir.getPath(), "hl.apk");
									}
									new Thread(new updateRunnable()).start();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			alert.create().show();
					}
//		} else {
//			// 清理工作，略去
//			// cheanUpdateFile(),文章后面我会附上代码
//		}
	}
  	
	public long downloadUpdateFile(String downloadUrl, File saveFile)
			throws Exception {

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("fail!");
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[4096];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
//				a.show();
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
//				a.tv_downloadData.setText(totalSize*100+"/"+updateTotalSize);
				// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
				if ((downloadCount == 0)
						|| (int) (totalSize * 100 / updateTotalSize) - 1 > downloadCount) {
					downloadCount += 1;
//					a.tv_downloadData.setText("/");
					a.bar.setProgress(downloadCount);
//					a.tv_downloadData.setText(downloadCount+"");
				}
			}
			a.bar.setProgress(100);
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}
	
	class updateRunnable implements Runnable {
//		Message message = updateHandler.obtainMessage();

		public void run() {
//			message.what = DOWNLOAD_COMPLETE;
			
			
			try {
				// 增加权限<USES-PERMISSION
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
				// 下载函数，以QQ为例子
				// 增加权限<USES-PERMISSION
				// android:name="android.permission.INTERNET">;
				long downloadSize = downloadUpdateFile(
						"http://119.23.248.54:8080/packs/2018-7-17/hl.apk",
						updateFile);
				if (downloadSize > 0) {
					// 下载成功
//					updateHandler.sendMessage(message);
//					showToast("下载成功");
					Intent intent = new Intent();   
			        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
			        intent.setAction(android.content.Intent.ACTION_VIEW);   
			        intent.setDataAndType(Uri.fromFile(updateFile),   
			                        "application/vnd.android.package-archive");   
			        startActivity(intent);   
				} 
			} catch (Exception ex) {
				ex.printStackTrace();
//				message.what = DOWNLOAD_FAIL;
//				// 下载失败
//				updateHandler.sendMessage(message);
			}
		}
	}
	
	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	public void reLoadVersion() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					versionData = getJson("http://119.23.248.54/Ls_Server_web/appVersion/searchNewVersion?typeid=1");
//					userData = getJson("http://119.23.248.54/Ls_Server_web/userinfo/findUserByPhone?phone="+userPhone);
					versionDataShow(versionData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		}).start();		
	}
    /**
   	 * fun：服务器返回数据 aditor：zhou date:20170911 describe: 使用遍历方式,data为服务器返回的数据
   	 */
    private void versionDataShow(String data)
    {
    	int a=0;
    	a = data.indexOf("versions");
    	a += 11;
    	a = data.indexOf("versions",a);
    	a += 10;
    	if(data.charAt(a)=='"')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(data.charAt(i)!='"')
    			{
    				if(i==(a+1))
    					version = data.charAt(i)+"";
    				else
    					version += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = 0;
    }
    
    //返回键处理
//    @Override
//    public void onBackPressed() {
////         super.onBackPressed();//注释掉这行,back键不退出activity
////        showToast("返回键");
//    	this.finish();
//    }
    
	@Override  
	public boolean onKeyDown(int keyCode,KeyEvent event){  
	    if(keyCode==KeyEvent.KEYCODE_BACK){   
	        moveTaskToBack(true);
	        return true;//不执行父类点击事件  
	    }
	    return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件  
	}  
}
