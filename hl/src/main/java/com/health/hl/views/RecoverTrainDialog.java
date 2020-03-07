package com.health.hl.views;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.health.hl.views.RecoverReportDialog.OnCustomDialogListener;
import com.health.hl.R;

/**
 * 操作说明
 * 
 * @author xavi
 * 
 */
public class RecoverTrainDialog extends Dialog {
	private Context context = null;
	private String title_name;
	private OnCustomDialogListener listener;
//	private App app;

	public OnCustomDialogListener getListener() {
		return listener;
	}

	public void setListener(OnCustomDialogListener listener) {
		this.listener = listener;
	}
	
	public RecoverTrainDialog(Context context, String title_name) {
		super(context,R.style.DialogStyleRecover);
		this.context = context;
//		this.app = app;
		this.title_name = title_name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_recover_train);
		// 设置标题
		setTitle(title_name);
//		Button BtnCancel = (Button) findViewById(R.id.btn_dialog_cancel);
//		BtnCancel.setOnClickListener(clickListener);
		Button BtnRescan = (Button) findViewById(R.id.btn_dialog_know);
		BtnRescan.setOnClickListener(clickListener);
		Button BtnRescan1 = (Button) findViewById(R.id.btn_dialog_know1);
		BtnRescan1.setOnClickListener(clickListener);
//		TextView tv_banben = (TextView) findViewById(R.id.tv_anfei_banben);
//		tv_banben.setText("版本：V"+getPackageInfo(getContext()).versionName);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
//			 case R.id.btn_dialog_cancel:
//				 AnfeiAboutDialog.this.dismiss();
//				 break;
			 case R.id.btn_dialog_know:
				 RecoverTrainDialog.this.dismiss();
				 listener.back("");
				 break;
			 case R.id.btn_dialog_know1:
				 RecoverTrainDialog.this.dismiss();
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
}
