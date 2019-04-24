/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		CheckLogin.java
 * 作者		高科
 * 编写日期	2019-03-20
 */
package com.jdsn.controller;

import java.io.IOException;
import java.util.Arrays;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONArray;
import com.jdsn.checkMonitor.service.SendToEmailServiceImpl;
import com.jdsn.log.LogWrite;
import com.jdsn.util.ReadEmailJson;

/*
 * TODO
 * 
 * @version 1.0.0.0
 * @author 高科
 */

public class CheckLogin implements Job{
	
	/* (non-Javadoc)
	 * 执行登录
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		//发送注册帧
		LoginRegister lr = new LoginRegister();
		byte[] recv = lr.Login();
		
		if(recv != null) {
			if (recv[18] == 0) {
				
				//模拟web端登录
				LoginWeb lw = new LoginWeb();
				try {
					int statusCode = lw.logonWeb();
					if(statusCode != 200) {
						SendToEmailServiceImpl sendToEmailServiceImpl = new SendToEmailServiceImpl();
						String title = ReadEmailJson.TITLE;// 所发送邮件的标题
						String from = ReadEmailJson.FROM;// 从那里发送
						JSONArray jsonArray = ReadEmailJson.RECEIVES;
						String[] sendTo = new String[jsonArray.size()];// 发送到那里
						
						for (int i = 0; i < jsonArray.size(); i++) {
							sendTo[i] = (String) jsonArray.get(i);
						}
						// 邮件的文本内容，可以包含html标记则显示为html页面
						String content = ReadEmailJson.CONTENT;
						content = "Web网页无法访问，请检查！";
						LogWrite.info(content);
						
						try {
							sendToEmailServiceImpl.sendmail(title, from, sendTo, content, null, "text/html;charset=gb2312");
						} catch (Exception ex) {
							LogWrite.error(ex);
						}
					}
				} catch (IOException e) {
					LogWrite.error(e);
				}
			}else {
				SendToEmailServiceImpl sendToEmailServiceImpl = new SendToEmailServiceImpl();
				String title = ReadEmailJson.TITLE;// 所发送邮件的标题
				String from = ReadEmailJson.FROM;// 从那里发送
				JSONArray jsonArray = ReadEmailJson.RECEIVES;
				String[] sendTo = new String[jsonArray.size()];// 发送到那里
				
				for (int i = 0; i < jsonArray.size(); i++) {
					sendTo[i] = (String) jsonArray.get(i);
				}
				// 邮件的文本内容，可以包含html标记则显示为html页面
				String content = ReadEmailJson.CONTENT;
				content = "1、注册帧的应答帧为：" + Arrays.toString(recv) + "\r\n" + "2、接收应答帧失败，服务器无法登录，请检查服务器！";
				LogWrite.info(content);
				
				try {
					sendToEmailServiceImpl.sendmail(title, from, sendTo, content, null, "text/html;charset=gb2312");
				} catch (Exception ex) {
					LogWrite.error(ex);
				}
			}
		}else {
			//无法登录，发送邮件
			SendToEmailServiceImpl sendToEmailServiceImpl = new SendToEmailServiceImpl();
			String title = ReadEmailJson.TITLE;// 所发送邮件的标题
			String from = ReadEmailJson.FROM;// 从那里发送
			JSONArray jsonArray = ReadEmailJson.RECEIVES;
			String[] sendTo = new String[jsonArray.size()];// 发送到那里
			
			for (int i = 0; i < jsonArray.size(); i++) {
				sendTo[i] = (String) jsonArray.get(i);
			}
			// 邮件的文本内容，可以包含html标记则显示为html页面
			String content = ReadEmailJson.CONTENT;
			content = "1、服务器无法登录，请检查服务器！";
			LogWrite.info(content);
			
			try {
				sendToEmailServiceImpl.sendmail(title, from, sendTo, content, null, "text/html;charset=gb2312");
			} catch (Exception ex) {
				LogWrite.error(ex);
			}
		}
		
	}
}