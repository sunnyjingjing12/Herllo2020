package com.health.hl.models;

import java.util.List;
/**
 * 血压计info类
 * @author yangqingcheng
 *
 */
public class AnfeiYiyuInfo {
	public String code;
	public String message;	
	public boolean response;//	true
	public List<YiyuData> data;
	public class YiyuData{
		public long date;//	1466006400000
		public int id;//	9
		public float depressivestate;//	2.0
		public int userId;//	90
	}
}
