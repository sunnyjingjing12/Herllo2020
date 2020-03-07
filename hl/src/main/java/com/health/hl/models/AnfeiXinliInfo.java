package com.health.hl.models;

import java.io.Serializable;

public class AnfeiXinliInfo implements Serializable  {
	private float xinli; 
	
	private static AnfeiXinliInfo userInfo;
	public static AnfeiXinliInfo get(){
		if(userInfo==null){
			userInfo=new AnfeiXinliInfo();
		}
		return userInfo;
	}
	private AnfeiXinliInfo(){
		
	}
	public float getXinli() {
		return xinli;
	}
	public void setXinli(float xinli) {
		this.xinli = xinli;
	}


}
