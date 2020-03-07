package com.health.hl.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.health.hl.R;
import com.health.hl.models.Config;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 操作说明
 * 
 * @author xavi
 * 
 */
public class DownloadDialog extends Dialog {
	public OnCustomDialogListener1 getListener() {
		return listener;
	}

	public void setListener(OnCustomDialogListener1 listener) {
		this.listener = listener;
	}

	public ProgressBar bar;
	public TextView tv_downloadData;
	public boolean replayFlag = false;
	private Context context = null;
	private String title_name;
	private OnCustomDialogListener1 listener;
	public String grade = "0";
//	private App app;
	private TextView textviewGrade;
	// 文件存储
	private File updateDir = null;
	private File updateFile = null;
	
	// 这样的下载代码很多，我就不做过多的说明
	int downloadCount = 0;
	int currentSize = 0;
	long totalSize = 0;
	int updateTotalSize = 0;
	

	public DownloadDialog(Context context, String title_name) {
		super(context);
		this.context = context;
//		this.app = app;
		this.title_name = title_name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailog_download);
		// 设置标题
		setTitle(title_name);
		bar = (ProgressBar) findViewById(R.id.dailog_download_Progress);
//		tv_banben.setText("版本：V"+getPackageInfo(getContext()).versionName);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
//			 case R.id.btn_dialog_replay:
//
//				 if(replayFlag)
//				 {
//					 listener.replay("");
//				 	 DownloadDialog.this.dismiss();
//				 }
//				 else
//				 {
//					 DownloadDialog.this.dismiss();
//				 }
////					 listener.continuePlay("");
//				 break;
//			 case R.id.btn_dialog_gameover:
//				 DownloadDialog.this.dismiss();
//				 listener.goto_adjust("");
////				Intent intent1 = new Intent(context,
////						RecoverAnalysisActivity.class);
////				// intent1.putExtra("levelvalue",spinner.getSelectedItem().toString());
////				// intent1.putExtra("timevalue",spinnertime.getSelectedItem().toString());
////				// intent1.putExtra("activity", "1");//1表示盆底肌测试
////				context.startActivity(intent1);
//				 break;
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
