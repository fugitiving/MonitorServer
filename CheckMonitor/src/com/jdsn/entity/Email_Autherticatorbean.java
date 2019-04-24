/*
* 模块编号 
* 功能描述 
* 文件名 
* 作者 
* 编写日期 
*/
package com.jdsn.entity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
* （功能描述） 
* 
* @version （版本号）
* @author（作者） 
*/
public class Email_Autherticatorbean extends Authenticator{
	private String m_username = null;  
    private String m_userpass = null;  
  
    public void setUsername(String username) {  
        m_username = username;  
    }  
  
    public void setUserpass(String userpass) {  
        m_userpass = userpass;  
    }  
  
    public Email_Autherticatorbean(String username, String userpass) {  
        super();  
        setUsername(username);  
        setUserpass(userpass);  
  
    }  
  
    public PasswordAuthentication getPasswordAuthentication() {  
  
        return new PasswordAuthentication(m_username, m_userpass);  
    }  
}
