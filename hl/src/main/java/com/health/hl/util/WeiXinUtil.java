package com.health.hl.util;

import com.tencent.mm.sdk.openapi.IWXAPI;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class WeiXinUtil {
	public static boolean isWXAppInstalledAndSupported(Context context,
			IWXAPI api) {
		// LogOutput.d(TAG, "isWXAppInstalledAndSupported");
		boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
				&& api.isWXAppSupportAPI();
		if (!sIsWXAppInstalledAndSupported) {
			Log.w("", "~~~~~~~~~~~~~~微信客户端未安装，请确认");
//			Toast.makeText(context, "微信客户端未安装，请确认", Toast.LENGTH_SHORT).show();
//			GameToast.showToast(context, "微信客户端未安装，请确认");
		}

		return sIsWXAppInstalledAndSupported;
	}
}
