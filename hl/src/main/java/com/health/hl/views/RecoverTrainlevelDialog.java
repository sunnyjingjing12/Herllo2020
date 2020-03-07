package com.health.hl.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.health.hl.app.App;
import com.health.hl.recover.RecoverAdjustActivity;
import com.health.hl.recover.RecoverAnalysisActivity;
import com.health.hl.recover.RecoverMainActivity;
import com.health.hl.R;

import flapbird.Flapbird;

/**
 * 操作说明
 * 
 * @author xavi
 * 
 */
public class RecoverTrainlevelDialog extends Dialog implements SeekBar.OnSeekBarChangeListener {
	public OnCustomDialogListener getListener() {
		return listener;
	}

	public void setListener(OnCustomDialogListener listener) {
		this.listener = listener;
	}

	private Context context = null;
	private String title_name;
	private OnCustomDialogListener listener;
	private SeekBar sk_trainlevel = null;    //训练等级滑动条
	private TextView tv_trainlevel = null;
	private TextView tv_title = null;
	private SeekBar sk_traintime;            //训练时间滑动条
//	private App app;
	private TextView tv_traintime;
	public int trainlevel = 5;
	public int traintime = 5;
	public int traintype = 1;
	public String mplan = "A";
	public static final String TRAINLEVEL = "trainlevel";
	public static final String TRAINTIME = "traintime";
	public static final String TRAINTYPE = "traintype";  //训练方式，1为游戏，2为波形
	private String filename="userinfo";
	private CheckBox checkboxgame;
	private CheckBox checkboxwave;

	public RecoverTrainlevelDialog(Context context, String title_name) {
		super(context,R.style.DialogStyleRecover);
		this.context = context;
//		this.app = app;
		this.title_name = title_name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_recover_trainlevel);
		// 设置标题
		setTitle(title_name);
		Button BtnCancel = (Button) findViewById(R.id.btn_dialog_cancel);
		BtnCancel.setOnClickListener(clickListener);
		Button BtnStrartrain = (Button) findViewById(R.id.btn_dialog_starttrain);
		BtnStrartrain.setOnClickListener(clickListener);
		sk_trainlevel = (SeekBar) findViewById(R.id.sk_recover_dailog_trainlevel);
		sk_trainlevel.setOnSeekBarChangeListener(this);
		tv_trainlevel = (TextView) findViewById(R.id.tv_recover_dailog_trainlevel);
		sk_traintime = (SeekBar) findViewById(R.id.sk_recover_dailog_traintime);
		sk_traintime.setOnSeekBarChangeListener(this);
		tv_traintime = (TextView) findViewById(R.id.tv_recover_dailog_traintime);
		sk_trainlevel.setProgress(trainlevel);
		sk_traintime.setProgress(traintime);
		tv_title = (TextView) findViewById(R.id.tv_recover_dailog_title);
		tv_title.setText("方案"+title_name);
//		sk_trainlevel.setOnSeekBarChangeListener(this);
		
		// 训练复选框
		checkboxgame = (CheckBox) findViewById(R.id.ck_recover_trainlevel_game);
		checkboxwave = (CheckBox) findViewById(R.id.ck_recover_trainlevel_wave);
		checkboxgame.setOnCheckedChangeListener(checkboxlister);
		checkboxwave.setOnCheckedChangeListener(checkboxlister);
		if(traintype == 1)
		{
			checkboxgame.setChecked(true);
			checkboxwave.setChecked(false);
		}else
		{
			checkboxgame.setChecked(false);
			checkboxwave.setChecked(true);
		}
//		TextView tv_banben = (TextView) findViewById(R.id.tv_anfei_banben);
//		tv_banben.setText("版本：V"+getPackageInfo(getContext()).versionName);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			 case R.id.btn_dialog_cancel:
				 RecoverTrainlevelDialog.this.dismiss();
				 break;
			 case R.id.btn_dialog_starttrain:
				 SharedPreferences prefs = App.get().getSharedPreferences(filename,
							Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					//存储介绍状态
					editor.putString(TRAINLEVEL, String.valueOf(trainlevel));
					editor.putString(TRAINTIME, String.valueOf(traintime));
					editor.putString(TRAINTYPE, String.valueOf(traintype));
					editor.commit();
				 RecoverTrainlevelDialog.this.dismiss();
				 listener.back("");
				 break;
			 default:
				 break;
		
			}
		}
	};
	//版本名
		public static String getVersionName(Context context) {
		    return getPackageInfo(context).versionName;
		}
		 
		//版本号
		public static int getVersionCode(Context context) {
		    return getPackageInfo(context).versionCode;
		}
		 
		private static PackageInfo getPackageInfo(Context context) {
		    PackageInfo pi = null;
		 
		    try {
		        PackageManager pm = context.getPackageManager();
		        pi = pm.getPackageInfo(context.getPackageName(),
		                PackageManager.GET_CONFIGURATIONS);
		 
		        return pi;
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		 
		    return pi;
		}
	// 定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener {
		public void back(String name);
	}
	
	 // 数值改变
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId())
        {
			case R.id.sk_recover_dailog_trainlevel:
				if(progress == 0)
				{
					progress = 1;
					sk_trainlevel.setProgress(progress);
				}
				if(progress == 11)
				{
					progress = 10;
					sk_trainlevel.setProgress(progress);
				}
				sk_trainlevel.setSecondaryProgress(progress);
				tv_trainlevel.setText("当前数值:" + progress);
				trainlevel = progress;
				break;
			case R.id.sk_recover_dailog_traintime:
				if(progress <= 5)
				{
					progress = 5;
					sk_traintime.setProgress(progress);

				}else if(progress <= 10)
				{
					progress = 10;
					sk_traintime.setProgress(progress);
				}else if(progress <= 15)
				{
					progress = 15;
					sk_traintime.setProgress(progress);
				}else if(progress <= 20)
				{
					progress = 20;
					sk_traintime.setProgress(progress);
				}else if(progress <= 25)
				{
					progress = 25;
					sk_traintime.setProgress(progress);
				}
				sk_traintime.setSecondaryProgress(progress);
				tv_traintime.setText("当前数值:" + progress);
				traintime = progress;
				break;
			default:
				break;
        }

    }
	
 // 开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    // 停止拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
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
			if (checkboxgame.isPressed()) {
				checkboxwave.setChecked(false);
				checkboxgame.setChecked(true);
				traintype = 1;
			} else if (checkboxwave.isPressed()) {
				checkboxgame.setChecked(false);
				checkboxwave.setChecked(true);
				traintype = 2;
			}
		}

	};
    
}
