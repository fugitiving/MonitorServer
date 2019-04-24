/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		LogWrite.java
 * 作者		高科
 * 编写日期	2019-04-23
 */
package com.jdsn.log;



import org.apache.log4j.Logger;

/*
 * TODO
 * 
 * @version 1.0.0.0
 * @author 高科
 */

public class LogWrite {

	private static Logger logger = Logger.getLogger(LogWrite.class);
	
	public static void debug(Object obj) {
		logger.debug(obj);
	}
	
	public static void info(Object obj) {
		logger.info(obj);
	}
	
	public static void error(Object obj) {
		logger.error(obj);
	}
}
