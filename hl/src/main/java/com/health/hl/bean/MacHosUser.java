/**  
* @Title:  MacHosUser.java
* @Package com.health.hl.bean
* @Description: 
* @author jh
* @date  2019年1月14日 下午4:59:28
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
 * @ClassName: MacHosUser
 * @Description:绑定Mac地址的医院用户信息
 * @author jh
 * @version V1.0  
 * @date 2019年1月14日 下午4:59:28
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class MacHosUser {

		private String mac ; //注册地址
		private String account ;  //账号
		private String registerDate; //注册时间
		
		/**
		 * @return mac
		 */
		public String getMac() {
			return mac;
		}
		/**
		 * @param mac 要设置的 mac
		 */
		public void setMac(String mac) {
			this.mac = mac;
		}
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
		 * @return registerDate
		 */
		public String getRegisterDate() {
			return registerDate;
		}
		/**
		 * @param registerDate 要设置的 registerDate
		 */
		public void setRegisterDate(String registerDate) {
			this.registerDate = registerDate;
		}
	
		
		
		
}
