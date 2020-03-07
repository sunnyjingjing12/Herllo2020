package com.health.hl.recover;

/** page：添加用户信息页面
 *  aditor：zhou
 *  date:20170412
 *  describe:
 *  添加或者修改用户信息,若修改用户信息会先从本地SP文件读取用户信息
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.health.hl.R.color;
import com.health.hl.adapter.Goods;
import com.health.hl.adapter.GoodsProgress;
import com.health.hl.adapter.UserCaseListViewAdapter;
import com.health.hl.adapter.UserCaseListViewAdapter.InnerItemOnclickListener;
import com.health.hl.adapter.UserCaseListViewProgressAdapter;
import com.health.hl.app.App;
import com.health.hl.R;

import android.R.string;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class RecoverRankingActivity extends Activity implements
		OnClickListener,InnerItemOnclickListener,  
		OnItemClickListener{


	private ImageView btnBack;
	List<Goods> list = new ArrayList<Goods>();  
	List<GoodsProgress> list_progress = new ArrayList<GoodsProgress>();
	UserCaseListViewAdapter adapter = null;
	UserCaseListViewProgressAdapter adapterProgress = null;
	private InputMethodManager manager;
	private Button btnPelvicRank;
	private Button btnProgressRank;
	private String userId = "631";               			//用户ID
	private String netPath;									//接口网址
	private String netPara;									//接口参数
	private String userData;								//接口数据
	private String userName[] = new String[100];   			//排行榜用户名
	private String userScore[] = new String[100];   		//排行榜用户得分
	private String userMaxPower[] = new String[100];  		//排行榜用户得分
	private String userOneMuscle[] = new String[100];   	//排行榜用户得分
	private String userTwoMuscle[] = new String[100];   	//排行榜用户得分
	private String userMaxPowerVary[] = new String[100];   	//排行榜用户得分
	private String userProgressNum[] = new String[100];   	//排行榜用户得分
	private String nowUserScore = "-";   					//当前用户得分
	private String nowUserRank = "-";   					//当前用户排名
	private String nowUserPercentage = "-";   				//当前用户超越百分比
	private String nowUserProgressRank = "-";   					//当前用户进步排名
	private String nowUserProgressNum = "-";   					//当前用户进步指数
	private String nowUserProgressPercentage = "-";   			//当前用户进步百分比
	private TextView textSocreTitle;
	private TextView textSocre;
	private TextView textRankTitle;
	private TextView textRank;
	private LinearLayout layoutRank;
	private LinearLayout layoutProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		manager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initEvent();
		initData();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		//开始测试按钮
		btnBack = (ImageView) findViewById(R.id.iv_recover_ranking_back);
		btnBack.setOnClickListener(this);
		//综合排行榜按钮
		btnPelvicRank = (Button) findViewById(R.id.btn_recover_ranking_pelvicrank);
		btnPelvicRank.setOnClickListener(this);
		//进步排行榜按钮
		btnProgressRank = (Button) findViewById(R.id.btn_recover_ranking_progressrank);
		btnProgressRank.setOnClickListener(this);
		
		//得分标题或者进步指数标题
		textSocreTitle = (TextView) findViewById(R.id.tv_ranking_progressindex);
		//得分或者进步指数
		textSocre = (TextView) findViewById(R.id.tv_ranking_progressindexdata);
		//得分或者进步指数
		textRankTitle = (TextView) findViewById(R.id.tv_ranking_ranking);
		//得分或者进步指数
		textRank = (TextView) findViewById(R.id.tv_ranking_rankingdata);
		
		//综合得分榜线性布局
		layoutRank = (LinearLayout) findViewById(R.id.ll_ranking_rank);
		//进步榜线性布局
		layoutProgress = (LinearLayout) findViewById(R.id.ll_ranking_progress);
	}

	private void initData() {
		// TODO Auto-generated method stub
		//读取用户ID
		userId = this.getIntent().getStringExtra("userid");
		if(userId.equals("0"))
		{
			userId = "303";
		}
		//读取排行榜数据
		reLoad(1);
		//设置表格标题的背景颜色  
        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.include_listview);  
        ListView tableListView = (ListView) findViewById(R.id.lv_recover_userCase_listData);  
        AdapterView.OnItemClickListener mLeftListOnItemClick = new AdapterView.OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {  
                  
                adapter.setSelectItem(arg2);  
                adapter.notifyDataSetInvalidated();  
//              adapter.notifyDataSetChanged();  
            }  
              
        };   
		try {
			    Thread.sleep(1000);
		        for(int i=0;i<100;i++)
		        {
		        	list.add(new Goods(i+1+"", userName[i], userScore[i],userMaxPower[i],userOneMuscle[i],userTwoMuscle[i]));
		        }
			    adapter = new UserCaseListViewAdapter(this, list);  
		        adapter.setOnInnerItemOnClickListener(this);
		        tableListView.setAdapter(adapter);  
		        tableListView.setOnItemClickListener(mLeftListOnItemClick);
		        textSocre.setText(nowUserScore);
		        if(nowUserRank.equals("-"))
		        {
		        	textRankTitle.setText("超过了0%的用户");
		        	textRank.setText("");
		        }else if(Integer.valueOf(nowUserRank)<=100)
		        {
		        	textRankTitle.setText("当前排名：");
		        	textRank.setText(nowUserRank);
		        }else
		        {
		        	textRankTitle.setText("超过了"+nowUserPercentage+"%的用户");
		        	textRank.setText("");
		        }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_recover_ranking);
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		// 返回上一页
		case R.id.iv_recover_ranking_back:
			finish();
			break;
		// 综合排行榜
		case R.id.btn_recover_ranking_pelvicrank:
			//设置相关按钮颜色
			btnPelvicRank.setTextColor(0xFFF186FD);
			btnProgressRank.setTextColor(color.bg_recover_analysispage_wordtitle);
			layoutRank.setVisibility(View.VISIBLE);
			layoutProgress.setVisibility(View.GONE);
			textSocreTitle.setText("您的综合得分为：");
	        textSocre.setText(nowUserScore);
	        if(nowUserRank.equals("-"))
	        {
	        	textRankTitle.setText("超过了0%的用户");
	        	textRank.setText("");
	        }else if(Integer.valueOf(nowUserRank)<=100)
	        {
	        	textRankTitle.setText("当前排名：");
	        	textRank.setText(nowUserRank);
	        }else
	        {
	        	textRankTitle.setText("超过了"+nowUserPercentage+"%的用户");
	        	textRank.setText("");
	        }
			break;
	    // 进步排行榜
		case R.id.btn_recover_ranking_progressrank:
			//设置相关按钮颜色
			btnProgressRank.setTextColor(0xFFF186FD);
			btnPelvicRank.setTextColor(color.bg_recover_analysispage_wordtitle);
			reLoad(2);
			layoutRank.setVisibility(View.GONE);
			layoutProgress.setVisibility(View.VISIBLE);
			if(adapterProgress == null)
			{
				//设置表格标题的背景颜色  
		        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.include_listview_progress);  
		        ListView tableListView = (ListView) findViewById(R.id.lv_recover_userCase_listDataProgress);  
		        AdapterView.OnItemClickListener mLeftListOnItemClick = new AdapterView.OnItemClickListener() {  
		            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {  
		                  
		                adapter.setSelectItem(arg2);  
		                adapter.notifyDataSetInvalidated();  
	//	              adapter.notifyDataSetChanged();  
		            }  	              
		        };   
				try {
					    Thread.sleep(1000);
				        for(int i=0;i<100;i++)
				        {
				        	list_progress.add(new GoodsProgress(i+1+"", userName[i], userProgressNum[i],userMaxPowerVary[i]));
				        }
					    adapterProgress = new UserCaseListViewProgressAdapter(this, list_progress);  
	//				    adapterProgress.setOnInnerItemOnClickListener(this);
				        tableListView.setAdapter(adapterProgress);  
				        tableListView.setOnItemClickListener(mLeftListOnItemClick);
	//			        textSocre.setText(nowUserScore);
	//			        if(Integer.valueOf(nowUserRank)<=100)
	//			        {
	//			        	textRankTitle.setText("当前排名：");
	//			        	textRank.setText(nowUserRank);
	//			        }else
	//			        {
	//			        	textRankTitle.setText("超过了"+nowUserPercentage+"%的用户");
	//			        	textRank.setText("");
	//			        }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			textSocreTitle.setText("您的进步指数为：");
	        textSocre.setText(nowUserProgressNum);
	        if(nowUserRank.equals("-"))
	        {
	        	textRankTitle.setText("超过了0%的用户");
	        	textRank.setText("");
	        }else if(Integer.valueOf(nowUserProgressRank)<=100)
	        {
	        	textRankTitle.setText("当前排名：");
	        	textRank.setText(nowUserProgressRank);
	        }else
	        {
	        	textRankTitle.setText("超过了"+nowUserProgressPercentage+"%的用户");
	        	textRank.setText("");
	        }
			break;
		default:
			break;
		}
	}   
	
	@Override  
    public void itemClick(View v) {  
        int position;  
        position = (Integer) v.getTag();  
        switch (v.getId()) {  
//        case R.id.lv_recover_usercase_operation:  
//            list.remove(position);
//            for(int i=0;i<list.size();i++)
//            {
//            	list.get(i).setId(i+1+"");
//            }
//            adapter.notifyDataSetChanged();
//            break;  
        default:  
            break;  
        }  
          
    } 
	
	@Override  
    public void onItemClick(AdapterView<?> parent, View view, int position,  
            long id) {  
		position += 1; 
    }
	
	/**
	 * fun：读取本地用户信息 aditor：zhou date:20170412 describe: 若有SP文件则从本地读取用户信息进行显示，目前只读取用户手机用户获取服务器数据
	 */
	public void reLoad(final int readFlag) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if(readFlag == 1)
					{
						userData = getJson("http://119.23.248.54/Ls_Server_web/rank/synthesizeRank?userId="+userId);
						rankDataShow(userData);
					}else if (readFlag == 2)
					{
						userData = getJson("http://119.23.248.54/Ls_Server_web/rank/progressRank?userId="+userId);
						progressDataShow(userData);
					}else
					{
						userData = getJson("http://119.23.248.54/Ls_Server_web/rank/synthesizeRank?userId="+userId);
						rankDataShow(userData);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
			}
		}).start();

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
	            inputLine = reader.readLine();
	            reader.close();
            }else
            {
            	return "0";
            }
//            a = conn.getResponseCode();
            return inputLine;
    }
	
    /**
   	 * fun：服务器返回数据 aditor：zhou date:20180717 describe: 使用遍历方式,data为服务器返回的数据
   	 */
    private void rankDataShow(String data)
    {
//    	Imlevel = "";
//    	IImlevel = "";
//		maxpower = "";
//		mplan = "";

	    	int a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("userName",a);
		    	a += 10;
		    	if(data.charAt(a)=='"')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!='"')
		    			{
		    				if(i==(a+1))
		    					userName[j] = data.charAt(i)+"";
		    				else
		    					userName[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("combinedScoring",a);
		    	a += 16;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!=',')
		    			{
		    				if(i==(a+1))
		    					userScore[j] = data.charAt(i)+"";
		    				else
		    					userScore[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("maxPower",a);
		    	a += 9;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!=',')
		    			{
		    				if(i==(a+1))
		    					userMaxPower[j] = data.charAt(i)+"";
		    				else
		    					userMaxPower[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("oneMuscle",a);
		    	a += 10;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!=',')
		    			{
		    				if(i==(a+1))
		    					userOneMuscle[j] = data.charAt(i)+"";
		    				else
		    					userOneMuscle[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("twoMuscle",a);
		    	a += 10;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!=',')
		    			{
		    				if(i==(a+1))
		    					userTwoMuscle[j] = data.charAt(i)+"";
		    				else
		    					userTwoMuscle[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	System.out.println(data);
	    	a = data.indexOf("userRank");
	    	a = data.indexOf("rankNo",a);
	    	a += 7;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!=',')
	    			{
	    				if(i==(a+1))
	    					nowUserRank = data.charAt(i)+"";
	    				else
	    					nowUserRank += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	
	    	a = data.indexOf("userRank");
	    	a = data.indexOf("combinedScoring",a);
	    	a += 16;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!=',')
	    			{
	    				if(i==(a+1))
	    					nowUserScore = data.charAt(i)+"";
	    				else
	    					nowUserScore += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	
	    	a = data.indexOf("userRank");
	    	a = data.indexOf("percentage",a);
	    	a += 11;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!=',')
	    			{
	    				if(i==(a+1))
	    					nowUserPercentage = data.charAt(i)+"";
	    				else
	    					nowUserPercentage += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	
	    	a = 0;
    	}
    
    /**
   	 * fun：服务器返回数据 aditor：zhou date:20170911 describe: 使用遍历方式,data为服务器返回的数据
   	 */
    private void progressDataShow(String data)
    {
//    	Imlevel = "";
//    	IImlevel = "";
//		maxpower = "";
//		mplan = "";

	    	int a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("userName",a);
		    	a += 10;
		    	if(data.charAt(a)=='"')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!='"')
		    			{
		    				if(i==(a+1))
		    					userName[j] = data.charAt(i)+"";
		    				else
		    					userName[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("progressNum",a);
		    	a += 12;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!=',')
		    			{
		    				if(i==(a+1))
		    					userProgressNum[j] = data.charAt(i)+"";
		    				else
		    					userProgressNum[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = data.indexOf("ranks");
	    	for(int j=0;j<100;j++)
	    	{
		    	a = data.indexOf("maxPowerVary",a);
		    	a += 13;
		    	if(data.charAt(a)==':')
		    	{
		    		for(int i=a+1;i<data.length()-1;i++)
		    		{
		    			if(data.charAt(i)!=',')
		    			{
		    				if(i==(a+1))
		    					userMaxPowerVary[j] = data.charAt(i)+"";
		    				else
		    					userMaxPowerVary[j] += data.charAt(i);
		    			}else
		    			{
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	a = data.indexOf("userRank");
	    	a = data.indexOf("rankNo",a);
	    	a += 7;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!=',')
	    			{
	    				if(i==(a+1))
	    					nowUserProgressRank = data.charAt(i)+"";
	    				else
	    					nowUserProgressRank += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	
	    	a = data.indexOf("userRank");
	    	a = data.indexOf("progressNum",a);
	    	a += 12;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!=',')
	    			{
	    				if(i==(a+1))
	    					nowUserProgressNum = data.charAt(i)+"";
	    				else
	    					nowUserProgressNum += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	
	    	a = data.indexOf("userRank");
	    	a = data.indexOf("percentage",a);
	    	a += 11;
	    	if(data.charAt(a)==':')
	    	{
	    		for(int i=a+1;i<data.length()-1;i++)
	    		{
	    			if(data.charAt(i)!=',')
	    			{
	    				if(i==(a+1))
	    					nowUserProgressPercentage = data.charAt(i)+"";
	    				else
	    					nowUserProgressPercentage += data.charAt(i);
	    			}else
	    			{
	    				break;
	    			}
	    		}
	    	}
	    	
	    	a = 0;
    	}
    
}
