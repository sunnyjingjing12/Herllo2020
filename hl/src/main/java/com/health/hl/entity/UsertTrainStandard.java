/**  
* @Title:  UserTestStandard.java
* @Package com.health.hl.entity
* @Description: 
* @author jh
* @date  2018年8月31日 上午9:52:26
* @version V1.0  
* Update Logs:
* ****************************************************
* Name:
* Date:
* Description:
******************************************************
*/
package com.health.hl.entity;

import java.io.Serializable;

/**
 * @ClassName: UserTestStandard
 * @Description:训练习惯
 * @author jh
 * @version V1.0  
 * @date 2018年8月31日 上午9:52:26
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class UsertTrainStandard implements Serializable{
	
	private int trainTime; //时间
	private int trainLevel; //等级
	private int trainMode; //模式  1：波形   2：游戏
	/**
	 * @return trainTime
	 */
	public int getTrainTime() {
		return trainTime;
	}
	/**
	 * @param trainTime 要设置的 trainTime
	 */
	public void setTrainTime(int trainTime) {
		this.trainTime = trainTime;
	}
	/**
	 * @return trainLevel
	 */
	public int getTrainLevel() {
		return trainLevel;
	}
	/**
	 * @param trainLevel 要设置的 trainLevel
	 */
	public void setTrainLevel(int trainLevel) {
		this.trainLevel = trainLevel;
	}
	/**
	 * @return trainMode
	 */
	public int getTrainMode() {
		return trainMode;
	}
	/**
	 * @param trainMode 要设置的 trainMode
	 */
	public void setTrainMode(int trainMode) {
		this.trainMode = trainMode;
	}
	
	

}
