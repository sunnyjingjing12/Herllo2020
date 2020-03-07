package com.health.hl.util;

import android.util.Log;

public class BraceletDataUtil {
	private static final String TAG="BraceletDataUtil";
	/**
	 * 发送获取电量
	 * @return
	 */
	public static byte[] sendGetPower(){
		
		return new byte[]{0x68, 0x03, 0x00,0x00, 0x6B, 0x16};
	}
	/**
	 * 打开心率测试
	 * @return
	 */
	public static byte[] openHeartRate(){
		//68 06 01 00 01 70 16
		return new byte[]{0x68, 0x06, 0x01,0x00,0x01,0x70, 0x16};
	}
	/**
	 * 获取实时心率
	 * @return
	 */
	public static byte[] getHeartRate(){
		//68 06 01 00 00 6f 16
		return new byte[]{0x68, 0x06, 0x01,0x00,0x00,0x6f, 0x16};
	}
	/**
	 * 关闭心率测试
	 * @return
	 */
	public static byte[] closeHeartRate(){
		//68 06 01 00 02 71 16
		return new byte[]{0x68, 0x06, 0x01,0x00,0x02,0x71, 0x16};
	}
	/**
	 * 打开疲劳测试
	 * @return
	 */
	public static byte[] openFatigue(){
		//68 0a 01 00 01 74 16
		return new byte[]{0x68, 0x0a, 0x01,0x00,0x01,0x74, 0x16};
	}
	/**
	 * 关闭疲劳测试
	 * @return
	 */
	public static byte[] closeFatigue(){
		//68 0a 01 00 00 74 16
		return new byte[]{0x68, 0x0a, 0x01,0x00,0x00,0x74, 0x16};
	}
	/**
	 * 获取实时数据
	 * @return
	 */
	public static byte[] getRealData(){
		//{0x68,0x06,0x01,0x00,0x00,0x6f,0x16};
		return new byte[]{0x68,0x06,0x01,0x00,0x00,0x6f,0x16};
	}
	
	
	/**
	 * 解析手环数据
	 * @param data
	 * @return
	 */
	public static String convertToString(byte[] data){
		StringBuilder response = new StringBuilder("");
		if(data!=null)
		for (int i = 0; i < data.length; i++) {
//			Log.i(TAG, "返回数据=" + data[i]);
			int v = data[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				response.append(0);
			}
			response.append(hv);
		}
		Log.i(TAG, "返回数据=" + response.toString());
		return response.toString();
	}
	/**
	 * 计算检验码
	 * @param data
	 * @return
	 */
	public static byte getCheckByte(byte[] data){
		byte b=0x00;
		for (int i = 0; i < data.length; i++) {
			b+=data[i];
		}
		return (byte)(b%256);
	}
}
