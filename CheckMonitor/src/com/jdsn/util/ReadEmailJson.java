/*
* 模块编号 
* 功能描述 
* 文件名 
* 作者 
* 编写日期 
*/
package com.jdsn.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jdsn.log.LogWrite;

/**
 * （功能描述）
 * 
 * @version （版本号） @author（作者）
 */
public class ReadEmailJson {
	/**
	 * 配置文件所在的路径
	 */
	private static final String JSON_NAME = "src//conf\\email_config.json";
	/**
	 * properties加载数据库的信息以及文件生成的路径
	 */
	public static String TITLE = null;
	public static String FROM = null;
	public static String CONTENT = null;
	public static String FILEURL = null;
	public static String FILENAME = null;
	public static String SMTP = null;
	public static String SERVICE_NAME = null;
	public static String SERVICE_PASWD = null;
	public static JSONArray RECEIVES = null;
	
	 static {

		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(JSON_NAME);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
			
			@SuppressWarnings("static-access")
			JSONObject root = new JSONObject().parseObject(laststr);// 将json格式的字符串转换成json
			
			TITLE = root.getString("title");			
			FROM = root.getString("from");			
			CONTENT= root.getString("content"); 			
			FILEURL = root.getString("fileUrl");				
			SMTP = root.getString("smtp"); 				
			SERVICE_NAME = root.getString("from"); 		
			SERVICE_PASWD = root.getString("serverpaswd");  		
			RECEIVES = root.getJSONArray("receives");          
		
		} catch (IOException e) {
			LogWrite.error(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LogWrite.error(e);
				}
			}
		}
	}

}
