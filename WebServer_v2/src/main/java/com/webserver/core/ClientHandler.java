package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 用于处理客户端请求
 * @author soft01
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		try{
			// 读取客户端发送过来的内容
			InputStream in = socket.getInputStream();
			
			StringBuilder builder = new StringBuilder();
			int c1 = -1;
			int c2 = -1;
			while((c2 = in.read() ) != -1){
				if (c1 == 13 && c2 == 10){
					break;
				}
				builder.append((char)c2);
				c1 = c2;
			}
			String line = builder.toString().trim();
			System.out.println(line);
			
/*			读全部字符串
 			int d = -1;
			while((d = in.read()) != -1){
				System.out.print((char)d);
			}
*/			
			
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				// 处理完毕后与客户端断开释放资源
				socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
