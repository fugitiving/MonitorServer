/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		LoginWeb.java
 * 作者		高科
 * 编写日期	2019-03-27
 */
package com.jdsn.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/*
 * TODO
 *
 * @version 1.0.0.0
 * @author 高科
 */

public class LoginWeb {
	public int logonWeb() throws IOException{
	//public static void main(String[] args) throws IOException {
		
		//加载访问Web信息的Ip和Port
		FileInputStream fis = new FileInputStream("src//conf\\Register.properties");
		Properties pro = new Properties();
		pro.load(new InputStreamReader(fis, "utf-8"));
		
		String ip = pro.getProperty("webServerIp");
		String port = pro.getProperty("webServerPort");
		
        // 登陆 Url：登录界面
        String loginUrl = "http://" + ip + ":" + port + "/frame/page/login.aspx";
        
        // 需登陆后访问的 Url：主界面，目前未实现，中间有一个验证没有搞明白
        //String dataUrl = "http://192.168.0.4:888/frame/page/main.aspx";
        
        Connection con = Jsoup.connect(loginUrl);// 获取连接
        con.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
        Response rs = con.execute();// 获取响应
        return rs.statusCode();
        /*// 获取，cooking和表单属性，下面map存放post时的数据
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("ctl02_UserName", "wq");
        datas.put("ctl02_Password", "wangqiong");
        
        System.out.println(rs.cookies());
        *//**
         * 第二次请求，post表单数据，以及cookie信息
         * 
         * **//*
        Connection con2 = Jsoup.connect(loginUrl);
        con2.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
        // 设置cookie和post上面的map数据
        
        Response login = con2.ignoreContentType(true).method(Method.POST)
                .data(datas).cookies(rs.cookies()).execute();
        // 打印，登陆成功后的信息
        int statcode = login.statusCode();
        System.out.println(statcode);*/
	}
}