/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		Register.java
 * 作者		高科
 * 编写日期	2019-03-21
 */
package com.jdsn.entity;

/*
 * TODO
 * 
 * @version 1.0.0.0
 * @author 高科
 */

public class Register {
	//#报文头标识
	private String flag;
	//#版本号
	private int version;
	//#报文长度
	private int megLen;
	//#挑战字
	private int challange;
	//#帧类型
	private int registerType;
	//#服务器名长度
	private int serverNameLen;
	//#服务器名
	private String serverName;
	//#用户名长度
	private int userNameLen;
	//#用户名
	private String userName;
	//#密码长度
	private int passwordLen;
	//#密码
	private String password;
	//#本地版本号长度
	private int ocalVersionLen;
	//#本地版本号
	private String localVersion;
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getMegLen() {
		return megLen;
	}
	public void setMegLen(int megLen) {
		this.megLen = megLen;
	}
	public int getChallange() {
		return challange;
	}
	public void setChallange(int challange) {
		this.challange = challange;
	}
	public int getRegisterType() {
		return registerType;
	}
	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}
	public int getServerNameLen() {
		return serverNameLen;
	}
	public void setServerNameLen(int serverNameLen) {
		this.serverNameLen = serverNameLen;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getUserNameLen() {
		return userNameLen;
	}
	public void setUserNameLen(int userNameLen) {
		this.userNameLen = userNameLen;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getPasswordLen() {
		return passwordLen;
	}
	public void setPasswordLen(int passwordLen) {
		this.passwordLen = passwordLen;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getOcalVersionLen() {
		return ocalVersionLen;
	}
	public void setOcalVersionLen(int ocalVersionLen) {
		this.ocalVersionLen = ocalVersionLen;
	}
	public String getLocalVersion() {
		return localVersion;
	}
	public void setLocalVersion(String localVersion) {
		this.localVersion = localVersion;
	}
}
