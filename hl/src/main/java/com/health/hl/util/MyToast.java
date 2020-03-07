package com.health.hl.util;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
		/*public static void toast(Context context,String text){
		
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		
	    }*/

	private static Toast toast = null;

	public static void toast(Context context,String msg) {
	if (toast == null) {
		toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
		toast.setText(msg);
		}
	toast.show();
	}
}
