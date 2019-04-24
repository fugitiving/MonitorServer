/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		Converter.java
 * 作者		高科
 * 编写日期	2019-03-27
 */
package com.jdsn.util;

/*
 * TODO
 * 
 * @version 1.0.0.0
 * @author 高科
 */

public class Converter {
	/**
	 * string转1个字节
	 * 
	 * @param property
	 * @return
	 */
	public static byte[] StringToByte(int num) {
		byte b[] = new byte[1];
		b[0] = (byte) (num & 0xff);
		return b;
	}

	/**
	 * int转byte数组
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] IntToByte(int num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((num >> 24) & 0xff);
		bytes[1] = (byte) ((num >> 16) & 0xff);
		bytes[2] = (byte) ((num >> 8) & 0xff);
		bytes[3] = (byte) (num & 0xff);
		return bytes;
	}

	/**
	 * short转byte数组
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] ShortToByte(int num) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) ((num >> 8) & 0xff);
		bytes[1] = (byte) (num & 0xff);
		return bytes;
	}
	
	public static String substring(String src, int start_idx, int end_idx) { 
		   byte[] b = src.getBytes(); 
		   String tgt = ""; 
		   for (int i = start_idx; i <= end_idx; i++) { 
			   if((char)b[i] != ' ') {
				   tgt += (char) b[i];   
			   }
		   } 
		   return tgt; 
	} 
}
