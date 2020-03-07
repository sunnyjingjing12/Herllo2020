/**  
* @Title:  PhysicianUserCorrelation.java
* @Package com.health.hl.bean
* @Description: 
* @author jh
* @date  2018年8月28日 上午9:26:21
* @version V1.0  
* Update Logs:
* ****************************************************
* Name:
* Date:
* Description:
******************************************************
*/
package com.health.hl.bean;

import java.io.Serializable;

/**
 * @ClassName: PhysicianUserCorrelation
 * @Description:医生用户关联表
 * @author jh
 * @version V1.0  
 * @date 2018年8月28日 上午9:26:21
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class PhysicianUserCorrelation implements Serializable{
	
	private  Integer  id ;  //主键
	private int uId;  //用户id
	private int dId;  //医生id
	/**
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id 要设置的 id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return uId
	 */
	public int getuId() {
		return uId;
	}
	/**
	 * @param uId 要设置的 uId
	 */
	public void setuId(int uId) {
		this.uId = uId;
	}
	/**
	 * @return dId
	 */
	public int getdId() {
		return dId;
	}
	/**
	 * @param dId 要设置的 dId
	 */
	public void setdId(int dId) {
		this.dId = dId;
	}
	
	
	
	

}
