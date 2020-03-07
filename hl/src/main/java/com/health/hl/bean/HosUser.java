/**  
* @Title:  HosUser.java
* @Package com.health.hl.bean
* @Description: 
* @author jh
* @date  2018年8月22日 下午5:30:47
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
 * @ClassName: HosUser
 * @Description:医院用户
 * @author jh
 * @version V1.0  
 * @date 2018年8月22日 下午5:30:47
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 ******************************************************
 */
public class HosUser {

	private  Integer  id ;  //主键 医生id
	private String name ; //用户名
	private String  phone; //手机号
	private String password; //密码
	private String hospital; //所属医院
	private String department; //医院科室
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
	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password 要设置的 password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return hospital
	 */
	public String getHospital() {
		return hospital;
	}
	/**
	 * @param hospital 要设置的 hospital
	 */
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	/**
	 * @return department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department 要设置的 department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/* (非 Javadoc)
	* <p>Title: toString</p>
	* <p>Description: </p>
	* @return
	* @see java.lang.Object#toString()
	*/
	@Override
	public String toString() {
		return "HosUser [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", password=" + password + ", hospital=" + hospital
				+ ", department=" + department + "]";
	}
	
	
	
	
	
	
}
