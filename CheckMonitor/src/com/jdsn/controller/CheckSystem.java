/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		CheckLogin.java
 * 作者		高科
 * 编写日期	2019-03-20
 */
package com.jdsn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONArray;
import com.jdsn.checkMonitor.service.SendToEmailServiceImpl;
import com.jdsn.log.LogWrite;
import com.jdsn.util.ReadEmailJson;
import com.sun.management.OperatingSystemMXBean; 

/*
 * TODO
 * 
 * @version 1.0.0.0
 * @author 高科
 */

public class CheckSystem implements Job{
	
	/* 
	 * 执行登录
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		//如果登录成功，则获取系统信息，查看共享磁盘是否可访问、磁盘使用率、内存、cpu是否正常
		try {
			FileInputStream fis = new FileInputStream("src//conf\\SystemConfig.properties");
			Properties pro = new Properties();
			pro.load(new InputStreamReader(fis, "utf-8"));
			
			class MemTask implements Runnable {
		         public void run() {
		        	 //持续一分钟，上面已经调用过一次memeory()
		             int i = 0;
		             double MemUtilizationRate = 0;
		             int memTime = Integer.parseInt(pro.getProperty("memTime"));
		             while(i < memTime) {
		            	 try {
							MemUtilizationRate = memory();
							if(MemUtilizationRate > Double.parseDouble(pro.getProperty("memRate"))) {
								i = i + 5;
			            	 }else {
			            		 break;
			            	 }
							//间隔5秒调用一次
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							LogWrite.error(e);
						}
		             }
		             if(i == memTime) {
	            		 String str = "内存使用率为" + MemUtilizationRate + "，请查看！";
	            		 SendEmail(str); 
	            	 }
		         }
		     }
			
			class CpuTask implements Runnable {
		         public void run() {
		        	 //持续一分钟，上面已经调用过一次cpu()
		             int i = 0;
		             double CpuUtilizationRate = 0;
		             int cpuTime = Integer.parseInt(pro.getProperty("cpuTime"));
		             while(i < cpuTime) {
		            	 try {
							CpuUtilizationRate = cpu();
							if(CpuUtilizationRate > Double.parseDouble(pro.getProperty("cpuRate"))) {
								i = i + 5;
			            	 }else {
			            		 break;
			            	 }
							//间隔5秒调用一次
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							LogWrite.error(e);
						}
		             }
		             if(i == cpuTime) {
	            		 String str = "CPU使用率为" + CpuUtilizationRate + "，请查看！";
	            		 SendEmail(str); 
	            	 }
		         }
		     }
			//内存
			MemTask mem = new MemTask();
			mem.run();
			
			//共享磁盘是否可用：从配置文件中获取
			String disk = pro.getProperty("HardDisk");
			File f = new File(disk+":\\");
			if(!f.exists()) {
				String str = "无法获取共享磁盘" + disk + "，请查看！";
				SendEmail(str);
			}
			
			//磁盘使用率列表
			Map<Character, Double> disks = disk();
			String str = "";
			for (Map.Entry<Character, Double> entry : disks.entrySet()) {
				double DiskUtilizationRate = entry.getValue();
				if(DiskUtilizationRate > Double.parseDouble(pro.getProperty("hardDiskRate"))) {
					str += entry.getKey().toString() + "磁盘使用率为" + DiskUtilizationRate + "，";
				}
			}
			if(!str.equals("")) {
				str += "请查看！";
				SendEmail(str);
			}
			
			//cpu
			CpuTask cpu = new CpuTask();
			cpu.run();
			
		} catch (FileNotFoundException e) {
			LogWrite.error(e);
		} catch (UnsupportedEncodingException e) {
			LogWrite.error(e);
		} catch (IOException e) {
			LogWrite.error(e);
		}
	}

	public double cpu() {
		OperatingSystemMXBean osMxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    	return osMxBean.getSystemCpuLoad();
    }

	private void SendEmail(String str) {
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
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		content = format.format(new Date(System.currentTimeMillis())) +"：" + str;
		LogWrite.info(content);
		
		try {
			sendToEmailServiceImpl.sendmail(title, from, sendTo, content, null, "text/html;charset=gb2312");
		} catch (Exception ex) {
			LogWrite.error(ex);
		}
	}

	private double memory() {
		OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		Long TotalRAM = osmb.getTotalPhysicalMemorySize()/1024/1024;
		Long AvailabeRAM = osmb.getFreePhysicalMemorySize()/1024/1024;
		
		//内存空闲率
		double FreeRate = AvailabeRAM.doubleValue() / TotalRAM.doubleValue();
		
		//内存使用率
		double UtilizationRate = 1 - FreeRate; 
		
		return UtilizationRate;
		
	}
	
	//获取文件系统使用率 
	public Map<Character, Double> disk() { 
		// 操作系统 
		HashMap<Character, Double> maps = new HashMap<Character, Double>();
		for (char c = 'A'; c <= 'Z'; c++) {
			String dirName = c + ":/";
			File win = new File(dirName);
			if (win.exists()) {
				long total = (long) win.getTotalSpace();
				long free = (long) win.getFreeSpace();
				Double compare = (Double) (1 - free * 1.0 / total);
				maps.put(c, compare);
			}
		}
		return maps;
	} 
	
	/*//获取文件系统使用率 
	public double disk(char c) { 
	  // 操作系统 
	   String dirName = c + ":/"; 
	   File win = new File(dirName); 
	   Double compare = 0d;
	   if (win.exists()) {
		   long total = (long) win.getTotalSpace();
		   long free = (long) win.getFreeSpace();
		   compare = 1 - free * 1.0 / total;
		} 
	   return compare; 
	}*/
}