package com.health.hl.models;

import java.util.List;

public class BalanceInfo {
	public String code;
	public String message;	
	public boolean response;//	true
	public List<MyData> data;
	public class MyData{
		public String account; // 	333
		public float bmi; // 	29.4
		public double bone; // 	0.0
		public String devName; // 	zfc
		public double fat; // 	0.0
		public int id; // 	1227
		public double kcal; // 	0.0
		public double muscle; // 	0.0
		public String source; // 	hs
		public int userId; // 	162
		public String userName; // 	333
		public String version; // 	1
		public double viscerafat; // 	0.0
		public double water; // 	0.0
		public double weight; // 	80.3
		DateS date = new DateS();		
		public DateS getDate() {
			return date;
		}
		public void setDate(DateS date) {
			this.date = date;
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
