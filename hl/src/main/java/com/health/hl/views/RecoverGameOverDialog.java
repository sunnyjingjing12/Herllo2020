package com.health.hl.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.health.hl.R;

/**
 * 操作说明
 * 
 * @author xavi
 * 
 */
public class RecoverGameOverDialog extends Dialog {
	public OnCustomDialogListener1 getListener() {
		return listener;
	}

	public void setListener(OnCustomDialogListener1 listener) {
		this.listener = listener;
	}

	public Button BtnReplay;
	public boolean replayFlag = false;
	private Context context = null;
	private String title_name;
	private OnCustomDialogListener1 listener;
	public String grade = "0";
//	private App app;
	private TextView textviewGrade;

	public RecoverGameOverDialog(Context context, String title_name) {
		super(context,R.style.DialogStyleRecover);
		this.context = context;
//		this.app = app;
		this.title_name = title_name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_recover_gameover);
		// 设置标题
		setTitle(title_name);
		BtnReplay = (Button) findViewById(R.id.btn_dialog_replay);
		BtnReplay.setOnClickListener(clickListener);
		if(replayFlag)
			BtnReplay.setText("再玩一次");
		else
			BtnReplay.setText("继续游戏");
		Button BtnGameOver = (Button) findViewById(R.id.btn_dialog_gameover);
		BtnGameOver.setOnClickListener(clickListener);
		textviewGrade = (TextView) findViewById(R.id.tv_game_report);
		textviewGrade.setText("游戏已完成，本次得分为"+grade+"!");
//		tv_banben.setText("版本：V"+getPackageInfo(getContext()).versionName);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
			 case R.id.btn_dialog_replay:

				 if(replayFlag)
				 {
					 listener.replay("");
				 	 RecoverGameOverDialog.this.dismiss();
				 }
				 else
				 {
					 RecoverGameOverDialog.this.dismiss();
				 }
//					 listener.continuePlay("");
				 break;
			 case R.id.btn_dialog_gameover:
				 RecoverGameOverDialog.this.dismiss();
				 listener.goto_adjust("");
//				Intent intent1 = new Intent(context,
//						RecoverAnalysisActivity.class);
//				// intent1.putExtra("levelvalue",spinner.getSelectedItem().toString());
//				// intent1.putExtra("timevalue",spinnertime.getSelectedItem().toString());
//				// intent1.putExtra("activity", "1");//1表示盆底肌测试
//				context.startActivity(intent1);
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
	public interface OnCustomDialogListener1 {
		public void replay(String name);
		public void goto_adjust(String name);
	}
}
