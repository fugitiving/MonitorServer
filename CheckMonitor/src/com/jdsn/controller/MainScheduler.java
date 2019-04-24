/*
* 模块编号 
* 功能描述 
* 文件名 
* 作者 
* 编写日期 
*/
package com.jdsn.controller;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
/**
* （功能描述） 
* 
* @version （版本号）
* @author（作者） 
*/
public class MainScheduler {
	 /**
	  * 创建调度器
	  * @return 调度器
	  */
    public static Scheduler getScheduler() throws SchedulerException{
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory.getScheduler();
    }
    
    /**
     * 任务调度（多长时间执行） 
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     */
    public static void schedulerJob() throws SchedulerException, UnsupportedEncodingException, IOException{
        //创建任务
        JobDetail jobA = JobBuilder.newJob(CheckLogin.class).withIdentity("job1", "group1").build();
        JobKey jobKeyB = new JobKey("jobB", "group1"); 
        JobDetail jobB = JobBuilder.newJob(CheckSystem.class) .withIdentity(jobKeyB).build();

        FileInputStream fis = new FileInputStream("src//conf\\cronSchedule.properties");
		Properties pro = new Properties();
		pro.load(new InputStreamReader(fis, "utf-8"));
		
		String cronLogin = pro.getProperty("cronLogin");
		String cronCheckSystem = pro.getProperty("cronCheckSystem");
		
        //创建触发器 每cronLogin秒执行一次
        /*Trigger triggerLogin = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
              .withSchedule(CronScheduleBuilder.cronSchedule(cronLogin)).build();*/  //使用cron定时器	cronLogin=0 0/5 * * * ?
		Trigger triggerLogin = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
	              .withSchedule(simpleSchedule()		//使用SimpleTrigger
	            		  .withIntervalInSeconds(Integer.parseInt(cronLogin))		//每隔一秒执行一次
	            		  .repeatForever())		//一直执行
	              .build();
        Scheduler scheduler = getScheduler();
        
        //创建触发器 每cronCheckSystem秒执行一次
        /*Trigger triggerSystem = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1")
                            .withSchedule(CronScheduleBuilder.cronSchedule(cronCheckSystem)).build();*/	//cronLogin=0 0/5 * * * ?
        Trigger triggerSystem = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1")
                .withSchedule(simpleSchedule().withIntervalInSeconds(Integer.parseInt(cronCheckSystem)).repeatForever()).build();
        //调度器开始调度任务
        scheduler.start();
        //将任务及其触发器放入调度器
        scheduler.scheduleJob(jobA, triggerLogin);
        scheduler.scheduleJob(jobB, triggerSystem);
    }
}
