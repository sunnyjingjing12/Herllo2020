/**  
* @Title:  PrintUser.java
* @Package com.health.hl.entity
* @Description: 
* @author jh
* @date  2018年9月6日 上午10:13:33
* @version V1.0  
* Update Logs:
* ****************************************************
* Name:
* Date:
* Description:
******************************************************
*/
package com.health.hl.entity;

/**
 * @ClassName: PrintUser
 * @Description:
 * @author jh
 * @version V1.0  
 * @date 2018年9月6日 上午10:13:33
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class PrintUser {

	private int id;
	private String hosptail;
	private String phone;
	
	public PrintUser(int id, String hosptail, String phone) {
		super();
		this.id = id;
		this.hosptail = hosptail;
		this.phone = phone;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id 要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return hosptail
	 */
	public String getHosptail() {
		return hosptail;
	}

	/**
	 * @param hosptail 要设置的 hosptail
	 */
	public void setHosptail(String hosptail) {
		this.hosptail = hosptail;
	}

	/**
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone 要设置的 phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
