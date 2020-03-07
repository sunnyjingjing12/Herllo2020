package com.health.hl.adapter;

import android.widget.ImageView;

public class GoodsProgress {
	 	private String rank;  
	    private String userName;  
	    private String progressNum;  
	    private String maxPowerVary;  
//	    private String phone;  
//	    private String maxPower; 
//	    private String muscleI; 
//	    private String muscleII;
//	    private String trainPlan;
//	    private String operation;
	      
	    public GoodsProgress() {  
	        super();  
	    }  
	  
	    public GoodsProgress(String rank, String userName, String progressNum, String maxPowerVary) {  
	        super();  
	        this.rank = rank;  
	        this.userName = userName;  
	        this.progressNum = progressNum;  
	        this.maxPowerVary = maxPowerVary;  
	    }  
	  
	    public String getRank() {  
	        return rank;  
	    }  
	  
	    public void setRank(String rank) {  
	        this.rank = rank;  
	    }  
	  
	    public String getUserName() {  
	        return userName;  
	    }  
	  
	    public void setUserName(String userName) {  
	        this.userName = userName;  
	    }  
	  
	    public String getProgressNum() {  
	        return progressNum;  
	    }  
	  
	    public void setSex(String progressNum) {  
	        this.progressNum = progressNum;  
	    }  
	    public String getMaxPowerVary() {  
	        return maxPowerVary;  
	    }  
	  
	    public void setMaxPower(String maxPowerVary) {  
	        this.maxPowerVary = maxPowerVary;  
	    }    	
}
