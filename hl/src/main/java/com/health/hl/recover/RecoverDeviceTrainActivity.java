package com.health.hl.recover;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.health.hl.R;
import com.health.hl.util.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecoverDeviceTrainActivity extends Activity implements View.OnClickListener {
    private ToastUtils customToast;        //消息框
    private ImageView adjustBack;
    private ImageView starticon;
    private ImageView stopicon;
    private ImageView show_sound;      //静音图标
    private TextView show_nowMinute;// 剩余时间（分钟）
    private TextView show_nowSecond;// 剩余时间（秒）
    private TextView show_title;// 剩余时间（秒）
    private LineChart recoverChart;   //训练波形图
    private int lastActivity1;   //区分是测试还是训练的标记
    private int type = 1;// 训练波形
    private int trainLevel;           //训练等级,暂无使用
    private int[] trainTypeSet = new int[10];// 训练波形方案波形组
    private int trainTypeCount = 0; //训练模式下第几个波形
    private float highTypeSet = 1;  //高波模式下的倍数
    private String mPlan; // 训练方案
    private float maxPower = 0;// 最大压力值
    private float powerPara;// 训练力度系数
    private float realData=0;
    private int trainTime = 0;// 训练时间
    private int minute = 0;// 计算剩余分钟；
    private int second = 0;// 计算剩余秒
    private int secondCount = 0;// 用于计数，每计数10次为一秒
    private int progressCount = 0;// 增加百分比计数
    private int nowProgress = 0;// 当前进度的百分比
    private int addProgress = 0;// 计算多少秒加1%进度
    private ProgressBar timeProgress;// 剩余时间进度条
    private int timeCount = 0;// 训练时间计算
    private Handler mHandler;         //定时线程
    private Thread thread;
    private boolean beginEndFlag = false;   //首次进入界面标记
    private int beginCount = 0;// 开始准备三秒
    ArrayList<Entry> yVals = new ArrayList<Entry>();
    private float[] realDataSet= new float[300];// 下位机上传的压力值
    private MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.shousuo); // 语音
    private boolean MediaPlayflag = true;  //是否播放语音标记
    private boolean MediaPlayingflag = false;  //是否正在播放语音标记

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_device_train);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void initData() {
        //初始化压力值
        realData = (float)0;
        //返回按钮
        adjustBack = (ImageView) findViewById(R.id.iv_recover_device_train_back);
        adjustBack.setOnClickListener(this);
        starticon = (ImageView) findViewById(R.id.iv_device_startagain);
        starticon.setOnClickListener(this);
        stopicon = (ImageView) findViewById(R.id.iv_device_stop);
        stopicon.setOnClickListener(this);
        //是否静音显示
        // show_sound = (ImageView) findViewById(R.id.iv_device_train_sound);
        //  show_sound.setOnClickListener(this);
        //剩余时间分钟
        show_nowMinute = (TextView) findViewById(R.id.tv_device_train_minute);
        //剩余时间秒钟
        show_nowSecond = (TextView) findViewById(R.id.tv_device_train_second);
        //页面标题
        show_title = (TextView) findViewById(R.id.tv_device_train_title);
        timeProgress = (ProgressBar) findViewById(R.id.pb_device_train_traintime);
        //线性表格
        recoverChart = (LineChart) findViewById(R.id.line_recover_chart);
        trainLevel = 1;//应该传入，这里写死训练等级
        trainTime = 5; //应该传入，这里写死时间
        lastActivity1 = 0;//这里是写死为训练的标记
        //这里有一步是读取本地数据的。师兄用的是reload方法
        maxPower = (float)30.0;//应该传入，这里写死最大压力值
        mPlan = "A"; //应该传入，这里写死
        //播放训练语音
        mediaPlayer = MediaPlayer.create(this, R.raw.begintrain);
        //设置训练难度。即占最大肌力的比例
        powerPara = (float) 0.7;//应该由trainlevel决定的，这里写死
        //初始化图表
        initChart(trainLevel); //写死了level值=1
        //设置图标坐标轴，为maxpower的1.2倍
        recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower*1.2));
        //清理模板数组
        yVals.clear();
        //训练初始化波形为2
        setParameter();//设训练参数（由mplan决定波形组）
        //训练波形
        type = trainTypeSet[0]; // type=8，是基波：trainTypeSet {8988688968}
        //设置模板波形（13种，由type决定是哪种）
        setModelyValsType(type, levelYAxis_1, (float) (maxPower*powerPara));
       // setRealData(type,(float) (maxPower*powerPara));
        //设置数据到图表中
        setData(0, 0, trainLevel);
        //显示
        recoverChart.animateX(0);

        //新建线程
        mHandler = new Handler();
        //显示测试时间
        show_nowMinute.setText(String.valueOf(trainTime));
        //分钟
        minute = trainTime;
        //设置倒计时进度条的秒数
        addProgress = trainTime * 60 / 100;
    }//initData()结束

    int a = 0;

    Runnable runnable = new Runnable() {
        //波形显示线程
        @Override
        public void run() {
            mHandler.postDelayed(this, 100);
            //如果为第一次进入界面,先把语音播报播完再开始进行肌力测试
            if (!beginEndFlag) {
                if (MediaPlayingflag != true) {//语音不在播报
                    secondCount++;
                    if (secondCount >= 10) {
                        //从5秒开始倒数,5,4,3,2,1..如果lastactivity1为1则是测试,为2则是训练
                        String a = String.valueOf(3 - beginCount) + "秒后开始训练";
                        showToast(a);
                        beginCount++;
                        secondCount = 0;
                        //当大于5秒时则开始波形
                        if (beginCount >= 3) {
                            beginCount = 0;
                            xVals1.clear();
                            yVals1.clear();
                            beginEndFlag = true;
                        }
                    }
                }
            } else {
                //存储压力值-J
                realDataSet[a] = realData;
                //这里少一步要存储压力值？？？？！！！！！！！
                //setRealData(type,(float) (maxPower*powerPara));
                //播放收缩、放松语音
                playTrainSound(a);
                setData(a, realData, trainLevel);
                recoverChart.animateX(0);
                //倒计时
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

                a++;

                //当一个波形完成(30秒),即a计数到300
                if (a >= 300) {
                    a = 0;
                    timeCount++;
                    if (timeCount >= (trainTime * 2)) {
                        timeCount = 0;
                        //停止线程
                        mHandler.removeCallbacks(runnable);
                        //显示训练结束消息框
                        showToast("训练结束");
                    } else {
                        xVals1.clear();
                        yVals1.clear();
                        yVals.clear();

                        trainTypeCount++;//训练模式下的第几个波形
                        if (trainTypeCount > 9) {
                            trainTypeCount = 0;
                        }
                        type = trainTypeSet[trainTypeCount];//训练波形方案波形组
                        if ((trainTypeCount == 2) || (trainTypeCount == 5)) {
                            recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower * 1.2 * highTypeSet));//highTypeSet高波模式下的倍数
                            setModelyValsType(type, levelYAxis_1, (float) (maxPower * powerPara * highTypeSet));
                        } else {
                            recoverChart.getAxisLeft().setAxisMaxValue((float) (maxPower * 1.2));
                            setModelyValsType(type, levelYAxis_1, (float) (maxPower * powerPara));
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_recover_device_train_back:
                Intent intent = new Intent(RecoverDeviceTrainActivity.this, RecoverDeviceBaseActivity.class);
                startActivity(intent);
                break;

            // 开始和继续训练图标
            case R.id.iv_device_startagain:
                runnable.run();
                break;
            //停止训练图标
            case R.id.iv_device_stop:
                break;
            default:
                break;
        }
    }

    /**
     * 初始化曲线图
     */
    private void initChart(int level) {
        recoverChart.setDescription("");
        recoverChart.setDrawGridBackground(false);

        XAxis xAxis = recoverChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        YAxis leftAxis = recoverChart.getAxisLeft();
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
        setModelyVals(level, type);
        recoverChart.animateX(0);
    }
    Handler handler = new Handler() { };
    // X坐标
    ArrayList<String> xVals1 = new ArrayList<String>();
    // 实际测量压力值数据
    ArrayList<Entry> yVals1 = new ArrayList<Entry>();

    /**
     * 画图函数
     */
    private void setData(int a, float data, int trainlevel) {
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
    //设置realData的值
    private void setRealData(int type,float levelY) {
        switch (type) {
            case 8://训练波形：基波
                for (int i = 0; i < 300; i++) {
                    float realData = 0;
                    if (i <= 30 && i >= 20) {
                        realData = levelY / 10 * i - 2 * levelY;
                    } else if (i < 130 && i >= 120) {
                        realData = levelY / 10 * i - 12 * levelY;
                    } else if (i < 230 && i >= 220) {
                        realData = levelY / 10 * i - 22 * levelY;
                    } else if ((i <= 70 && i >= 30)
                            || (i <= 170 && i >= 130)
                            || (i <= 270 && i >= 230)) {
                        realData = levelY;
                    } else if (i <= 80 && i > 70) {
                        realData = (-levelY) / 10 * i + 8 * levelY;
                    } else if (i <= 180 && i > 170) {
                        realData = (-levelY) / 10 * i + 18 * levelY;
                    } else if (i <= 280 && i > 240) {
                        realData = (-levelY) / 10 * i + 28 * levelY;
                    } else {
                        realData = 0;
                    }
                    yVals1.add(new Entry(realData,i));
                }
                break;
            default:
                break;
        }
    }
    //设置训练参数
    private void  setParameter() {//mplan的类型，ABCDEFG……
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

}

