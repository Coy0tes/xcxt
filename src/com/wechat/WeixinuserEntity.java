package com.wechat;

import javax.persistence.Entity;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 微信用户列表
 * @author zhangdaihao
 * @date 2016-05-25 20:21:13
 * @version V1.0   
 *
 */
@Entity
@Table(name = "dm_weixin_user", schema = "")
@SuppressWarnings("serial")
public class WeixinuserEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**酒店*/
	private java.lang.String departid;
	/**昵称*/
	private java.lang.String nickname;
	/**性别*/
	private java.lang.String sex;
	/**省*/
	private java.lang.String province;
	/**市*/
	private java.lang.String city;
	/**OpenId*/
	private java.lang.String openid;
	
	private String subscribeTime;

	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public java.lang.String getDepartid() {
		return departid;
	}

	public void setDepartid(java.lang.String departid) {
		this.departid = departid;
	}

	public java.lang.String getNickname() {
		return nickname;
	}

	public void setNickname(java.lang.String nickname) {
		this.nickname = nickname;
	}

	public java.lang.String getSex() {
		return sex;
	}

	public void setSex(java.lang.String sex) {
		this.sex = sex;
	}

	public java.lang.String getProvince() {
		return province;
	}

	public void setProvince(java.lang.String province) {
		this.province = province;
	}

	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public java.lang.String getOpenid() {
		return openid;
	}

	public void setOpenid(java.lang.String openid) {
		this.openid = openid;
	}

	public String getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	
	
}
