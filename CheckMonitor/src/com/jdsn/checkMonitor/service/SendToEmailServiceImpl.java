/*
* 模块编号 
* 功能描述 
* 文件名 
* 作者 
* 编写日期 
*/
package com.jdsn.checkMonitor.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.jdsn.entity.Email_Autherticatorbean;
import com.jdsn.log.LogWrite;
import com.jdsn.util.Constant;
import com.jdsn.util.ReadEmailJson;

/**
* （功能描述） 
* 
* @version （版本号）
* @author（作者） 
*/
public class SendToEmailServiceImpl implements SendToEmailService{
	
	@SuppressWarnings("static-access")
	public void sendmail(String subject, String from, String[] to, String text, String[] filenames,
			String mimeType) throws Exception {
		// ResourceBundle mailProps = ResourceBundle.getBundle("mail");
		// 可以从配置文件读取相应的参数
		Properties props = new Properties();

		String smtp = null; // 设置发送邮件所用到的smtp
		
		switch (ReadEmailJson.SMTP) {
		case "163":
			smtp = Constant.SMTP_163; 
			break;
		case "qq":
			smtp = Constant.SMTP_QQ; 
			break;
		case "sohu":
			smtp = Constant.SMTP_SOHU; 
			break;
		case "hotmail":
			smtp = Constant.SMTP_HOTMAIL; 
			break;
		case "移动":
			smtp = Constant.SMTP_YD; 
			break;
		default:
			smtp = Constant.SMTP_QQ;
			break;
		}
		
		String servername = ReadEmailJson.SERVICE_NAME;
		String serverpaswd = ReadEmailJson.SERVICE_PASWD;

		javax.mail.Session mailSession; // 邮件会话对象
		javax.mail.internet.MimeMessage mimeMsg; // MIME邮件对象

		props = java.lang.System.getProperties(); // 获得系统属性对象
		props.put(Constant.MAIL_SMTP_HOST, smtp); // 设置SMTP主机
		props.put(Constant.MAIL_SMTP_AUTH, "true"); // 是否到服务器用户名和密码验证
		// 到服务器验证发送的用户名和密码是否正确
		Email_Autherticatorbean myEmailAuther = new Email_Autherticatorbean(servername, serverpaswd);
		// 设置邮件会话
		mailSession = javax.mail.Session.getInstance(props, (Authenticator) myEmailAuther);

		// 设置debug打印信息
//		mailSession.setDebug(true);

		// 设置传输协议
		javax.mail.Transport transport = mailSession.getTransport("smtp");
		// 设置from、to等信息
		mimeMsg = new javax.mail.internet.MimeMessage(mailSession);
		if (!from.isEmpty()) {

			InternetAddress sentFrom = new InternetAddress(from);
			mimeMsg.setFrom(sentFrom); // 设置发送人地址
		}

		InternetAddress[] sendTo = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			LogWrite.info("发送到:" + to[i]);
			sendTo[i] = new InternetAddress(to[i]);
		}

		mimeMsg.setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO, sendTo);
		mimeMsg.setSubject(subject, "gb2312");
		//解决邮件附件发送的名称乱码问题
		System.setProperty("mail.mime.splitlongparameters","false");
		MimeBodyPart messageBodyPart1 = new MimeBodyPart();
		// messageBodyPart.setText(UnicodeToChinese(text));
		messageBodyPart1.setContent(text, mimeType);

		Multipart multipart = new MimeMultipart();// 附件传输格式
		multipart.addBodyPart(messageBodyPart1);

		mimeMsg.setContent(multipart);
		// 设置信件头的发送日期
		mimeMsg.setSentDate(new Date());
		mimeMsg.saveChanges();
		// 发送邮件
		transport.send(mimeMsg);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println("发送时间：" + sdf.format(System.currentTimeMillis()));
		transport.close();
	}
}
