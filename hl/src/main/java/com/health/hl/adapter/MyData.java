/**  
 * @Title:  MyData.java
 * @Package com.health.hl.adapter
 * @Description: 
 * @author jh
 * @date  2018年9月6日 上午9:52:21
 * @version V1.0  
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
package com.health.hl.adapter;

import com.alibaba.fastjson.JSON;
import com.health.hl.app.App;
import com.health.hl.entity.PrintUser;

import android.R.integer;
import android.webkit.JavascriptInterface;


/**
 * @ClassName: MyData
 * @Description:
 * @author jh
 * @version V1.0  
 * @date 2018年9月6日 上午9:52:21
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class MyData {
//	private App session ; 
//	private String data;
	private int userId;
	private String hospital;
	private String userPhone;
	
	public MyData(int userId,String hospital,String userPhone) {
		super();
//		this.session = session;
//		this.data = data;
		this.userId = userId;
		this.hospital = hospital;
		this.userPhone = userPhone;

	}
	/**
	 * 获取person字符串传Html
	 * @return
	 */
	@JavascriptInterface
	public String getData(){
		PrintUser printUser  = new PrintUser(userId, hospital,userPhone); 
		String jsonStr  = JSON.toJSONString(printUser);
		return jsonStr;
	}
}

