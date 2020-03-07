package com.health.hl.recover;

/** page：添加用户信息页面
 *  aditor：zhou
 *  date:20170412
 *  describe:
 *  添加或者修改用户信息,若修改用户信息会先从本地SP文件读取用户信息
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.health.hl.R;
import com.health.hl.app.App;
import com.health.hl.util.ToastUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecoverAccountActivity extends Activity implements
		OnClickListener, OnTouchListener ,OnEditorActionListener {


	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		// 返回上一页
		case R.id.et_recover_account_phone:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			break;
		case R.id.et_recover_account_name:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			break;
		case R.id.et_recover_account_weight:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			break;
		case R.id.et_recover_account_height:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			break;
		case R.id.et_recover_account_age:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			break;
		default:
			break;
		}
		return false;
	}

	private ImageView accountBack; // 返回按钮
	private Button accountSave; // 保存按钮
	private CheckBox checkboxPregnant; // 孕期复选框
	private CheckBox checkboxPost; // 产后复选框
	private CheckBox checkboxMan; // 男性复选框
	private Toast customToast; // 消息显示框
	private String filename = "userinfo"; // 本地存储SP文件名称
	private EditText accountPhone; // 手机编辑框
	private EditText accountName; // 姓名编辑框
	private EditText accountWeight; // 体重编辑框
	private EditText accountHeight; // 高度编辑框
	private EditText accountAge; // 年龄编辑框
	private String userPhone=""; // 姓名手机
	private String userName=""; // 姓名内容
	private String userWeight=""; // 体重内容
	private String userHeight=""; // 身高内容
	private String userAge=""; // 年龄内容
	private String userState="1"; // 身体状态内容
	private String userId="0";//用户id 
	private String userAid="0";//用户aId
	private String code; //网络返回码,用于判断各种数据状态
	private String netPath;//接口网址
	private String netPara;//接口参数
	private String userData;//服务器返回的所有信息总和
	public static final String PHONE = "phone"; // SP文件名称键值对
	public static final String NAME = "name"; // SP文件名称键值对
	public static final String WEIGHT = "weight"; // SP文件体重键值对
	public static final String HEIGHT = "height"; // SP文件身高键值对
	public static final String AGE = "age"; // SP文件年龄键值对
	public static final String SEX = "sex"; // SP文件性别键值对
	private InputMethodManager manager;  
	private boolean netflag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		manager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		initEvent();
		initData();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		// 返回按钮
		accountBack = (ImageView) findViewById(R.id.iv_recover_account_back);
		accountBack.setOnClickListener(this);
		// 存储按钮
		accountSave = (Button) findViewById(R.id.btn_recover_account_save);
		accountSave.setOnClickListener(this);

		// 性别复选框
		checkboxPregnant = (CheckBox) findViewById(R.id.ck_recover_account_pregnant);
		checkboxPost = (CheckBox) findViewById(R.id.ck_recover_account_post);
		checkboxMan = (CheckBox) findViewById(R.id.ck_recover_account_man);
		checkboxPregnant.setOnCheckedChangeListener(checkboxlister);
		checkboxPost.setOnCheckedChangeListener(checkboxlister);
		checkboxMan.setOnCheckedChangeListener(checkboxlister);
		// 用户手机
		accountPhone = (EditText) findViewById(R.id.et_recover_account_phone);
		accountPhone.setOnEditorActionListener(this);
		accountPhone.setOnTouchListener(this);	
		// 用户姓名
		accountName = (EditText) findViewById(R.id.et_recover_account_name);
		accountName.setOnEditorActionListener(this);
		accountName.setOnTouchListener(this);
		// 用户体重
		accountWeight = (EditText) findViewById(R.id.et_recover_account_weight);
		accountWeight.setOnEditorActionListener(this);
		accountWeight.setOnTouchListener(this);
		// 用户身高
		accountHeight = (EditText) findViewById(R.id.et_recover_account_height);
		accountHeight.setOnEditorActionListener(this);
		accountHeight.setOnTouchListener(this);
		// 用户年龄
		accountAge = (EditText) findViewById(R.id.et_recover_account_age);
		accountAge.setOnEditorActionListener(this);
		accountAge.setOnTouchListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		// 从SP文件读取用户信息
		reLoad();
		//判断手机是否为空,若不为空则向服务器读取数据
		if(!userPhone.equals(""))
		{
			userName = "-";
			userWeight = "-";
			userHeight = "-";
			userAge = "-";
			//新建线程,与服务器交互数据必须在新建线程中完成,否则会报错
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						//读取服务器用户基本信息
						userData = getJson("http://119.23.248.54/Ls_Server_web/userinfo/findUserByPhone?phone="+userPhone);
						//数据解析
						dataShow(userData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}
				}
			}).start();
			//数据读取需要一定的时间,延迟1秒后再进行相应的处理
			new Handler().postDelayed(new Runnable() {						
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(!userName.equals("-"))
					{
						accountName.setText(userName);
						accountWeight.setText(userWeight);
						accountHeight.setText(userHeight);
						accountAge.setText(userAge);
					}else
					{
						showToast("网络环境差，读取数据超时！正在重新读取。。。");
						initData();
					}
					if(userState.equals("1"))
					{
						checkboxPregnant.setChecked(false);
						checkboxPost.setChecked(true);
						checkboxMan.setChecked(false);
					}else if(userState.equals("0"))
					{
						checkboxPregnant.setChecked(true);
						checkboxPost.setChecked(false);
						checkboxMan.setChecked(false);
					}else 
					{
						checkboxPregnant.setChecked(false);
						checkboxPost.setChecked(false);
						checkboxMan.setChecked(true);
					}
				}
			}, 1000);
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_recover_account);
	}

	/**
	 * fun：复选框监听 aditor：zhou date:20170412 describe: 选择怀孕或者产后的状态切换
	 */
	private CheckBox.OnCheckedChangeListener checkboxlister = new CheckBox.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			// 怀孕或者产后状态选择
			if (checkboxPregnant.isPressed()) {
				checkboxPost.setChecked(false);
				checkboxPregnant.setChecked(true);
				checkboxMan.setChecked(false);
				userState = "0";
			} else if (checkboxPost.isPressed()) {
				checkboxPregnant.setChecked(false);
				checkboxPost.setChecked(true);
				checkboxMan.setChecked(false);
				userState = "1";
			} else if (checkboxMan.isPressed())
			{
				checkboxPregnant.setChecked(false);
				checkboxPost.setChecked(false);
				checkboxMan.setChecked(true);
				userState = "2";
			}
		}

	};

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		// 返回上一页
		case R.id.iv_recover_account_back:
			finish();
			break;
		// 保存用户
		case R.id.btn_recover_account_save:
			Boolean a = dataSave(); // 存储信息
			if(a)
			{
				//若useraid为0则表示为新用户则对接用户信息新建接口
				if(userAid.equals("0"))
				{
					netPath = "http://119.23.248.54/Ls_Server_web/userinfo/save";
//				    showToast(netPath+"?"+netPara);
				}
				//若useraid不为0则表示为旧用户则对接用户信息更新接口
				else
				{
					netPath = "http://119.23.248.54/Ls_Server_web/userinfo/update";
					netPara += "&aId="+userAid;
					userAid = "0";
//					showToast("2");
			}

			code = null;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							code = postDownloadJson(netPath,netPara);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//如果与服务器连接失败,则报错
						}
					}
				}).start();
				//数据对接完成后,延迟一秒回到上一个界面
				new Handler().postDelayed(new Runnable() {						
					@Override
					public void run() {
						// TODO Auto-generated method stub
							finish();
					}
				}, 2000);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		
		case R.id.et_recover_account_phone:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			break;
		case R.id.et_recover_account_name:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			
			//若用户手机不为空则进行用户信息读取
			userPhone = accountPhone.getText().toString();
			if(!userPhone.equals(""))
			{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							userData = getJson("http://119.23.248.54/Ls_Server_web/userinfo/findUserByPhone?phone="+userPhone);
							if(!userData.equals("wrong"))
								dataShow(userData);
//							codeanalysis(code);
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
					}
				}).start();
					new Handler().postDelayed(new Runnable() {						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(!userAid.equals("0"))
							{
								accountName.setText(userName);
								accountWeight.setText(userWeight);
								accountHeight.setText(userHeight);
								accountAge.setText(userAge);
								if(userState.equals("1"))
								{
									checkboxPregnant.setChecked(false);
									checkboxPost.setChecked(true);
									checkboxMan.setChecked(false);
								}else if(userState.equals("0"))
								{
									checkboxPregnant.setChecked(true);
									checkboxPost.setChecked(false);
									checkboxMan.setChecked(false);
								}else
								{
									checkboxPregnant.setChecked(false);
									checkboxPost.setChecked(false);
									checkboxMan.setChecked(true);
								}
								showToast("成功读取用户信息");
								//设置用户姓名光标至最后一位
								accountName.setFocusable(true);
								accountName.setSelection(userName.length());
							}else
							{
								accountName.setText("");
								accountWeight.setText("");
								accountHeight.setText("");
								accountAge.setText("");
								checkboxPregnant.setChecked(false);
								checkboxPost.setChecked(true);
								checkboxMan.setChecked(false);
								accountName.setFocusable(true);
							}
							
						}
					}, 1000);
			}
			break;
		case R.id.et_recover_account_weight:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			break;
		case R.id.et_recover_account_height:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			break;
		case R.id.et_recover_account_age:
			accountPhone.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountName.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountWeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountHeight.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_editnone));
			accountAge.setBackground(getResources().getDrawable(R.drawable.bg_recover_accountpage_edit));
			break;
		default:
			break;
		}
		return false;
	}
	
	/**
	 * fun：数据存储 aditor：zhou date:20170412 describe: 把用户信息存储在本地SP文件中
	 */
	public boolean dataSave() {
		// 读取文件
		SharedPreferences prefs = App.get().getSharedPreferences(filename,
				Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = prefs.edit();

		// 判断各信息编辑栏是否为空,如果为空提醒信息
		if (accountPhone.getText().toString().trim().equals("")) {
			showToast(getApplicationContext().getResources().getString(
					R.string.recover_account_main_emptyphone));
			return false;
		} else if(accountPhone.getText().length()!=11)
		{
			showToast(getApplicationContext().getResources().getString(
					R.string.recover_account_main_wrongphone));
			return false;
		}else if (accountName.getText().toString().trim().equals("")) {
			showToast(getApplicationContext().getResources().getString(
					R.string.recover_account_main_emptyname));
			return false;
		} else if (accountWeight.getText().toString().trim().equals("")) {
			showToast(getApplicationContext().getResources().getString(
					R.string.recover_account_main_emptyweight));
			return false;
		} else if (accountHeight.getText().toString().trim().equals("")) {
			showToast(getApplicationContext().getResources().getString(
					R.string.recover_account_main_emptyheight));
			return false;
		} else if (accountAge.getText().toString().trim().equals("")) {
			showToast(getApplicationContext().getResources().getString(
					R.string.recover_account_main_emptyage));
			return false;
		} else {
			// 存储信息
			editor.putString(PHONE, accountPhone.getText().toString());
			editor.putString(NAME, accountName.getText().toString());
			editor.putString(WEIGHT, accountWeight.getText().toString());
			editor.putString(HEIGHT, accountHeight.getText().toString());
			editor.putString(AGE, accountAge.getText().toString());
			//拼凑接口参数
			netPara = "phone="+accountPhone.getText().toString()+"&"+"userName="+accountName.getText().toString()
					+"&"+"userSex=1"+"&"+"height="+accountHeight.getText().toString()+"&"+"weight="+
					accountWeight.getText().toString()+"&"+"age="+accountAge.getText().toString()+"&currentState="+userState;
			return editor.commit();
		}
	}

	
	/**
	 * fun：显示消息（Toast） aditor：zhou date:20170412 describe: 显示相应消息内容
	 */
//	private void showToast(String text) {
//		if (customToast == null) {
//			customToast = Toast.makeText(getApplicationContext(), text,
//					Toast.LENGTH_SHORT);
//		} else {
//			customToast.setText(text);
//			customToast.setDuration(Toast.LENGTH_LONG);
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
	/**
	 * fun：读取本地用户信息 aditor：zhou date:20170412 describe: 若有SP文件则从本地读取用户信息进行显示，目前只读取用户手机用户获取服务器数据
	 */
	public void reLoad() {
		
		SharedPreferences prefs = App.get().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		userPhone = prefs.getString("phone", "");
		accountPhone.setText(userPhone);

	}

	@Override 
	public boolean onTouchEvent(MotionEvent event) {   
	// TODO Auto-generated method stub   
	if(event.getAction() == MotionEvent.ACTION_DOWN){     
	if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){      
	manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);     
	}   
	}   
	return super.onTouchEvent(event);  
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
            String inputcode = inputLine.substring(8, 12);
            reader.close();
            return inputcode;
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
            //服务器返回码,若为200则连接成功
//            a = conn.getResponseCode();
            a = 200;
            if(a == 200)
            {
	                //获取服务器返回信息
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            int inputcode = 0;
	            inputLine = reader.readLine();
//	                //截取CODE码用于识别状况
	            inputcode = inputLine.indexOf("code");
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
	 * fun：服务器返回码解析 aditor：zhou date:20170911 describe: 暂未使用，
	 */
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
    	}
    	if((code.equals("200,"))||(code.equals("1003")))
    	{
    		netflag = true;
    	}else
    	{
    		netflag = false;
    	}
    }
    
    /**
	 * fun：服务器返回数据 aditor：zhou date:20170911 describe: 使用遍历方式,data为服务器返回的数据
	 */
    private void dataShow(String data)
    {
		userName = "";
		userWeight = "";
		userHeight = "";
		userAge = "";
		userAid = "0";
		userId = "0";
    	int a=0;
    	//寻找到相应字段的所处位数
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
    			//判断若为数字则为所需数据
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
    	if(data.charAt(a)=='"')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					userState = data.charAt(i)+"";
    				else
    					userState += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    	a = data.indexOf("id");
    	a += 3;
    	if(data.charAt(a)==':')
    	{
    		for(int i=a+1;i<data.length()-1;i++)
    		{
    			if(Character.isDigit(data.charAt(i)))
    			{
    				if(i==(a+1))
    					userId = data.charAt(i)+"";
    				else
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
    				if(i==(a+1))
    					userAid = data.charAt(i)+"";
    				else
    					userAid += data.charAt(i);
    			}else
    			{
    				break;
    			}
    		}
    	}
    }
    
}
