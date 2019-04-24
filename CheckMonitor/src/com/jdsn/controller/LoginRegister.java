/*
 * 模块编号
 * 功能描述	TODO
 * 文件名		Login.java
 * 作者		高科
 * 编写日期	2019-03-27
 */
package com.jdsn.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import com.jdsn.log.LogWrite;
import com.jdsn.util.Converter;
import com.jdsn.util.Encrypt;

/*
 * TODO
 * 
 * @version 1.0.0.0
 * @author 高科
 */

public class LoginRegister {
	public byte[] Login(){
		try {
			FileInputStream fis = new FileInputStream("src//conf\\Register.properties");
			Properties pro = new Properties();
			pro.load(new InputStreamReader(fis, "utf-8"));

			String serverIp = pro.getProperty("serverIp");
			int serverPort = Integer.parseInt(pro.getProperty("serverPort"));
			// 创建Socket对象
			Socket socket = new Socket(serverIp, serverPort);

			// 2、获取输出流，向服务器端发送信息
			OutputStream os = socket.getOutputStream();// 字节输出流
			PrintWriter pw = new PrintWriter(os);// 将输出流包装成打印流

			byte[] b = encode(pro);

			os.write(b);
			Thread.sleep(1000);
			pw.flush();

			socket.shutdownOutput();

			// 3、获取输入流，并读取服务器端的响应信息
			InputStream is = socket.getInputStream();

			byte recv[] = new byte[256];
			is.read(recv);
			//System.out.println(Arrays.toString(recv));

			/*// 判断接收帧是否正确
			if (recv[18] == 0) {
				System.out.println("接收应答帧成功");
			} else {
				System.out.println("接收应答帧失败");
			}*/

			decode(recv);

			byte temp[] = new byte[4];
			System.arraycopy(recv, 0, temp, 0, 4);
			//System.out.println(new String(temp));
			// 4、关闭资源
			// br.close();
			is.close();
			pw.close();
			os.close();
			socket.close();
			return recv;
		} catch (UnknownHostException e) {
			LogWrite.error(e);
		} catch (IOException e) {
			LogWrite.error(e);
		} catch (InterruptedException e) {
			LogWrite.error(e);
		} finally {

		}
		return null;
	}

	/**
	 * 组包（注册帧）
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] encode(Properties pro) throws UnsupportedEncodingException {

		byte[] b = new byte[256];

		int meslen = 0;

		// 报文头标识
		byte[] temp = pro.getProperty("flag").getBytes();
		System.arraycopy(temp, 0, b, 0, 4);
		meslen += temp.length;

		// 版本号
		temp = Converter.ShortToByte(Integer.parseInt(pro.getProperty("version")));
		System.arraycopy(temp, 0, b, 4, 2);
		meslen += temp.length;

		// 报文长度
		// temp = IntToByte(88);
		// System.arraycopy(temp, 0, b, 6, 4);
		// b[9] = 88;
		meslen += 4;

		// 挑战字
		temp = Converter.IntToByte(Integer.parseInt(pro.getProperty("challange")));
		System.arraycopy(temp, 0, b, 10, 4);
		meslen += temp.length;

		// 帧类型 0x00000001
		temp = Converter.IntToByte(Integer.parseInt(pro.getProperty("registerType")));
		System.arraycopy(temp, 0, b, 14, 4);
		meslen += temp.length;

		// 服务器名长度
		// temp = ShortToByte(Integer.parseInt(pro.getProperty("serverNameLen")));
		// System.arraycopy(temp, 0, b, 18, 2);

		// 服务器名
		String serName = pro.getProperty("serverName");
		temp = serName.getBytes("GBK");
		System.arraycopy(temp, 0, b, 20, 8);
		meslen += temp.length;

		// 服务器名长度
		temp = Converter.ShortToByte(temp.length);
		System.arraycopy(temp, 0, b, 18, 2);
		meslen += temp.length;

		// 用户名长度
		// temp = ShortToByte(Integer.parseInt(pro.getProperty("userNameLen")));
		// System.arraycopy(temp, 0, b, 28, 2);

		// 用户名
		temp = pro.getProperty("userName").getBytes();
		System.arraycopy(temp, 0, b, 30, 2);
		meslen += temp.length;

		// 用户名长度
		temp = Converter.ShortToByte(temp.length);
		System.arraycopy(temp, 0, b, 28, 2);
		meslen += temp.length;

		// 密码长度
		// temp = ShortToByte(Integer.parseInt(pro.getProperty("passwordLen")));
		// System.arraycopy(temp, 0, b, 32, 2);

		// 密码
		Encrypt ep = new Encrypt();
		String pswd = ep.EncryptPassword(pro.getProperty("userName"), pro.getProperty("password"), "", 1024);
		temp = pswd.getBytes();
		// temp = "D34FB7905C8084A28D3B66F97D6053818A28626C9B740639".getBytes();
		System.arraycopy(temp, 0, b, 34, 48);
		meslen += temp.length;

		// 密码长度
		temp = Converter.ShortToByte(temp.length);
		System.arraycopy(temp, 0, b, 32, 2);
		meslen += temp.length;

		// 本地版本号长度
		// temp = ShortToByte(Integer.parseInt(pro.getProperty("localVersionLen")));
		// System.arraycopy(temp, 0, b, 82, 2);

		// 本地版本号
		temp = pro.getProperty("localVersion").getBytes();
		System.arraycopy(temp, 0, b, 84, 3);
		meslen += temp.length;

		// 本地版本号长度
		temp = Converter.ShortToByte(temp.length);
		System.arraycopy(temp, 0, b, 82, 2);
		meslen += temp.length;

		// CRC
		temp = Converter.StringToByte(Integer.parseInt(pro.getProperty("crc")));
		System.arraycopy(temp, 0, b, 87, 1);
		meslen += temp.length;

		b[9] = (byte) meslen;

		byte buf[] = new byte[meslen];
		System.arraycopy(b, 0, buf, 0, meslen);
		return buf;
	}

	/**
	 * 解包
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] decode(byte[] recv) {

		return recv;
	}

	
}