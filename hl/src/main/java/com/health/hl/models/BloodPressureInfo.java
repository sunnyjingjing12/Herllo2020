package com.health.hl.models;

import java.util.List;
/**
 * 血压计info类
 * @author yangqingcheng
 *
 */
public class BloodPressureInfo {
	public String code;
	public String message;	
	public boolean response;//	true
	public List<MyData> data;
	public class MyData{
		public String account; // 	333
		public float dia; //	90.0
		public int id; //	1
		public float pul; //	70.0
		public float sys; //	110.0
		public String userName; //	zhou
		DateS createTime = new DateS();		
		public DateS getDate() {
			return createTime;
		}
		public void setDate(DateS date) {
			this.createTime = date;
		}
	}
	public class DateS{
		public int date;//		15
		public int day;//		0
		public int hours;//		12
		public int minutes;//		0
		public int month;//		10
		public int nanos;//		0
		public int seconds;//		0
		private long time;//		1447560000000
		public int timezoneOffset;//		-480
		public int year;//		115	
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
	}
}
