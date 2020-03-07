package com.health.hl.recover;

/** page：报告分析页面
 *  aditor：zhou
 *  date:20170412
 *  describe:
 *  显示用户的肌力测试结果,可直接进入游戏
 */

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.health.hl.R;
import com.health.hl.R.color;
import com.health.hl.util.ToastUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecoverAnalysisActivity extends Activity implements OnClickListener, OnChartValueSelectedListener {

	
	private ImageView btn_back;    //返回按钮
	private HorizontalBarChart mChart_current;      //平行柱状图,显示本次测试结果
	private LineChart mChart_histroy;               //曲线图,显示历史肌力结果
	private Button btn_chartnow;                    //切换至本次测试图表按钮
	private Button btn_charthistroy;                //切换至历史肌力测试图表按钮
	private TextView show_Ilevel;                   //I类肌肌力等级显示文字
	private TextView show_IIlevel;                  //II类肌肌力等级显示文字
	private TextView show_mplan;                    //训练方案显示文字
	//I类肌5次的历史数据
	private ImageView show_Ilevel1;
	private ImageView show_Ilevel2;
	private ImageView show_Ilevel3;
	private ImageView show_Ilevel4;
	private ImageView show_Ilevel5;
	//II类肌5次的历史数据
	private ImageView show_IIlevel1;
	private ImageView show_IIlevel2;
	private ImageView show_IIlevel3;
	private ImageView show_IIlevel4;
	private ImageView show_IIlevel5;
	
	private Button btn_starttrain;                  //开始测试按钮		
	private Button btn_savereport;                 //点击打印报告
	private TextView show_analysis;	                //肌力分析文字显示
	private Toast customToast; 						// 消息显示框
	private String userdata;						//用户信息
	private String userid;							//用户id data20190820，author：晶，传入用户数据
	private String userphone;						//用户手机
	private String analysistext;                    //肌力分析文字内容
	private String messiagesize;					//肌力分析文字内容
	private String userWeight;
	private String mPlanCode = "-";   //训练方案编码
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	
	
	private static PowerManager.WakeLock wakeLock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initEvent();
	}
	
	private void initView() {
		setContentView(R.layout.activity_recover_analysis);
	}


	
	private void initData() {
		//返回按钮
		btn_back = (ImageView) findViewById(R.id.iv_recover_analysis_back);
		btn_back.setOnClickListener(this);
		//切换当次测试结果图表按钮
		btn_chartnow = (Button) findViewById(R.id.btn_analysis_chartnow);
		btn_chartnow.setOnClickListener(this);
		//切换历史肌力等级图表按钮
		btn_charthistroy = (Button) findViewById(R.id.btn_analysis_charthistroy);
		btn_charthistroy.setOnClickListener(this);
		//开始训练按钮
		btn_starttrain = (Button) findViewById(R.id.btn_analysis_starttrain);
		btn_starttrain.setOnClickListener(this);
		//点击打印报告（图片）
		btn_savereport = (Button) findViewById(R.id.btn_analysis_savereport);
		btn_savereport.setOnClickListener(this);
		//I类肌等级
		show_Ilevel  = (TextView) findViewById(R.id.tv_analysis_Ilevel);
		show_Ilevel1 = (ImageView) findViewById(R.id.iv_recover_analysis_Ilevel1);
		show_Ilevel2 = (ImageView) findViewById(R.id.iv_recover_analysis_Ilevel2);
		show_Ilevel3 = (ImageView) findViewById(R.id.iv_recover_analysis_Ilevel3);
		show_Ilevel4 = (ImageView) findViewById(R.id.iv_recover_analysis_Ilevel4);
		show_Ilevel5 = (ImageView) findViewById(R.id.iv_recover_analysis_Ilevel5);
		//II类肌等级
		show_IIlevel = (TextView) findViewById(R.id.tv_analysis_IIlevel);
		show_IIlevel1 = (ImageView) findViewById(R.id.iv_recover_analysis_IIlevel1);
		show_IIlevel2 = (ImageView) findViewById(R.id.iv_recover_analysis_IIlevel2);
		show_IIlevel3 = (ImageView) findViewById(R.id.iv_recover_analysis_IIlevel3);
		show_IIlevel4 = (ImageView) findViewById(R.id.iv_recover_analysis_IIlevel4);
		show_IIlevel5 = (ImageView) findViewById(R.id.iv_recover_analysis_IIlevel5);
		//训练方案
		show_mplan = (TextView) findViewById(R.id.tv_analysis_plannum);
		//训练方案
		show_analysis = (TextView) findViewById(R.id.tv_analysis_muscle_suggestion);
		//获取用户ID
		userid = this.getIntent().getStringExtra("userid");
		//获取用户ID
		userphone = this.getIntent().getStringExtra("userphone");
		userWeight = this.getIntent().getStringExtra(
				"userweight");
		mPlanCode = this.getIntent().getStringExtra("mplancode");
//		showToast(userid);
		//从本地SP文件读取数据
		reload();
	    
	}
	
	private void initEvent() {

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// 设置不自动锁屏
//		PowerManager pm = (PowerManager) getApplicationContext()
//				.getSystemService(getApplicationContext().POWER_SERVICE);
//		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK,
//				" WakeLock");
//		wakeLock.acquire();
		getWindow().
		addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onResume();

	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.iv_recover_analysis_back:  //返回
			//跳转至首页
			Intent intent1 = new Intent(RecoverAnalysisActivity.this,
					RecoverMainActivity.class);
			startActivity(intent1);
			finish();
			break;	
		case R.id.btn_analysis_chartnow:  //切换档次测试图表
			//初始化当次测试图表
			initchart_current();
			//显示图表
			mChart_current.setVisibility(View.VISIBLE);
			mChart_current.animateY(1500);
			//隐藏历史肌力等级数据图表
	        mChart_histroy.setVisibility(View.GONE);
			mChart_histroy = null;
			//设置相关按钮颜色
			btn_chartnow.setTextColor(0xFFF186FD);
			btn_charthistroy.setTextColor(color.bg_recover_analysispage_wordtitle);
			//禁用和开启相关按钮
			btn_chartnow.setClickable(false);
			btn_charthistroy.setClickable(true);
			break;	
		case R.id.btn_analysis_charthistroy:  //切换历史数据
//			mChart_histroy = null;
	        //初始化历史图表
			initchart_histroy();
	        setHistropData();
	        //显示图表
			mChart_histroy.setVisibility(View.VISIBLE);
			mChart_histroy.animateX(1000);
	        //隐藏当次测试结果图表
			mChart_current.setVisibility(View.GONE);
			mChart_current = null;
			//设置按钮颜色
			btn_chartnow.setTextColor(color.bg_recover_analysispage_wordtitle);
			btn_charthistroy.setTextColor(0xFFF186FD);
			//禁用和开启相关按钮
			btn_chartnow.setClickable(true);
			btn_charthistroy.setClickable(false);
			break;	
		case R.id.btn_analysis_starttrain:  //开始训练
			finish();
			Intent intent2 = new Intent(RecoverAnalysisActivity.this,
					RecoverAdjustActivity.class);
			intent2.putExtra("userid",userid);
			intent2.putExtra("maxpower",maxpower);
			intent2.putExtra("mplan",mplan);
			intent2.putExtra("activity", "1");//1表示盆底肌测试
			intent2.putExtra("mplancode",mPlanCode);
			intent2.putExtra("userweight",userWeight);//1表示盆底肌测试
			intent2.putExtra("userphone",userphone);//晶
			startActivity(intent2);
			break;
		case R.id.btn_analysis_savereport://保存报告（图片）
			//System.out.println("111");
			//Log.d("myDebug", "myFirstDebugMg");
			Intent intent3 = new Intent(RecoverAnalysisActivity.this,
					RecoverSaveReportActivity.class); 
			intent3.putExtra("userid",userid);
			intent3.putExtra("userphone",userphone);
			startActivity(intent3);
			break;
		//System.out.println("222");
		default:
			break;
		}
	}
			
//			RecoverTrainlevelDialog TrainlevelDialog = null;
//			//弹出设置训练等级和训练时间对话框
//			TrainlevelDialog = new RecoverTrainlevelDialog(RecoverAnalysisActivity.this,
//					"推荐：A方案 5等级");
//			setDialogHeight(TrainlevelDialog);
//			TrainlevelDialog.trainlevel = trainlevel;
//			TrainlevelDialog.traintime = traintime;
//			TrainlevelDialog.traintype = traintype;
//			TrainlevelDialog
//					.setListener(new com.sayes.sayhealth.views.RecoverTrainlevelDialog.OnCustomDialogListener() {
//
//
//						@Override
//						public void back(String name) {
//							// TODO Auto-generated method stub
//							// retest();
//
//							reload();
//							if(traintype == 1)
//							{
//								Intent intent1 = null;
//								intent1 = new Intent(RecoverAnalysisActivity.this,
//										Flapbird.class);
//								intent1.putExtra("levelvalue",String.valueOf(trainlevel));
//								intent1.putExtra("Highestlevel",String.valueOf(trainlevel));
//								intent1.putExtra("timevalue",String.valueOf(traintime));
//								intent1.putExtra("maxpower",maxpower);
////								intent1.putExtra("lastactivity",String.valueOf(lastactivity));
//								startActivity(intent1);
//							}else
//							{
//								Intent intent1 = null;
//								intent1 = new Intent(RecoverAnalysisActivity.this,
//										RecoverTrainActivity.class);
//								lastactivity = 2;
//								intent1.putExtra("levelvalue",String.valueOf(trainlevel));
//								intent1.putExtra("Highestlevel",String.valueOf(trainlevel));
//								intent1.putExtra("timevalue",String.valueOf(traintime));
//								intent1.putExtra("lastactivity",String.valueOf(lastactivity));
//								startActivity(intent1);
//							}
//						}
//					});
//			TrainlevelDialog.show();
			//break;	
	
	
	private void initchart_current()
	{
		mChart_current = (HorizontalBarChart) findViewById(R.id.chart_analysis_current);
		mChart_current.setOnChartValueSelectedListener(this);
		//无分割线
		mChart_current.setDrawGridBackground(false);
		mChart_current.setDescription("");

        // scaling can now only be done on x- and y-axis separately
		//设置不可以缩放
		mChart_current.setPinchZoom(false);
        //设置无阴影
		mChart_current.setDrawBarShadow(false);
		mChart_current.setDrawValueAboveBar(false);
        //设置左坐标轴
		mChart_current.getAxisLeft().setEnabled(false);
		mChart_current.getAxisLeft().setDrawAxisLine(false);
		mChart_current.getAxisLeft().setDrawLabels(false);
		mChart_current.getAxisLeft().setDrawGridLines(false);
		//设置右坐标轴
		mChart_current.getAxisRight().setDrawLabels(false);
		mChart_current.getAxisRight().setDrawGridLines(false);
		mChart_current.getAxisRight().setStartAtZero(false);
		mChart_current.getAxisRight().setAxisMaxValue(6f);
		mChart_current.getAxisRight().setAxisMinValue(-6f);
		mChart_current.getAxisRight().setLabelCount(7, false);
		mChart_current.getAxisRight().setValueFormatter(new CustomFormatter());
		mChart_current.getAxisRight().setTextSize(9f);
		
		//禁止图表缩放
		mChart_current.setScaleXEnabled(false);
		mChart_current.setScaleYEnabled(false);
		mChart_current.setFocusable(false);
		mChart_current.setFocusableInTouchMode(false);
		//设置图标不能识别触摸手势
		mChart_current.setTouchEnabled(false);
		
		//设置X轴有双侧(正负值)
        XAxis xAxis = mChart_current.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(9f);

        Legend l = mChart_current.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
        l.setFormSize(16f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        // IMPORTANT: When using negative values in stacked bars, always make sure the negative values are in the array first
        //填入双侧数据
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(new float[]{ -Float.valueOf(slowdata[0]), Float.valueOf(quickdata[0]) }, 0));
        yValues.add(new BarEntry(new float[]{ -Float.valueOf(slowdata[1]), Float.valueOf(quickdata[1]) }, 1));
        yValues.add(new BarEntry(new float[]{ -Float.valueOf(slowdata[2]), Float.valueOf(quickdata[2]) }, 2));
        yValues.add(new BarEntry(new float[]{ -Float.valueOf(slowdata[3]), Float.valueOf(quickdata[3]) }, 3));
        yValues.add(new BarEntry(new float[]{ -Float.valueOf(slowdata[4]), Float.valueOf(quickdata[4]) }, 4));

        //设置图表属性
        BarDataSet set = new BarDataSet(yValues, "");
        set.setValueFormatter(new CustomFormatter());
        set.setValueTextSize(7f);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setBarSpacePercent(40f);
        set.setColors(new int[] {0xFF7C9AFF, 0xFFE787FD});
        set.setStackLabels(new String[]{
                "I类肌", "II类肌"
        });

        String []xVals = new String[]{"1", "2", "3", "4", "5"};

        BarData data = new BarData(xVals, set);
        mChart_current.setData(data);
        mChart_current.invalidate();
	}
	
	private void initchart_histroy()
	{
		mChart_histroy = (LineChart)findViewById(R.id.chart_analysis_histroy);
		mChart_histroy.setDescription("");
		mChart_histroy.setDrawGridBackground(false);

		XAxis xAxis = mChart_histroy.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setAvoidFirstLastClipping(true);
		// xAxis.setTypeface(mTf);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawLabels(true);
		YAxis leftAxis = mChart_histroy.getAxisLeft();
		// leftAxis.setTypeface(mTf);
		leftAxis.setLabelCount(5, false);		

		leftAxis.setAxisMinValue(0);
		leftAxis.setAxisMaxValue(6);
		// 禁止右边Y坐标轴显示
		mChart_histroy.getAxisRight().setEnabled(false);
		// 禁止X坐标轴显示
		mChart_histroy.getXAxis().setEnabled(true);
		// 禁止图表缩放
		mChart_histroy.setScaleXEnabled(false);
		mChart_histroy.setScaleYEnabled(false);
		mChart_histroy.setFocusable(false);
		mChart_histroy.setFocusableInTouchMode(false);
		// recoverchart.setClickable(false);
		// 设置画图标轮廓线
		mChart_histroy.setDrawBorders(true);
		// 设置图标不能识别触摸手势
		mChart_histroy.setTouchEnabled(false);

		mChart_histroy.animateX(0);
	}
	
	
	private class CustomFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public CustomFormatter() {
            mFormat = new DecimalFormat("###");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(Math.abs(value)) + "级";
        }
    }

	// 坐标轴X值
	ArrayList<String> xVals1 = new ArrayList<String>();
	// 实际测量压力值数据
	ArrayList<Entry> yVals1 = new ArrayList<Entry>();
	// 实际测量压力值数据
	ArrayList<Entry> yVals2 = new ArrayList<Entry>();
	// 实际测量压力值数据
	ArrayList<Entry> yVals = new ArrayList<Entry>();
	private String Imlevel;     //I类肌等级
	private String IImlevel;    //II类肌等级
	private String maxpower;    //最大肌力
	private String quickdata[] = new String[5];  //当前测试第一次II类肌等级
	private String slowdata[] = new String[5];  //当前测试第一次II类肌等级
//	private String quicklevel1;  //当前测试第一次II类肌等级
//	private String quicklevel2;	 //当前测试第二次II类肌等级
//	private String quicklevel3;  //当前测试第三次II类肌等级
//	private String quicklevel4;  //当前测试第四次II类肌等级
//	private String quicklevel5;  //当前测试第五次II类肌等级
//	private String slowlevel1;   //当前测试第一次I类肌等级
//	private String slowlevel2;	 //当前测试第二次I类肌等级
//	private String slowlevel3;	 //当前测试第三次I类肌等级
//	private String slowlevel4;	 //当前测试第四次I类肌等级
//	private String slowlevel5;	 //当前测试第五次I类肌等级
	private String histroy_I[] = new String[5];   //I类肌历史数据
	private String histroy_II[] = new String[5];  //II类肌历史数据
	private String maxPower[] = new String[5];  //II类肌历史数据
	private String date[] = new String[5];   //I类肌历史数据
	private String mplan;        //训练方案
	private int temp = 5;        //训练方案
	private float maxPowerdata = 0;        //最大肌力历史数据中的最大值
	private int maxPowerDivide = 1;        //最大肌力的除值
//	private int traintime;
//	private int trainlevel;
//	private String planlevel;
	
	private void setHistropData() {
		//清空坐标
		xVals1.clear();
		yVals.clear();
		yVals1.clear();
		yVals2.clear();
		//填入五次历史数据数值
		xVals1.add("");
		yVals1.add(new Entry(0, 0));
		yVals.add(new Entry(0, 0));
		yVals2.add(new Entry(0, 0));
		temp = 5;
		for (int i = 0; i <=4; i++) {
			if (!histroy_I[i].equals("-"))
			{
				temp = i;
			}
		}
		for(int i=0;i <= temp;i++)
		{
			if(Float.valueOf(maxPower[i])>maxPowerdata)
			{
				maxPowerdata = Float.valueOf(maxPower[i]);
			}
		}
		maxPowerDivide = (int) (maxPowerdata/5);
		maxPowerDivide++;
		if(temp < 5)
		{
			for (int i = temp; i >= 0; i--) {
				xVals1.add(date[i]);
				yVals1.add(new Entry(Integer.valueOf(histroy_I[i]), temp + 1 - i));
				yVals.add(new Entry(Integer.valueOf(histroy_II[i]), temp + 1 - i));
				yVals2.add(new Entry(Float.valueOf(maxPower[i])/maxPowerDivide, temp + 1 - i));
			}
			for (int i = temp; i < 4; i++) {
				xVals1.add("");
//				yVals1.add(new Entry(0, i+1));
//				yVals.add(new Entry(0, i+1));
			}
		}
//		if(!histroy_I[0].equals("-"))
//		{
//			xVals1.add(date[0]);
//			yVals1.add(new Entry(Integer.valueOf(histroy_I[0]), 5));
//			yVals.add(new Entry(Integer.valueOf(histroy_I[0]), 5));
//		}
//		xVals1.add("");
//		xVals1.add("");
//		showToast(histroy_I[4]);		
		LineDataSet set1 = new LineDataSet(yVals1, "I类肌");
		LineDataSet set2 = new LineDataSet(yVals, "II类肌");
		LineDataSet set3 = new LineDataSet(yVals2, "最大肌力");
		// I类肌曲线设置
		set1.enableDashedLine(10f, 0f, 0f);
		set1.setColor(0xFF7C9AFF);
		set1.setLineWidth(3f);
		set1.setDrawCircles(true);
		set1.setDrawCircleHole(true);
		set1.setDrawValues(false);
//		set1.setLabel("aa");
		// II类肌曲线设置
		set2.enableDashedLine(10f, 0f, 0f);
		set2.setColor(0xFFE787FD);
		set2.setLineWidth(6f);
		set2.setDrawCircles(true);
		set2.setDrawCircleHole(true);
		set2.setDrawValues(false);
		// 最大肌力曲线设置
		set3.enableDashedLine(10f, 0f, 0f);
		set3.setColor(0xFF1BDA29);
		set3.setLineWidth(6f);
		set3.setDrawCircles(true);
		set3.setDrawCircleHole(true);
		set3.setDrawValues(false);
		

		//先放set2,再放set1,把set1(实际波形)显示在上层
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set2); // add the datasets
		dataSets.add(set1);
		dataSets.add(set3);
		LineData datachart = new LineData(xVals1, dataSets);
		// set data 如果y坐标大于x坐标则报错
		if ((yVals.size() <= xVals1.size()) || (yVals1.size() <= xVals1.size()))
			mChart_histroy.setData(datachart);

	}
	
	
	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
	}

	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	public void reload() {
		
	       //历史肌力等级
		for(int i=0;i<5;i++)
		{
			histroy_I[i] = "-";
			histroy_II[i] = "-";
			date[i] = "-";
			quickdata[i] = "0";
			slowdata[i] = "0";
		}
//		histroy_I[0] = "-";
//		histroy_I[1] = "-";
//		histroy_I[2] = "-";
//		histroy_I[3] = "-";
//		histroy_I[4] = "-";
//		histroy_II[0] = "-";
//		histroy_II[1] = "-";
//		histroy_II[2] = "-";
//		histroy_II[3] = "-";
//		histroy_II[4] = "-";
//		quickdata[0] = "0";
//		quickdata[1] = "0";
//		quickdata[2] = "0";
//		quickdata[3] = "0";
//		quickdata[4] = "0";
//		slowdata[0] = "0";
//		slowdata[1] = "0";
//		slowdata[2] = "0";
//		slowdata[3] = "0";
//		slowdata[4] = "0";
		userdata = "";
		new Thread(new Runnable() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					userdata = getJson("http://119.23.248.54/Ls_Server_web/mstest/findMsTestRecordByuId?uId="+userid);
					testdatashow(userdata);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
		}).start();
		
		new Handler().postDelayed(new Runnable() {						
			@Override
			public void run() {
				if(!userdata.equals(""))
				{
					// TODO Auto-generated method stub
				    show_Ilevel.setText(Imlevel+"级");
				    show_IIlevel.setText(IImlevel+"级");
					show_analysis.setText(analysistext);
					
					//设置肌力等级图案
				    switch(Integer.valueOf(Imlevel))
			       {
			       		case 0:
			       			break;
			       		case 1:
			       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			break;
			       		case 2:
			       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			break;
			       		case 3:
			       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			break;
			       		case 4:
			       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			break;
			       		case 5:
			       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			show_Ilevel5.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
			       			break;
			       		default:
			       			break;
			       }
				    
			       switch(Integer.valueOf(IImlevel))
			       {
			       		case 0:
			       			break;
			       		case 1:
			       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			break;
			       		case 2:
			       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			break;
			       		case 3:
			       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			break;
			       		case 4:
			       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			break;
			       		case 5:
			       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			show_IIlevel5.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
			       			break;
			       		default:
			       			break;
			       }
			       //设置训练方案
			       show_mplan.setText(mplan);
					//初始化本次测试结果图表
					initchart_current();
					//动画显示1500ms
			        mChart_current.animateY(1500);
	//		        //初始化历史肌力等级图表
			        initchart_histroy();
	//		        //设置历史数据
			        setHistropData();
				}else
				{
					new Thread(new Runnable() {	
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								userdata = getJson("http://119.23.248.54/Ls_Server_web/mstest/findMsTestRecordByuId?uId="+userid);
								testdatashow(userdata);
							} catch (Exception e) {
								// TODO Auto-generated catch block
							}
						}
					}).start();
					new Handler().postDelayed(new Runnable() {						
						@Override
						public void run() {
							if(!userdata.equals(""))
							{
								// TODO Auto-generated method stub
							    show_Ilevel.setText(Imlevel+"级");
							    show_IIlevel.setText(IImlevel+"级");
								show_analysis.setText(analysistext);
								
								//设置肌力等级图案
							    switch(Integer.valueOf(Imlevel))
						       {
						       		case 0:
						       			break;
						       		case 1:
						       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			break;
						       		case 2:
						       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			break;
						       		case 3:
						       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			break;
						       		case 4:
						       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			break;
						       		case 5:
						       			show_Ilevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			show_Ilevel5.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_1level5));
						       			break;
						       		default:
						       			break;
						       }
							    
						       switch(Integer.valueOf(IImlevel))
						       {
						       		case 0:
						       			break;
						       		case 1:
						       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			break;
						       		case 2:
						       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			break;
						       		case 3:
						       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			break;
						       		case 4:
						       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			break;
						       		case 5:
						       			show_IIlevel1.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel2.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel3.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel4.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			show_IIlevel5.setBackground(getResources().getDrawable(R.drawable.bg_recover_analysispage_2level5));
						       			break;
						       		default:
						       			break;
						       }
						       //设置训练方案
						       show_mplan.setText(mplan);
								//初始化本次测试结果图表
								initchart_current();
								//动画显示1500ms
						        mChart_current.animateY(1500);
				//		        //初始化历史肌力等级图表
						        initchart_histroy();
				//		        //设置历史数据
						        setHistropData();
							}else
							{
								finish();
								Intent intent2 = new Intent(RecoverAnalysisActivity.this,
										RecoverMainActivity.class);
								startActivity(intent2);
							}
						}
					}, 1000);
				}
			}
		}, 1000);
//        SharedPreferences prefs = App.get().getSharedPreferences(
//        		"userinfo",
//                Context.MODE_PRIVATE
//        );
//       Imlevel  = prefs.getString("Imlevel", "-");
//       IImlevel  = prefs.getString("IImlevel", "-");
//       
//       maxpower = prefs.getString("maxpower", "-");
//       //如果测试数据为0也显示为1级
//       quicklevel1 = prefs.getString("quick1", "1");
////       if(Float.valueOf(quicklevel1)<=1)
////    	   quicklevel1 = "1";
//       quicklevel2 = prefs.getString("quick2", "1");
////       if(Float.valueOf(quicklevel2)<=1)
////    	   quicklevel2 = "1";
//       quicklevel3 = prefs.getString("quick3", "1");
////       if(Float.valueOf(quicklevel3)<=1)
////    	   quicklevel3 = "1";
//       quicklevel4 = prefs.getString("quick4", "1");
////       if(Float.valueOf(quicklevel4)<=1)
////    	   quicklevel4 = "1";
//       quicklevel5 = prefs.getString("quick5", "1");
////       if(Float.valueOf(quicklevel5)<=1)
////    	   quicklevel5 = "1";
//       slowlevel1 = prefs.getString("slow1", "1");
////       if(Float.valueOf(slowlevel1)<=1)
////    	   slowlevel1 = "1";
//       slowlevel2 = prefs.getString("slow2", "1");
////       if(Float.valueOf(slowlevel2)<=1)
////    	   slowlevel2 = "1";
//       slowlevel3 = prefs.getString("slow3", "1");
////       if(Float.valueOf(slowlevel3)<=1)
////    	   slowlevel3 = "1";
//       slowlevel4 = prefs.getString("slow4", "1");
////       if(Float.valueOf(slowlevel4)<=1)
////    	   slowlevel4 = "1";
//       slowlevel5 = prefs.getString("slow5", "1");  
////       if(Float.valueOf(slowlevel5)<=1)
////    	   slowlevel5 = "1";
		//历史肌力等级
//       //训练方案
//       mplan = prefs.getString("trainplan", "-");
//       setAnalysisWord();
//       planlevel = prefs.getString("trainlevel", "1");
//       traintime = Integer.parseInt(prefs.getString("traintime","5"));
//       trainlevel = Integer.parseInt(planlevel);
//       traintype = Integer.parseInt(prefs.getString("traintype", "1"));
       

       
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
	
	private void setAnalysisWord()
	{
			if(mplan.equals("A"))
			{
				analysistext = "盆底肌平均持续收缩<=1秒，异常；平均快速收缩放松1次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("B"))
			{
				analysistext = "盆底肌平均持续收缩2秒，异常；平均快速收缩放松1次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("C"))
			{
				analysistext = "盆底肌平均持续收缩3秒，异常；平均快速收缩放松1次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("D"))
			{
				analysistext = "盆底肌平均持续收缩4秒，正常；平均快速收缩放松1次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("E"))
			{
				analysistext = "盆底肌平均持续收缩5秒，正常；平均快速收缩放松1次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("F"))
			{
				analysistext = "盆底肌平均持续收缩<=1秒，异常；平均快速收缩放松2次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("G"))
			{
				analysistext = "盆底肌平均持续收缩2秒，异常；平均快速收缩放松2次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("H"))
			{
				analysistext = "盆底肌平均持续收缩3秒，异常；平均快速收缩放松2次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("I"))
			{
				analysistext = "盆底肌平均持续收缩4秒，正常；平均快速收缩放松2次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("J"))
			{
				analysistext = "盆底肌平均持续收缩5秒，正常；平均快速收缩放松2次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("K"))
			{
				analysistext = "盆底肌平均持续收缩<=1秒，异常；平均快速收缩放松3次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("L"))
			{
				analysistext = "盆底肌平均持续收缩2秒，异常；平均快速收缩放松3次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("M"))
			{
				analysistext = "盆底肌平均持续收缩3秒，异常；平均快速收缩放松3次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("N"))
			{
				analysistext = "盆底肌平均持续收缩4秒，正常；平均快速收缩放松3次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("O"))
			{
				analysistext = "盆底肌平均持续收缩5秒，正常；平均快速收缩放松3次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("P"))
			{
				analysistext = "盆底肌平均持续收缩<=1秒，异常；平均快速收缩放松4次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("Q"))
			{
				analysistext = "盆底肌平均持续收缩2秒，异常；平均快速收缩放松4次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("R"))
			{
				analysistext = "盆底肌平均持续收缩3秒，异常；平均快速收缩放松4次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("S"))
			{
				analysistext = "盆底肌平均持续收缩4秒，正常；平均快速收缩放松4次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("T"))
			{
				analysistext = "盆底肌平均持续收缩5秒，正常；平均快速收缩放松4次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("U"))
			{
				analysistext = "盆底肌平均持续收缩<=1秒，异常；平均快速收缩放松5次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("V"))
			{
				analysistext = "盆底肌平均持续收缩2秒，异常；平均快速收缩放松5次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("W"))
			{
				analysistext = "盆底肌平均持续收缩3秒，异常；平均快速收缩放松5次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("X"))
			{
				analysistext = "盆底肌平均持续收缩4秒，正常；平均快速收缩放松5次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else if(mplan.equals("Y"))
			{
				analysistext = "盆底肌平均持续收缩5秒，正常；平均快速收缩放松5次，正常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}else
			{
				analysistext = "盆底肌平均持续收缩<=1秒，异常；平均快速收缩放松1次，异常；坚持主动盆底康复训练，有助于恢复盆底肌力水平。";
			}
			show_analysis.setText(analysistext);
	}
	
	// 显示消息栏
//		private void showToast(String text) {
//			if (customToast == null) {
//				customToast = Toast.makeText(getApplicationContext(), text,
//						Toast.LENGTH_SHORT);
//			} else {
//				customToast.setText(text);
//				customToast.setDuration(Toast.LENGTH_LONG);
//			}
//			customToast.setGravity(Gravity.CENTER, 0, 0);
//			customToast.show();
//		}
	public void showToast(String text) {
		//		ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
		try {
			ToastUtils.showToast(this,text, Toast.LENGTH_SHORT);
			customToast.setGravity(Gravity.CENTER, 0, 0);
			//        customToast.show();
		} catch (Exception e) {}
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
            if(a == 200)
            {
	                //获取服务器返回信息
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//	            int inputcode = 0;
	            inputLine = reader.readLine();
//	                //截取CODE码用于识别状况
//	            inputcode = inputLine.indexOf("age");
//	            char b= inputLine.charAt(inputcode);
//	            char c= inputLine.charAt(0);
	            reader.close();
            }
//            a = conn.getResponseCode();
            return inputLine;
    }
    
    
    private void testdatashow(String data)
    {
	    	int a=0;
	    	a = data.indexOf("size");
	    	a += 5;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(Character.isDigit(data.charAt(i)))
	    			{
	    				if(i==(a+1))
	    					messiagesize = data.charAt(i)+"";
	    				else
	    					messiagesize += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = 0;
	    	a = data.indexOf("oneMuscle");
	    	a += 10;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(Character.isDigit(data.charAt(i)))
	    			{
	    				if(i==(a+1))
	    					Imlevel = data.charAt(i)+"";
	    				else
	    					Imlevel += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = 0;
	    	a = data.indexOf("twoMuscle");
	    	a += 10;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(Character.isDigit(data.charAt(i)))
	    			{
	    				if(i==(a+1))
	    					IImlevel = data.charAt(i)+"";
	    				else
	    					IImlevel += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = 0;
	    	//解析历史肌力数据
	    	for(int j=0;j<Integer.parseInt(messiagesize);j++)
	    	{
		    	a = data.indexOf("oneMuscle",a);
		    	a += 10;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(Character.isDigit(data.charAt(i)))
		    			{
		    				if(i==(a+1))
		    					histroy_I[j] = data.charAt(i)+"";
		    				else
		    					histroy_I[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	
	    	a = 0;
	    	for(int j=0;j<Integer.parseInt(messiagesize);j++)
	    	{
		    	a = data.indexOf("twoMuscle",a);
		    	a += 10;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(Character.isDigit(data.charAt(i)))
		    			{
		    				if(i==(a+1))
		    					histroy_II[j] = data.charAt(i)+"";
		    				else
		    					histroy_II[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = 0;
	    	for(int j=0;j<Integer.parseInt(messiagesize);j++)
	    	{
		    	a = data.indexOf("recordDate",a);
		    	a += 11;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+7;i<a+12;i++)
		    		{
		    			if(i==a+7)
		    			{
		    				date[j] = data.charAt(i)+"";
		    			}else if(i==a+9)
		    			{
		    				date[j] += "/";
		    			}else
		    			{
		    				date[j] += data.charAt(i);
		    			}
		    		}
		    	}
	    	}
	    	
	    	a = 0;
	    	//解析历史肌力数据
	    	for(int j=0;j<5;j++)
	    	{
		    	a = data.indexOf("cOneMuscle",a);
		    	a += 11;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(Character.isDigit(data.charAt(i)))
		    			{
		    					slowdata[j] = data.charAt(i)+"";
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a=0;
	    	for(int j=0;j<5;j++)
	    	{
		    	a = data.indexOf("cTwoMuscle",a);
		    	a += 11;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(Character.isDigit(data.charAt(i)))
		    			{
		    				quickdata[j] = data.charAt(i)+"";
		    			}else
		    			{
		    				break;
		    			}
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
	    					mplan = data.charAt(i)+"";
	    				else
	    					mplan += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	a = data.indexOf("tester");
	    	a += 8;
	    	if(data.charAt(a)=='"')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!='"')
	    			{
	    				if(i==(a+1))
	    					analysistext = data.charAt(i)+"";
	    				else
	    					analysistext += data.charAt(i);
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
	    					maxpower = data.charAt(i)+"";
	    				else
	    					maxpower += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	
	    	a = 0;
	    	//解析历史肌力数据
	    	for(int j=0;j<5;j++)
	    	{
		    	a = data.indexOf("maxPower",a);
		    	a += 9;
		    	maxPower[j] = "";
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			for(int k=0;k<4;k++)
		    			{
		    				if(data.charAt(i+k) != ',')
		    					maxPower[j] += data.charAt(i+k)+"";
		    			}
		    			break;
		    		}
		    	}
	    	}
	    	
    	}
}
