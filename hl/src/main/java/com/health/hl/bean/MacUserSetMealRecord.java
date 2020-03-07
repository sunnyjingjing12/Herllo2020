/**  
* @Title:  MacUserSetMealRecord.java
* @Package com.health.hl.bean
* @Description: 
* @author jh
* @date  2019年1月14日 下午5:03:17
* @version V1.0  
* Update Logs:
* ****************************************************
* Name:
* Date:
* Description:
******************************************************
*/
package com.health.hl.bean;

/**
 * @ClassName: MacUserSetMealRecord
 * @Description:用户套餐使用记录
 * @author jh
 * @version V1.0  
 * @date 2019年1月14日 下午5:03:17
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class MacUserSetMealRecord {

	private String account; //账号
	private String recordDate; //记录时间
	private int flag;  // 0 ：测试   1：训练
	private int countNumber;  //次数
	/**
	 * @return account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account 要设置的 account
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return recordDate
	 */
	public String getRecordDate() {
		return recordDate;
	}
	/**
	 * @param recordDate 要设置的 recordDate
	 */
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	/**
	 * @return flag
	 */
	public int getFlag() {
		return flag;
	}
	/**
	 * @param flag 要设置的 flag
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	/**
	 * @return countNumber
	 */
	public int getCountNumber() {
		return countNumber;
	}
	/**
	 * @param countNumber 要设置的 countNumber
	 */
	public void setCountNumber(int countNumber) {
		this.countNumber = countNumber;
	}
	
	
	
	 
}
