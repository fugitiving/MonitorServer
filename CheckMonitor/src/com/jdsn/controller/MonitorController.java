/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		MonitorController1.java
 * 作者		高科
 * 编写日期	2019-04-24
 */
package com.jdsn.controller;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.quartz.SchedulerException;

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

public class MonitorController {
	//private static final Logger logger = Logger.getLogger(MonitorController.class);
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		 try {
			MainScheduler mainScheduler = new MainScheduler();
			mainScheduler.schedulerJob();
			
			//启动时发送一个邮件
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
			LogWrite.info(content);
			try {
				sendToEmailServiceImpl.sendmail(title, from, sendTo, content, null, "text/html;charset=gb2312");
			} catch (Exception ex) {
				LogWrite.error(ex);
			}
		} catch (SchedulerException e) {
			LogWrite.error("出现定时调度异常：" + e.toString());
		}
	}
}
