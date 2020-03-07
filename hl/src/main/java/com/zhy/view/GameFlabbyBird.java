package com.zhy.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


import com.health.hl.app.App;
import com.health.hl.recover.RecoverTrainActivity;
import com.health.hl.views.RecoverGameOverDialog;
import com.health.hl.views.RecoverGameOverDialog.OnCustomDialogListener1;
import com.health.hl.views.RecoverReportDialog;
import com.health.hl.views.RecoverReportDialog.OnCustomDialogListener;
import com.health.hl.R;


public class GameFlabbyBird extends SurfaceView implements Callback, Runnable
{

	private SurfaceHolder mHolder;
	/**
	 * 与SurfaceHolder绑定的Canvas
	 */
	private Canvas mCanvas;
	//private Canvas mCanvas1;
	/**
	 * 用于绘制的线程
	 */
	public Thread t;
	
	final  CountDownLatch  latch =  new CountDownLatch(1);
	
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;
	//小鱼是否移动标记，当蓝牙断开则停止
	public boolean isRunningflag = true;
	//时间是否倒计时标记，当蓝牙断开则停止
	public boolean timeRunningflag = false;
	//是否开始时间倒数,当点击后再进行倒数
	public boolean timeRunningfirstflag = false;
	//是否开始时间倒数,当点击后再进行倒数
	private boolean touchflag = false;
	public boolean Startflag = false;
	public boolean gameOverflag = false;
	private int touchcount = 0;
	
	
	private Paint mPaint;

	/**
	 * 当前View的尺寸
	 */
	private int mWidth;
	private int mHeight;
	private RectF mGamePanelRect = new RectF();

	/**
	 * 背景
	 */
	private Bitmap mBg;
	
	private Bitmap mback;

	/**
	 * *********鸟相关**********************
	 */
	private Bird mBird;
	private Bitmap mBirdBitmap;
	public int mBirdY;
	public float maxpower = (float) 15.0;
	public float x = (float) 0;
	public float y = (float) 0;
	/**
	 * 地板
	 */
	private Floor mFloor;
	private Bitmap mFloorBg;

	/**
	 * *********管道相关**********************
	 */
	/**
	 * 管道
	 */
	private Bitmap mPipeTop;
	private Bitmap mPipeBottom;
	private RectF mPipeRect;
	private int mPipeWidth;
	private int mPipeType = 1; //训练管道类型
	private int mPipeModel = 1;  //训练管道模版
	private int mPipesetcount = 6;  //训练管道一套波形计数
	private int succeecount = 0;  //训练管道一套波形计数
	private int succeemodel = 1;  //训练管道一套波形计数
	private int mPipeCount = 0;  //训练管道计算
	private boolean mPipeFirstCount = true; //每个训练管道类型切换后需要间隔一个管道
	private boolean mPipeUpDownFlag = true; //训练管道向上还是向下标记
	private boolean PipeFirstCount = true;  //首个管道距离设置为300
	/**
	 * 管道的宽度 60dp
	 */
	private static final int PIPE_WIDTH = 60;
	private List<Pipe> mPipes = new ArrayList<Pipe>();

	/**
	 * 分数
	 */
	private final int[] mNums = new int[] { R.drawable.n0, R.drawable.n1,
			R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5,
			R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9 };
	private Bitmap[] mNumBitmap;

	public int mGrade = 0;      //当前分数
	public int lastmGrade = 0;      //当前分数
	public int planGrade = 1;   //目标分数
	private int mRemovedPipe = 0;  //已经通过的管道
	private boolean mRemovedPipeflag = false;  //已经通过的管道

	private static final float RADIO_SINGLE_NUM_HEIGHT = 1 / 15f;
	private int mSingleGradeWidth;
	private int mSingleGradeHeight;
	private RectF mSingleNumRectF;

	private int mSpeed = Util.dp2px(getContext(), 2);  //小鱼的速度
	public int trainlevel = 1;  //当前训练等级
	public int traintime = 1;  //当前训练时间
	public int minute = 0;// 计算剩余分钟；
	private int second = 0;// 计算剩余秒
	private int secontcount = 0; //1秒的计数
	private int swimcount = 0;   //游泳音效播放计数
	private int scorereducecount = 0;  //小鱼碰到石头后减分,计数3秒,当减分后3秒内不会重复触犯减分
	public int Highesttrainlevel = 1;//历史最高训练等级
	public String mplan = "A";//历史最高训练等级
	private int Pipelength = 5 ;  
	private int Pipenum = 3 ;
	//文件读取路径
	private String filename="userinfo";
	//训练等级存储字段
	public static final String TRAINLEVEL = "trainlevel"; 
	public MediaPlayer mediaPlayer; // 背景语音
	public MediaPlayer mediaPlayer_hit; // 撞击语音
	public MediaPlayer mediaPlayer_succee; // 成功语音
	

	/***********************************/

	public enum GameStatus
	{
		WAITTING, RUNNING, STOP, ASKING;
	}

	/**
	 * 记录游戏的状态
	 */
	public GameStatus mStatus = GameStatus.WAITTING;

	/**
	 * 触摸上升的距离，因为是上升，所以为负值
	 */
	private static final int TOUCH_UP_SIZE = -16;
	/**
	 * 将上升的距离转化为px；这里多存储一个变量，变量在run中计算
	 * 
	 */
	private final int mBirdUpDis = Util.dp2px(getContext(), TOUCH_UP_SIZE);

	private int mTmpBirdDis;
	/**
	 * 鸟自动下落的距离
	 */
	private final int mAutoDownSpeed = Util.dp2px(getContext(), 2);

	/**
	 * 两个管道间距离
	 */
	private int PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);
	/**
	 * 记录移动的距离，达到 PIPE_DIS_BETWEEN_TWO 则生成一个管道
	 */
	private int mTmpMoveDistance = 0;
	/**
	 * 记录需要移除的管道
	 */
	private List<Pipe> mNeedRemovePipe = new ArrayList<Pipe>();

	/**
	 * 处理一些逻辑上的计算
	 */
	private void logic()
	{
		switch (mStatus)
		{
		case RUNNING:

			mGrade = 0;
			// 更新我们地板绘制的x坐标，地板移动
			mFloor.setX(mFloor.getX() - mSpeed);

			logicPipe();

			// 默认下落，点击时瞬间上升
//			mTmpBirdDis += mAutoDownSpeed;
			System.out.println("BirdY:"+(mFloor.getY()-(int)(mFloor.getY()*mBirdY/200000.000)));
			mBird.setY(mFloor.getY()-(int)(mFloor.getY()*mBirdY/(maxpower * 1000)));
			if(mBird.getY() > mFloor.getY() - mBird.getHeight())
				mBird.setY(mFloor.getY() - mBird.getHeight());
			if(mBird.getY()<=0)
				mBird.setY(0);
			// 计算分数
			mGrade += mRemovedPipe;
			for (Pipe pipe : mPipes)
			{
				if (pipe.getX() + mPipeWidth < mBird.getX())
				{
					mGrade++;
					if((mGrade >= planGrade)&&(trainlevel>=Highesttrainlevel)&&(trainlevel<10))
					{
					SharedPreferences prefs = App.get().getSharedPreferences(
			        		filename,
			                Context.MODE_PRIVATE
			        );
			        SharedPreferences.Editor editor = prefs.edit();
			        editor.putString(TRAINLEVEL, String.valueOf(trainlevel+1));
			        editor.commit();
			        //System.out.println("succee!");
					}
				}
			}
			checkGameOver();

			break;

		case STOP: // 鸟落下
			// 如果鸟还在空中，先让它掉下来
			if (mBird.getY() < mFloor.getY() - mBird.getWidth())
			{
				mTmpBirdDis += mAutoDownSpeed;
				mBird.setY(mBird.getY() + mTmpBirdDis);
			} else
			{
				mStatus = GameStatus.ASKING;
				gameOverflag = true;
				initPos();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 重置鸟的位置等数据
	 */
	public void initPos()
	{
		mPipes.clear();
		//立即增加一个
//		mPipes.add(new Pipe(getContext(), getWidth(), getHeight(), mPipeTop,
//				mPipeBottom, trainlevel, mPipeUpDownFlag));
		mNeedRemovePipe.clear();
		// 重置鸟的位置
		// mBird.setY(mHeight * 2 / 3);
		mBird.resetHeigt();
		// 重置下落速度
		mTmpBirdDis = 0;
		mTmpMoveDistance = 0 ;
		mRemovedPipe = 0;
		PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);
		mPipeType = 1; //训练管道类型
		mPipeModel = 1;  //训练管道模版
		mPipesetcount = 6;  //训练管道一套波形计数
		succeecount = 0;  //训练管道一套波形计数
		succeemodel = 1;  //训练管道一套波形计数
		mPipeCount = 0;  //训练管道计算
		mPipeFirstCount = true; //每个训练管道类型切换后需要间隔一个管道
		mPipeUpDownFlag = true; //训练管道向上还是向下标记
		PipeFirstCount = true;  //首个管道距离设置为300
//		mGrade = 0;      //当前分数
//		lastmGrade = 0;      //当前分数
		mRemovedPipe = 0;  //已经通过的管道
		mRemovedPipeflag = false;  //已经通过的管道
		minute = traintime;
	
//		gameOverflag = false;
		
	}

	private void checkGameOver()
	{

		// 如果触碰地板，gg
//		if (mBird.getY() > mFloor.getY() - mBird.getHeight())
//		{
//			mStatus = GameStatus.STOP;
//		}
		
		// 如果撞到管道
		for (Pipe wall : mPipes)
		{
			// 已经穿过的
			if (wall.getX() + mPipeWidth < mBird.getX())
			{
				continue;
			}
			if (wall.touchBird(mBird))
			{
//				TouchInit();
//				mStatus = GameStatus.STOP;
				if(!mRemovedPipeflag)
				{
					//每碰到一次石头,减5分
					mRemovedPipe -=5;
					//切换减分计数标记
					mRemovedPipeflag = true;
					touchflag = true;
				}
				//播放碰撞音效
				mediaPlayer_hit.start();
				//减到负分后置为0分
				if(mRemovedPipe <0)
					mRemovedPipe = 0;
				break;
			}
		}
	}

	private void logicPipe()
	{
		// 管道移动
		for (Pipe pipe : mPipes)
		{
			if (pipe.getX() < -mPipeWidth)
			{
				mNeedRemovePipe.add(pipe);
				mRemovedPipe++;
//				mediaPlayer_succee.start();
				continue;
			}
			pipe.setX(pipe.getX() - mSpeed);
		}
		// 移除管道
		mPipes.removeAll(mNeedRemovePipe);
		mNeedRemovePipe.clear();

		// Log.e("TAG", "现存管道数量：" + mPipes.size());

		// 管道
		mTmpMoveDistance += mSpeed;
//		mTmpMoveDistance ++;
		// 生成一个管道
		if (mTmpMoveDistance >= PIPE_DIS_BETWEEN_TWO)
		{
			Pipe pipe = new Pipe(getContext(), getWidth(), getHeight(),
					mPipeTop, mPipeBottom , trainlevel, mPipeUpDownFlag);
			mPipes.add(pipe);
			mTmpMoveDistance = 0;
			mPipeCount++;
			if(mPipeType == 3)
			{
				mPipeUpDownFlag = !mPipeUpDownFlag;
				mPipesetcount = 6;
//				mediaPlayer_succee.start();
			}
			if(mPipeCount>=mPipeModel)
			{
				if(mPipeFirstCount)
				{
					PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 300);
					mPipeUpDownFlag = true;
					mPipeFirstCount = false;
					mPipeCount = 0;
					mPipeModel = 1;
					mPipesetcount++;
//					mPipesetcount = 6;
				}else
				{
//					mediaPlayer_succee.start();
					setPipeType();
				}
			}
		}
	}

	private void setPipeType()
	{
		mPipeUpDownFlag = false;
		if(PipeFirstCount)
		{
			PipeFirstCount = false;
		}else
		{
			mPipeFirstCount = true;
	    PipeFirstCount = true;
//		Random random = new Random();
//		mPipeType = random.nextInt(6) + 1;
	    if(mPipesetcount > 5)
	    {
		    mPipeType++;
		    if(mPipeType > 3)
		    {
		    	mPipeType = 1;
		    }
		    mPipesetcount = 1;
	    }
//	    callback.adjust();
//		System.out.println("mPipeType:" + mPipeType);
		switch (mPipeType) {
		case 1:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);
			mPipeCount = 0;
			mPipeModel = 6;
//			mSpeed = Util.dp2px(getContext(), trainlevel+2);
			break;
		case 2:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);
			mPipeCount = 0;
			mPipeModel = Pipelength+2;
//			mSpeed = Util.dp2px(getContext(), 13-trainlevel);
			break;
		case 4:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);
			mPipeCount = 0;
			mPipeModel = 3;
//			mSpeed = Util.dp2px(getContext(), 13-trainlevel);
			break;
		case 6:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);
			mPipeCount = 0;
			mPipeModel = trainlevel+3;
//			mSpeed = Util.dp2px(getContext(), 13-trainlevel);
			break;
		case 5:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);
			mPipeCount = 0;
			mPipeModel = trainlevel+4;
//			mSpeed = Util.dp2px(getContext(), 13-trainlevel);
			break;
		case 3:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 400);
			mPipeCount = 0;
			mPipeModel = Pipenum*2+1;
//			mSpeed = Util.dp2px(getContext(), trainlevel+2);
			break;
		case 7:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 400);
			mPipeCount = 0;
			mPipeModel = 7;
//			mSpeed = Util.dp2px(getContext(), trainlevel+2);
			break;
		default:
			PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 400);
			mPipeCount = 0;
			mPipeModel = 5;
//			mPipesetcount = 6;
			break;
		}
		}
	}
	
//	Runnable runnable = new Runnable() {
//
//		//波形显示线程
//			@Override
//			public void run() {
//				if (second != 0) {
//					second--;
//				} else {
//					minute--;
//					second = 59;
//				}
//			}
//	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		int action = event.getAction();
		x = event.getX();
		y = event.getX();
		if (action == MotionEvent.ACTION_DOWN)
		{
			if((x>=0)&&(x<=100)&&(y>=0)&&(y<=100))
			{
				callback.adjust();
			}else if(Startflag)
			{
//				stop();
				switch (mStatus)
				{
					case WAITTING:
						mStatus = GameStatus.RUNNING;
						timeRunningflag = true;
						timeRunningfirstflag = true;
						mediaPlayer.start();
		//				mHandler.postDelayed(runnable, 1000);
						break;
					case RUNNING:
						mTmpBirdDis = mBirdUpDis;
						break;
					case ASKING:
						callback.adjust();
						break;
				}
			}
		}

		return true;

	}
	private Context context;
	
	public GameFlabbyBird(Context context)
	{
		this(context, null);
		this.context=context;
	}

	public GameFlabbyBird(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mHolder = getHolder();
		mHolder.addCallback(this);

		setZOrderOnTop(true);// 设置画布 背景透明
		mHolder.setFormat(PixelFormat.TRANSLUCENT);

		// 设置可获得焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		// 设置常亮
		this.setKeepScreenOn(true);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);


		// 初始化速度
		// mSpeed = Util.dp2px(getContext(), 2);
		mPipeWidth = Util.dp2px(getContext(), PIPE_WIDTH);
//		reload();
		initBitmaps();
		Setparameter();
		mediaPlayer = MediaPlayer.create(context, R.raw.beijing);
		mediaPlayer_hit = MediaPlayer.create(context, R.raw.hit3);
		mediaPlayer_succee = MediaPlayer.create(context, R.raw.succee);
	}

	/**
	 * 初始化图片
	 */
	private void initBitmaps()
	{
		mBg = loadImageByResId(R.drawable.bg_recover_gamepage_background);
		mBirdBitmap = loadImageByResId(R.drawable.bg_recover_gamepage_fish);
		mFloorBg = loadImageByResId(R.drawable.bg_recover_gamepage_floor);
		mPipeTop = loadImageByResId(R.drawable.icon_recover_gamepage_upstone1);
		mPipeBottom = loadImageByResId(R.drawable.icon_recover_gamepage_downstone12);
		mback = loadImageByResId(R.drawable.img_back);
		mNumBitmap = new Bitmap[mNums.length];
		for (int i = 0; i < mNumBitmap.length; i++)
		{
			mNumBitmap[i] = loadImageByResId(mNums[i]);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
//		Log.e("TAG", "surfaceCreated");

		// 开启线程
		isRunning = true;
		t = new Thread(this);
		t.start();	
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
//		Log.e("TAG", "surfaceDestroyed");
		// 通知关闭线程
		isRunning = false;
	}

	@Override
	public void run()
	{
		while (isRunning)
		{
			if(isRunningflag)
			{
				long start = System.currentTimeMillis();
				if(timeRunningfirstflag)
				{
					if(timeRunningflag)
					{
						secontcount++;
						swimcount++;
						scorereducecount++;
						if(secontcount >=16)
						{
							if (second != 0) {
								second--;
							} else {
								minute--;
								second = 59;
							}
							secontcount = 0;
							if((minute == 0)&&(second == 0))
							{
								mStatus = GameStatus.STOP;
								timeRunningfirstflag = false;
//								latch.countDown();
//								isRunning = false;
							}
						}
						if(swimcount >=100)
						{
							swimcount = 0;
						}
						if(scorereducecount >= 48)
						{
							mRemovedPipeflag = false;
							scorereducecount = 0;
							touchflag = false;
						}
					}
				}
				if((mediaPlayer!=null)&&(mediaPlayer.isPlaying() != true))
				{
					mediaPlayer.start();
				}
				logic();
				draw();
				long end = System.currentTimeMillis();
				try
				{
					if (end - start < 50)
					{
						Thread.sleep(50 - (end - start));
					}
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
//		if(isRunning == false)
//		{
//			callback.adjust();
//		}
	}

	private void draw()
	{
		try
		{
			// 获得canvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null)
			{
				// drawSomething..

				drawBg();
				drawBird();

				drawPipes();

				drawFloor();

				drawGrades();

			}
		} catch (Exception e)
		{
		} finally
		{
			if (mCanvas != null)
				mHolder.unlockCanvasAndPost(mCanvas);
		}
	}

	private void drawFloor()
	{
		mFloor.draw(mCanvas, mPaint);
	}

	/**
	 * 绘制背景
	 */
	private void drawBg()
	{
		mCanvas.drawBitmap(mBg, null, mGamePanelRect, null);
	}

	private void drawBird()
	{
		mBird.draw(mCanvas);
	}

	/**
	 * 绘制管道
	 */
	private void drawPipes()
	{
		for (Pipe pipe : mPipes)
		{
			// pipe.setX(pipe.getX() - mSpeed);
			pipe.draw(mCanvas, mPipeRect);
		}
	}

	/**
	 * 初始化尺寸相关
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{

		super.onSizeChanged(w, h, oldw, oldh);

		mWidth = w;
		mHeight = h;
		mGamePanelRect.set(0, 0, w, h);

		// 初始化mBird
		mBird = new Bird(getContext(), mWidth, mHeight, mBirdBitmap);
		// 初始化地板
		mFloor = new Floor(mWidth, mHeight, mFloorBg);
		// 初始化管道范围
		mPipeRect = new RectF(0, 0, mPipeWidth, mHeight);

//		Pipe pipe = new Pipe(getContext(), w, h, mPipeTop, mPipeBottom, trainlevel, mPipeUpDownFlag);
//		mPipes.add(pipe);
		PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 70);

		// 初始化分数
		mSingleGradeHeight = (int) (h * RADIO_SINGLE_NUM_HEIGHT);
		mSingleGradeWidth = (int) (mSingleGradeHeight * 1.0f
				/ mNumBitmap[0].getHeight() * mNumBitmap[0].getWidth());
		mSingleNumRectF = new RectF(0, 0, mSingleGradeWidth, mSingleGradeHeight);
	}

	/**
	 * 绘制分数
	 */
	private void drawGrades()
	{
		String grade = mGrade + "";
		mCanvas.save();
		mCanvas.translate(mWidth / 2 - grade.length() * mSingleGradeWidth / 2,
				1f / 8 * mHeight);
		// draw single num one by one
		for (int i = 0; i < grade.length(); i++)
		{
			String numStr = grade.substring(i, i + 1);
			int num = Integer.valueOf(numStr);
			mCanvas.drawBitmap(mNumBitmap[num], null, mSingleNumRectF, null);
			mCanvas.translate(mSingleGradeWidth, 0);
		}
		
		Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);  
		textPaint.setTextSize(55);  
		textPaint.setColor( Color.WHITE);  
		float baseX = 1f / 13 * mWidth;  
		float baseY = 1f / 17 * mHeight; 
		baseX = -(1f / 2 * mWidth);
		mCanvas.drawBitmap(mback, baseX, 0, textPaint);
		baseX = 1f / 13 * mWidth;  
		baseY = 1f / 17 * mHeight; 
		//mCanva以分数为基础进行位移，分数显示在中央，-为左移，+为右移
		mCanvas.drawText("目标"+planGrade, baseX, baseY, textPaint);
		baseX = -(3f / 12 * mWidth);  
		mCanvas.drawText("等级"+String.valueOf(trainlevel), baseX, baseY, textPaint);
		baseX = -(5f / 12 * mWidth);  
		mCanvas.drawText("方案"+mplan, baseX, baseY, textPaint);
		baseX = 4f / 13 * mWidth; 
		mCanvas.drawText(String.valueOf(minute)+"分"+String.valueOf(second)+"秒", baseX, baseY, textPaint);
		baseX = -70;  
		baseY = 3f / 17 * mHeight; 
		if(touchflag)
		{
			mCanvas.drawText("-5", baseX, baseY, textPaint);
		}
		mCanvas.restore();
		if(mGrade != lastmGrade)
		{
			if(mGrade > lastmGrade)
			{
				succeecount++;
				if(succeecount >= succeemodel)
				{
					mediaPlayer_succee.start();
					succeecount = 0;
					succeemodel = mPipeModel;
				}
			}
			lastmGrade = mGrade;
		}
	}

	/**
	 * 根据resId加载图片
	 * 
	 * @param resId
	 * @return
	 */
	private Bitmap loadImageByResId(int resId)
	{
		return BitmapFactory.decodeResource(getResources(), resId);
	}
	
	public void setSpeed(int speed)
	{
		mSpeed = Util.dp2px(getContext(), speed);
	}
	
	//撞击后复位
	private void TouchInit()
	{
		mPipeType = 1; //训练管道类型
		mPipeModel = 1;  //训练管道模版
		mPipeCount = 0;  //训练管道计算
		mPipeFirstCount = true; //每个训练管道类型切换后需要间隔一个管道
		mPipeUpDownFlag = true; //训练管道向上还是向下标记
		PipeFirstCount = true;  //首个管道距离设置为300
		mPipesetcount = 6;
	}
	
//	private void stop() {
//		// TODO Auto-generated method stub
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("1111");
//	}
	
	/** fun：读取本地数据
	 *  aditor：zhou
	 *  date:20170317
	 *  describe:
	 *  从本地文件读取用户信息进行显示，若无则显示默认值
	 */
	private void reload() {
        SharedPreferences prefs = App.get().getSharedPreferences(
        		"userinfo",
                Context.MODE_PRIVATE
        );       
//       mplan = prefs.getString("trainplan", "A"); 
//       trainlevel = Integer.valueOf(prefs.getString("trainlevel", "1"));
//       traintime = Integer.valueOf(prefs.getString("traintime", "5"));
       minute = traintime;
       planGrade = (minute/5)*150;
	}
	
	
	//设置训练参数
	private void Setparameter() {
		if(mplan.equals("A"))
		{
			Pipelength = 5;
			Pipenum = 3;
		}else if(mplan.equals("B"))
		{
			Pipelength = 7;
			Pipenum = 3;
		}else if(mplan.equals("C"))
		{
			Pipelength = 9;
			Pipenum = 3;
		}else if(mplan.equals("D"))
		{
			Pipelength = 11;
			Pipenum = 3;
		}else if(mplan.equals("E"))
		{
			Pipelength = 13;
			Pipenum = 3;
		}else if(mplan.equals("F"))
		{
			Pipelength = 5;
			Pipenum = 5;
		}else if(mplan.equals("G"))
		{
			Pipelength = 7;
			Pipenum = 5;
		}else if(mplan.equals("H"))
		{
			Pipelength = 9;
			Pipenum = 5;
		}else if(mplan.equals("I"))
		{
			Pipelength = 11;
			Pipenum = 5;
		}else if(mplan.equals("J"))
		{
			Pipelength = 13;
			Pipenum = 5;
		}else if(mplan.equals("K"))
		{
			Pipelength = 5;
			Pipenum = 7;
		}else if(mplan.equals("L"))
		{
			Pipelength = 7;
			Pipenum = 7;
		}else if(mplan.equals("M"))
		{
			Pipelength = 9;
			Pipenum = 7;
		}else if(mplan.equals("N"))
		{
			Pipelength = 11;
			Pipenum = 7;
		}else if(mplan.equals("O"))
		{
			Pipelength = 13;
			Pipenum = 7;
		}else if(mplan.equals("P"))
		{
			Pipelength = 5;
			Pipenum = 9;
		}else if(mplan.equals("Q"))
		{
			Pipelength = 7;
			Pipenum = 9;
		}else if(mplan.equals("R"))
		{
			Pipelength = 9;
			Pipenum = 9;
		}else if(mplan.equals("S"))
		{
			Pipelength = 11;
			Pipenum = 9;
		}else if(mplan.equals("T"))
		{
			Pipelength = 13;
			Pipenum = 9;
		}else if(mplan.equals("U"))
		{
			Pipelength = 5;
			Pipenum = 9;
		}else if(mplan.equals("V"))
		{
			Pipelength = 7;
			Pipenum = 9;
		}else if(mplan.equals("W"))
		{
			Pipelength = 9;
			Pipenum = 9;
		}else if(mplan.equals("X"))
		{
			Pipelength = 11;
			Pipenum = 9;
		}else if(mplan.equals("Y"))
		{
			Pipelength = 13;
			Pipenum = 9;
		}
		
//		System.out.println("mplan"+mplan);
//		System.out.println("Pipelength:"+Pipelength);
//		System.out.println("Pipenum:"+Pipenum);
	}	
	
	private MyCallInterface callback;
	
	public void setCallback(MyCallInterface callback) {
		this.callback = callback;
	}

	public interface MyCallInterface  
	{  
	    public void method();  
	    public void adjust();    
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
	
	
	
	
 
}
