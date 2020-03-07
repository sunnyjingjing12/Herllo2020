package com.health.hl.recover;

import com.health.hl.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class RecoverStartpageActivity extends Activity{

	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		setContentView(R.layout.activity_recover_startpage);

	}
	private void initData() {
		mHandler = new Handler();
		mHandler.postDelayed(runnable, 2000);

	}
	private void initEvent() {


	}
	
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			Intent intent1 = new Intent(RecoverStartpageActivity.this,
					RecoverMainActivity.class);
			startActivity(intent1);
		}
		};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
}
