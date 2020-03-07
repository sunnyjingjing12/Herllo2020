package com.health.hl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 
 *类描述：用户基本信息表
 *@author: 豪
 *@date： 日期：2017-9-6 时间：下午5:17:45
 *@version 1.0
 */
public class PrUserInfo implements Serializable{
    private Integer id;

    private Integer aId;

    private String userName;

    private String userSex;

    private BigDecimal height;

    private BigDecimal weight;
    
    @JSONField(format="yyyy-MM-dd")
    private Date birthday;

    private String realName;

    private String phone;

    private String email;

    private String headImg;

    private String currentState;
    
    //用户来源  来源为：heeytech  applet
    private String userSources;
    
    //只针对返回的时候存取
    private Integer age;
    
    

    public PrUserInfo() {
	
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

 
	

	
	public Integer getaId() {
		return aId;
	}

	
	public void setaId(Integer aId) {
		this.aId = aId;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex == null ? null : userSex.trim();
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState == null ? null : currentState.trim();
    }
    
    
    

	public String getUserSources() {
		return userSources;
	}

	public void setUserSources(String userSources) {
		this.userSources = userSources == null ? null : userSources.trim();
	}


	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}

	/* (非 Javadoc)
	* <p>Title: toString</p>
	* <p>Description: </p>
	* @return
	* @see java.lang.Object#toString()
	*/
	@Override
	public String toString() {
		return "PrUserInfo [id=" + id + ", aId=" + aId + ", userName="
				+ userName + ", userSex=" + userSex + ", height=" + height
				+ ", weight=" + weight + ", birthday=" + birthday
				+ ", realName=" + realName + ", phone=" + phone + ", email="
				+ email + ", headImg=" + headImg + ", currentState="
				+ currentState + ", userSources=" + userSources + ", age="
				+ age + "]";
	}

	
	
    
    
}