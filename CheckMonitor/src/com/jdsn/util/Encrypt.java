
package com.jdsn.util;



public class Encrypt {

	static {
		System.loadLibrary("src//conf//EncryptPasswordJNI");
	}
	
	public native String EncryptPassword(String username, String password, String buf, int buf_size);
	
//	public static void main(String args[]) {
//		EncryptPassword ep = new EncryptPassword();
//		String string = ep.EncryptPassword("wq","wangqiong", "", 1024);
//		
//		System.out.println(string);
//	}
}
