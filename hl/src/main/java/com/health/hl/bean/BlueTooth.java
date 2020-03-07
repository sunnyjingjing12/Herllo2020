/**  
* @Title:  BlueTooth.java
* @Package com.health.hl.bean
* @Description: 
* @author jh
* @date  2018年9月30日 下午3:43:48
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
 * @ClassName: BlueTooth
 * @Description:
 * @author jh
 * @version V1.0  
 * @date 2018年9月30日 下午3:43:48
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class BlueTooth {

	private String pcmac;  //pc设备mac地址
	private String name; //蓝牙名称
	private String mac; //蓝牙mac地址
	private int flag; //是否第一次连接  0：第一次   1：非第一次  重置时使用 置零

	/**
	 * @return pcmac
	 */
	public String getPcmac() {
		return pcmac;
	}
	/**
	 * @param pcmac 要设置的 pcmac
	 */
	public void setPcmac(String pcmac) {
		this.pcmac = pcmac;
	}
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name 要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}
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
	
	
}
