package com.health.hl.recover;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.health.hl.R;
import com.health.hl.adapter.MyData;
import com.health.hl.app.App;
import com.health.hl.other.JSONHelper;
import com.health.hl.service.FileService;
import com.health.hl.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class RecoverSaveReportActivity extends Activity{

	WebView webView;
	Button btnSave;
	private static App session;
//	public static com.itextpdf.text.Rectangle A4 = PageSize.A4;
//	private static final String FOLDER_NAME = "/SnapShotImage/";
	private String userid;		//用户id data：20190820， author：晶 ,传用户数据
	private static String userphone;	//用户手机
	Bitmap bmp = null;
	FileService fileService = null;
	private Notification notification = null;
    private ImageView imgShow;
    File appDir;
    File file;
    String fileName;
    String TAG="ViewActivity";
	private Toast customToast;          //消息框
	private static final int REQUEST_EXTERNAL_STORAGE = 1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		testPermission();
		//开启webview的HTML缓存,这个功能可提高性能开销
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//判断当前开发版本SDK是否大于5.0
		    WebView.enableSlowWholeDocumentDraw();
		}	
		//设置界面布局资源--晶
		setContentView(R.layout.activity_recover_savereport);
		session = (App) getApplication();//App继承了Application_晶
	//	final JsInterface myJavaScriptInterface = new JsInterface(this);  
		initView();
		initData();
		initEvent();
	}

	private void initView() {
//		初始化视图对象webview
		webView = (WebView) findViewById(R.id.print_preview_webview);
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
		//初始化视图对象btnsave
		btnSave = (Button)this.findViewById(R.id.print_preview_btnSave);
	}
	
	private void initEvent() {
		//设置点击监听（只有一个事件需要监听的时候用new的这种方式）
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//v就是发生事件的视图对象（或者说你操作的那个视图）-晶
				webView.setScrollY(0);//
				webView.setInitialScale(100);//代表加载的内容不缩放
				Timer timer = new Timer();  
				MyTimerTask timerTask = new MyTimerTask();  
				//schedule(task,time,period)任务，首次执行任务的时间，执行一次任务的时间间隔
				timer.schedule(timerTask, 1000, 1); // 延迟3秒钟,执行1次  
			}
		});
	}
	
	class MyTimerTask extends TimerTask {  
//	    protected Context context;
		@Override  
	    public void run() {  
	    	RecoverSaveReportActivity.this.runOnUiThread(new Runnable(){
				@Override
				public void run() {
					try {
						bmp = captureWebView(webView);
//						RecoverSaveReportActivity.saveImageToGallery(context,bmp);
						saveImageToGallery(bmp,"my");
					} catch (final Exception e) {
//						showToast( "报告已存储至手机-文件管理-Herllo文件夹下"); 
						Toast.makeText(RecoverSaveReportActivity.this, e.getMessage(), Toast.LENGTH_LONG);
					}
				}
	    	});
//	    	showToast( "报告已存储至手机-文件管理-Herllo文件夹下"); 
	    	this.cancel();
	    }  
	}  
	
	
//	Toast.makeText(RecoverSaveReportActivity.this,"报告已存储至手机-文件管理-Herllo文件夹下",Toast.LENGTH_SHORT).show();
//	showToast( "报告已存储至手机-文件管理-Herllo文件夹下");  
//	String fileName = userphone+fileService.saveBitmapToSDCard(
////			"" + "user.jpg", bmp);
//	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
//	String fileName=userphone+"-"+sdf.format(new Date())+".jpg";
////	Intent intent = getPdfFileIntent(Save(bmp));
//	Intent intent =saveImageToGallery(bmp);
//	startActivity(intent);
	private void initData() {
		//获取用户ID
		userid = this.getIntent().getStringExtra("userid");
		//获取用户ID
		userphone = this.getIntent().getStringExtra("userphone");
		fileService = new FileService(this);
		
		//通过file方式访问本地文件夹
		webView.loadUrl("file:///android_asset/www/heeytech.html");
		//开启webview缓存功能-晶
		webView.setDrawingCacheEnabled(true);
		//声明WebSettings子类-晶
		WebSettings wSet = webView.getSettings();
 		//webView.buildDrawingCache();//强制绘制缓存-晶
		//webview设置支持JavaScript，访问的页面要与JavaScript交互
		wSet.setJavaScriptEnabled(true);
		//调用WebView关联的WebSettings中addJavaScriptInterface
		webView.addJavascriptInterface(new MyData(Integer.valueOf(userid),"",userphone),"my");
		wSet.setJavaScriptCanOpenWindowsAutomatically(true);//晶
		// 设置可以支持缩放 
		wSet.setSupportZoom(true); 
		// 设置出现缩放工具 
		wSet.setBuiltInZoomControls(true);
		//自适应屏幕
		wSet.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wSet.setLoadWithOverviewMode(true);
		//在js中调用本地java方法
      
       //添加客户端支持
        webView.setWebChromeClient(new WebChromeClient());
       
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) { 
		    // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}			
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}
		});
	}
	
	private class JsInterface {
        private Context mContext;
 
        public JsInterface(Context context) {
            this.mContext = context;
        }
 
        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void showInfoFromJs(String name) {
            Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
        }
    }
	
	public void saveImageToGallery(Bitmap bitmap, String bitName) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Herllo";
//        String storePath = "Herllo";
        File appDir = new File(storePath);//新建一个文件
        if (!appDir.exists()) {   
            appDir.mkdirs();
        }
//        String fileName = System.currentTimeMillis() + ".jpg";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
        String fileName=userphone +"-"+sdf.format(new Date())+".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //保存图片文件时，通过Bitmap的compress()方法将Bitmap对象压缩到一个文件输出流中，然后flush()即可
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            
            //把文件插入到系统图库
//          MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//            MediaStore.Images.Media.insertImage(getContentResolver(),appDir.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
//            Uri uri = Uri.fromFile(file);
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            this.sendBroadcast(intent);
            showToast("保存成功");
//            if (isSuccess) {
//                return true;
//            } else {
//                return false;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return false;
    }
//	public static Intent getPdfFileIntent( File file ){    
//		   
//        Intent intent = new Intent("android.intent.action.VIEW");    
//        intent.addCategory("android.intent.category.DEFAULT");    
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
//        Uri uri = Uri.fromFile(file);    
//        intent.setDataAndType(uri, "application/jpg");//设置有特定格式的uri数据    
//        return intent;    
//    }
	
//	public byte[] Bitmap2Bytes(Bitmap bm) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//		return baos.toByteArray();
//	}
	
	
//	private File Save(Bitmap bitmap) {//方法返回一个PDF格式的文件
		// 首先保存图片
//		String state = Environment.getExternalStorageState();
//	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
//      String fileName=userphone+"-"+sdf.format(new Date())+".jpg";
//      String savestate = MediaStore.Images.Media.insertImage(getContentResolver(),
//		bitmap, fileName, null);
//      if(!savestate.equals(null))
//      {
//    	  showToast( "报告已存储");  
//      }
		
//        File appDir = new File(Environment.getExternalStorageDirectory(), "herllo1");
//        if (!appDir.exists()) {
//        	boolean savedir = appDir.mkdirs();
//        	if(savedir = true)
//        	{
//        		int a = 1;
//        	}
//        }
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
//        String fileName=userphone+"-"+sdf.format(new Date())+".jpg";
//        File file = new File(appDir, fileName);
//        System.out.println( file.getAbsolutePath().toString());
//        System.out.println( appDir.toString());
//        if (bitmap != null) {
//            try {
//                FileOutputStream fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
////                MediaStore.Images.Media.insertImage(getContentResolver(),
////                		bitmap, fileName, null);
//                fos.flush();
//                fos.close();
////                String path = Intent(this,bitmap);
//                showToast( "报告已存储");              
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        showToast("报告已存储！");
////      其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(getContentResolver(),appDir.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//         //最后通知图库更新
//        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//       //  scanIntent.setData(Uri.fromFile(new File("/sdcard/Damily/"+fileName)));
//       scanIntent.setData(Uri.fromFile(new File(Environment.getExternalStorageDirectory() +"/荷露/"+ fileName)));
//         sendBroadcast(scanIntent);
//         // sendNotification();下一篇博客实现
//         return scanIntent;
//	 }
	


//private String Intent(RecoverSaveReportActivity recoverSaveReportActivity, Bitmap bitmap) {
//	// TODO Auto-generated method stub
//	return null;
//}
	//下面注释掉的是讲保存的截图转成PDF
//		File localFile = new File(Environment.getExternalStorageDirectory()
//				+ FOLDER_NAME + "user.pdf");
//		Document localDocument = new Document(A4);
//		localDocument.setMargins(0.0F, 0.0F, 0.0F, 0.0F);
//		PdfWriter localPdfWriter = null;
//		while (true) {
//			Image localImage = null;
//			try {
//				localPdfWriter = PdfWriter.getInstance(localDocument,
//						new FileOutputStream(localFile));
//				localPdfWriter.open();
//				localDocument.open();
//				localImage = Image.getInstance(Bitmap2Bytes(bmp));
//				float f1 = localImage.getHeight() / localImage.getWidth();
//				float f2 = A4.getHeight() / A4.getWidth();
//				localImage.scaleToFit(A4.getWidth(), A4.getHeight());
//				localDocument.add(localImage);
//				return localFile;
//			} catch (Exception localException) {
//			} finally {
//				try {
//					if (localPdfWriter != null)
//						localPdfWriter.close();
//					if (localDocument != null)
//						localDocument.close();
//				} catch (Exception e) {
//					System.out.println(e.getMessage());
//				}
//			}
//			localImage.scaleToFit(A4.getHeight() / localImage.getHeight()
//					* localImage.getWidth(), A4.getHeight());
//		}
//	}

	/**
	 * 截取webView快照(webView加载的整个内容的大小)
	 * 
	 * @param webView
	 * @return
	 */
	private Bitmap captureWebView(WebView webView) {
		Picture snapShot = webView.capturePicture();
		Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
				snapShot.getHeight(), Bitmap.Config.ARGB_8888);//ARGB_8888代表一种32位的位图--晶
		Canvas canvas = new Canvas(bmp);
		snapShot.draw(canvas);
		return bmp;
	}
	public class AndroidToastForJs {
		
		private Context mContext;
		private Object printModel;

		public AndroidToastForJs(Context context,Object printModel){
				this.mContext = context;
				this.printModel = printModel;
		}
		
		//webview中调用toast原生组件
		public void showToast(String toast) {
				Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		}
			
		 //以json实现webview与js之间的数据交互
		public String jsontohtml(){
				return JSONHelper.toJSON(printModel);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 if (webView != null) {
			 webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
			 webView.clearHistory();

	           // ((ViewGroup) webView.getParent()).removeView(webView);
	            webView.destroy();
	            webView = null;
	        }
	        super.onDestroy();
	}

	//显示消息
//	private void showToast(String text) {
//		if (customToast == null) {
//			customToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//		} else {
//			customToast.setText(text);
//			customToast.setDuration(Toast.LENGTH_SHORT);
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
	   * 获取读取内存权限
	   * @param v1.0
	   * @author 晶
	   */
	public void testPermission (){
	        String[] permissions;
	       //读取内存权限
	                permissions = new String[]{
	                        Manifest.permission.WRITE_EXTERNAL_STORAGE
	                };
	                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
	                    }else{ //否则去获取权限
	                    	getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,permissions);
	                }
	    }
	  
	  //检查某个权限是否已经获得
	   private boolean checkPermission(String permission){
	        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
	        if (ActivityCompat.checkSelfPermission(this, permission)
	                == PackageManager.PERMISSION_GRANTED)
	            return true;
	        else
	            return false;
	    }
	  //获取权限
	    private void getPermission(String permission,String [] permissions) {
	        //申请权限
	        ActivityCompat.requestPermissions(
	                this,
	                permissions,
	                REQUEST_EXTERNAL_STORAGE);
	 
	        //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
	        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
//	            Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
	        	showToast( "请手动在设置中开启存储权限，否则无法正常使用报告存储功能！"); //但在实际测试的时候并没没有出现提示框
	    }
}
