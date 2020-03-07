package com.health.hl.adapter;

import android.widget.ImageView;

public class Goods {
	 	private String rank;  
	    private String userName;  
	    private String score;  
	    private String maxPower;  
	    private String levelI;  
	    private String levelII;  
//	    private String phone;  
//	    private String maxPower; 
//	    private String muscleI; 
//	    private String muscleII;
//	    private String trainPlan;
//	    private String operation;
	      
	    public Goods() {  
	        super();  
	    }  
	  
	    public Goods(String rank, String userName, String score, String maxPower,  
	    		String levelI,String levelII) {  
	        super();  
	        this.rank = rank;  
	        this.userName = userName;  
	        this.score = score;  
	        this.maxPower = maxPower;  
	        this.levelI = levelI;  
	        this.levelII = levelII;  
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
	  
	    public String getScore() {  
	        return score;  
	    }  
	  
	    public void setSex(String score) {  
	        this.score = score;  
	    }  
	    public String getMaxPower() {  
	        return maxPower;  
	    }  
	  
	    public void setMaxPower(String maxPower) {  
	        this.maxPower = maxPower;  
	    } 
	    public String getLevelI() {  
	        return levelI;  
	    }  
	  
	    public void setMuscleI(String levelI) {  
	        this.levelI = levelI;  
	    } 
	    public String getLevelII() {  
	        return levelII;  
	    }  
	  
	    public void setMuscleII(String levelII) {  
	        this.levelII = levelII;  
	    } 	    	
}
