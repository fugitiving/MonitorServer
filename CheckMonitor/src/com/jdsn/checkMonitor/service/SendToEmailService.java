/*
* 模块编号 
* 功能描述 
* 文件名 
* 作者 
* 编写日期 
*/
package com.jdsn.checkMonitor.service;

/**
* （功能描述） 
* 
* @version （版本号）
* @author（作者） 
*/
public interface SendToEmailService {
	public void sendmail(String subject, String from, String[] to, String text, String[] filenames,
			String mimeType) throws Exception  ;
}
