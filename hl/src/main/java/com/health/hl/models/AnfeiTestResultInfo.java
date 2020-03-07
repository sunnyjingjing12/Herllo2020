package com.health.hl.models;

import java.io.Serializable;
/**
 * 用于保存当前日期是否有测量结果的类
 * @author yangqingcheng
 *
 */
public class AnfeiTestResultInfo implements Serializable  {
	private boolean isTest = false; 
	private boolean isYaliTest = false; 
	private static AnfeiTestResultInfo userInfo;
	public static AnfeiTestResultInfo get(){
		if(userInfo==null){
			userInfo=new AnfeiTestResultInfo();
		}
		return userInfo;
	}
	private AnfeiTestResultInfo(){
		
	}
	public boolean getIsTest() {
		return isTest;
	}
	public void setIsTest(boolean isTest) {
		this.isTest = isTest;
	}
	public boolean isYaliTest() {
		return isYaliTest;
	}
	public void setYaliTest(boolean isYaliTest) {
		this.isYaliTest = isYaliTest;
	}



}
