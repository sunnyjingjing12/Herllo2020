package com.health.hl.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.health.hl.models.SettingInfo;


public class ParseUtil {
	private DecimalFormat dfc = new DecimalFormat("#.#");
	
	private String time;
	private String tizhong;
	private String guge;
	private String zhifang;
	private String jirou;
	private String shuifen;
	private String neizang;
	private String BMR;
	private float bmi;
	private DecimalFormat fnum; 
	private static ParseUtil state;

    private ParseUtil() {
    	fnum = new DecimalFormat("##0.0");
//        reload();
    }

    public static ParseUtil get() {

        if (state == null) {
            state = new ParseUtil();
            
        }

        return state;
    }
	
	

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getBmi() {
		return bmi;
	}

	public void setBmi(float bmi) {
		this.bmi = bmi;
	}

	public String getTizhong() {
		return tizhong;
	}

	public void setTizhong(String tizhong) {
		this.tizhong = tizhong;
	}

	public String getGuge() {
		return guge;
	}

	public void setGuge(String guge) {
		this.guge = guge;
	}

	public String getZhifang() {
		return zhifang;
	}

	public void setZhifang(String zhifang) {
		this.zhifang = zhifang;
	}

	public String getJirou() {
		return jirou;
	}

	public void setJirou(String jirou) {
		this.jirou = jirou;
	}

	public String getShuifen() {
		return shuifen;
	}

	public void setShuifen(String shuifen) {
		this.shuifen = shuifen;
	}

	public String getNeizang() {
		return neizang;
	}

	public void setNeizang(String neizang) {
		this.neizang = neizang;
	}

	public String getBMR() {
		return BMR;
	}

	public void setBMR(String bMR) {
		BMR = bMR;
	}

	public void parse(byte[] data) {
//		byte[] data = hexStringToBytes(content);
		//位与：第一个操作数的的第n位于第二个操作数的第n位如果都是1，那么结果的第n为也为1，否则为0
		// 设别类型
		int v = data[0] & 0xFF;
		String typeRec = "脂肪秤";
		if (v == 0xcf) {
			typeRec = "脂肪秤";
		} else if (v == 0xce) {
			typeRec = "人体秤";
		} else if (v == 0xcb) {
			typeRec = "婴儿秤";
		} else if (v == 0xca) {
			typeRec = "厨房秤";
		}

		// 等级和组号
		int level = (data[1] >> 4) & 0xf;
		int group = data[1] & 0xf;

		String levelRec = "普通";
		if (level == 0) {
			levelRec = "普通";
		} else if (level == 1) {
			levelRec = "业余";
		} else if (level == 2) {
			levelRec = "专业";
		}

		// 性别
		int sex = (data[2] >> 7) & 0x1;
		String secRec = "";
		if (sex == 1) {
			secRec = "男";
		} else {
			secRec = "女";
		}
		// 年龄
		int age = data[2] & 0x7f;

		// 身高
		int height = data[3] & 0xFF;

		// 体重
		int weight = (data[4] << 8) | (data[5] & 0xff);
		float scale = (float) 0.1;
		if (v == 0xcf) {
			scale = (float) 0.1;
		} else if (v == 0xce) {
			scale = (float) 0.1;
		} else if (v == 0xcb) {
			scale = (float) 0.01;
		} else if (v == 0xca) {
			scale = (float) 0.001;
		}

		float weightRec = scale * weight;

		if (weightRec < 0) {
			weightRec *= -1;
		}

		// 脂肪
		int zhifang = (data[6] << 8) | (data[7] & 0xff);

		float zhifangRate = (float) (zhifang * 0.1);
		// 骨骼
		Log.i("测试", "骨骼字节="+data[8]);
		int guge = data[8] & 0xff;

		float gugeRate = (float)(guge * 0.1);

		// 肌肉
		int jirou = (data[9] << 8) | (data[10] & 0xff);
		float jirouRate = (float) (jirou * 0.1);

		// 内脏脂肪
		int neizang = data[11] & 0xff;
		int neizanglevel = neizang * 1;

		// 水分
		int water = ((data[12] << 8) | (data[13] & 0xFF));
		float waterRate = (float) (water * 0.1);

		// 热量
		int hot = data[14] << 8 | (data[15] & 0xff);
		
		this.time=getCurrentTime();
		this.tizhong=dfc.format(weightRec < 0 ? -weightRec : weightRec);
		this.guge=dfc.format(gugeRate < 0 ? -gugeRate : gugeRate);
		this.zhifang=dfc.format(zhifangRate < 0 ? -zhifangRate : zhifangRate);
		this.jirou=dfc.format(jirouRate < 0 ? -jirouRate : jirouRate);
		this.shuifen=dfc.format(waterRate < 0 ? -waterRate : waterRate);
		this.neizang=dfc.format(neizanglevel < 0 ? -neizanglevel : neizanglevel);
		this.BMR=dfc.format(hot < 0 ? -hot : hot);
		float a=Float.parseFloat(tizhong)
				/ ((Float.parseFloat(SettingInfo.get().getHeight()) / 100) * (Float
						.parseFloat(SettingInfo.get().getHeight()) / 100));
		this.bmi=Float.parseFloat(fnum.format(a));
		String[] rec = new String[]{"体重：" + dfc.format(weightRec < 0 ? -weightRec : weightRec) + "Kg", "骨骼：" + dfc.format(gugeRate < 0 ? -gugeRate : gugeRate) + "%", "脂肪：" + dfc.format(zhifangRate < 0 ? -zhifangRate : zhifangRate) + "%",
				"肌肉：" + dfc.format(jirouRate < 0 ? -jirouRate : jirouRate) + "%", "水分：" + dfc.format(waterRate < 0 ? -waterRate : waterRate) + "%", "内脏脂肪：" + dfc.format(neizanglevel < 0 ? -neizanglevel : neizanglevel), "BMR:" + dfc.format(hot < 0 ? -hot : hot) + "kcal"};
		Log.i("ParseUtil", rec[0]+rec[1]+rec[2]+rec[3]+rec[4]+rec[5]+rec[6]);
//		return rec;
	}
	public void parseVScale(byte[] data) {
//		byte[] data = hexStringToBytes(content);
		//位与：第一个操作数的的第n位于第二个操作数的第n位如果都是1，那么结果的第n为也为1，否则为0
		// 设别类型

		// 性别
		int sex = (data[2] >> 7) & 0x1;
		String secRec = "";
		if (sex == 1) {
			secRec = "男";
		} else {
			secRec = "女";
		}

		// 体重
		int weight = (data[4] << 8) | (data[5] & 0xff);
		float scale = (float) 0.1;

		float weightRec = scale * weight;

		if (weightRec < 0) {
			weightRec *= -1;
		}

		// 脂肪
		int zhifang = (data[6] << 8) | (data[7] & 0xff);
		if (zhifang == -1) {
			zhifang = 0;
		}
		float zhifangRate = (float) (zhifang * 0.1);
		
		// 水分
		int water = ((data[8] << 8) | (data[9] & 0xFF));
		if (water == -1) {
			water = 0;
		}
		float waterRate = (float) (water * 0.1);
		
		// 骨骼
		Log.i("测试", "骨骼字节="+data[8]);
		int guge = (data[10] << 8) | (data[11] & 0xff);
		if (guge == -1) {
			guge = 0;
		}
		float gugeRate = (float)(guge * 0.1);

		// 肌肉
		int jirou = (data[12] << 8) | (data[13] & 0xff);
		if (jirou == -1) {
			jirou = 0;
		}
		float jirouRate = (float) (jirou * 0.1);

		// 内脏脂肪
		int neizang = data[14] & 0xff;
		if (neizang == 255) {
			neizang = 0;
		}
		int neizanglevel = neizang * 1;


		// 热量
		int hot = data[15] << 8 | (data[16] & 0xff);
		if (hot == -1) {
			hot = 0;
		}
		//BMI
		int bmi = data[17] << 8 | (data[18] & 0xff);
		if (bmi == -1) {
			bmi = 0;
		}
		float bmilevel = (float) (bmi * 0.1);
		
		this.time=getCurrentTime();
		this.tizhong=dfc.format(weightRec < 0 ? -weightRec : weightRec);
		this.guge=dfc.format(gugeRate < 0 ? -gugeRate : gugeRate);
		this.zhifang=dfc.format(zhifangRate < 0 ? -zhifangRate : zhifangRate);
		this.jirou=dfc.format(jirouRate < 0 ? -jirouRate : jirouRate);
		this.shuifen=dfc.format(waterRate < 0 ? -waterRate : waterRate);
		this.neizang=dfc.format(neizanglevel < 0 ? -neizanglevel : neizanglevel);
		this.BMR=dfc.format(hot < 0 ? -hot : hot);
		/*float a=Float.parseFloat(tizhong)
				/ ((Float.parseFloat(SettingInfo.get().getHeight()) / 100) * (Float
						.parseFloat(SettingInfo.get().getHeight()) / 100));
		this.bmi=Float.parseFloat(fnum.format(a));*/
		this.bmi=Float.parseFloat(fnum.format(bmilevel));
		String[] rec = new String[]{"体重：" + dfc.format(weightRec < 0 ? -weightRec : weightRec) + "Kg", "骨骼：" + dfc.format(gugeRate < 0 ? -gugeRate : gugeRate) + "%", "脂肪：" + dfc.format(zhifangRate < 0 ? -zhifangRate : zhifangRate) + "%",
				"肌肉：" + dfc.format(jirouRate < 0 ? -jirouRate : jirouRate) + "%", "水分：" + dfc.format(waterRate < 0 ? -waterRate : waterRate) + "%", "内脏脂肪：" + dfc.format(neizanglevel < 0 ? -neizanglevel : neizanglevel), "BMR:" + dfc.format(hot < 0 ? -hot : hot) + "kcal"};
		Log.i("ParseUtil", rec[0]+rec[1]+rec[2]+rec[3]+rec[4]+rec[5]+rec[6]);
//		return rec;
	}
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	private String getCurrentTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		String date=sdf.format(new Date());
		Log.i("当前时间", "当前时间="+date);
		return date;
	}
}
