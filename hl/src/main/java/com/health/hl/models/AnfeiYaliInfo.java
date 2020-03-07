package com.health.hl.models;

import java.util.List;
/**
 * 血压计info类
 * @author yangqingcheng
 *
 */
public class AnfeiYaliInfo {
	public String code;
	public String message;	
	public boolean response;//	true
	public List<YaliData> data;
	public class YaliData{
		public long date;//	1466006400000
		public int id;//	9
		public float mentalStress;//	69.0
		public float physicalStress;//	64.0
		public int userId;//	90
	}
}
