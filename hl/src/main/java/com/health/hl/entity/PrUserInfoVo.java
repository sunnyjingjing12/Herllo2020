/**  
* @Title:  UserInfoVo.java
* @Package com.health.hl.entity
* @Description: 
* @author jh
* @date  2018年8月28日 下午3:57:36
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
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @ClassName: UserInfoVo
 * @Description:
 * @author jh
 * @version V1.0  
 * @date 2018年8月28日 下午3:57:36
 * Update Logs:
 * ****************************************************
 * Name:
 * Date:
 * Description:[{"aid":430,"age":25,"birthday":"1993-07-14","caseName":"G","height":171.0,"id":429,"maxPower":21.0,"oneMuscle":2,"phone":"17688561260","twoMuscle":2,"userName":"j","userSex":"0","weight":60.0}]
 ******************************************************
 */
public class PrUserInfoVo implements Serializable {

	
    private Integer id ;
	
	private Integer aid ;
	
	private String userName;
	
	private String userSex;
	
	private BigDecimal height;
	
    private BigDecimal weight;
    
    private Date birthday;
    
    private int age;
    
    private String phone;
    
    private Integer oneMuscle;
    
    private Integer twoMuscle;
    
    private BigDecimal maxPower;
    
    private String caseName;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	public Integer getAid() {
		return aid;
	}
	
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public BigDecimal getHeight() {
		return height;
	}
	public void setHeight(BigDecimal height) {
		this.height = height;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getOneMuscle() {
		return oneMuscle;
	}
	public void setOneMuscle(Integer oneMuscle) {
		this.oneMuscle = oneMuscle;
	}
	public Integer getTwoMuscle() {
		return twoMuscle;
	}
	public void setTwoMuscle(Integer twoMuscle) {
		this.twoMuscle = twoMuscle;
	}
	public BigDecimal getMaxPower() {
		return maxPower;
	}
	public void setMaxPower(BigDecimal maxPower) {
		this.maxPower = maxPower;
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
 
}
