package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.webserver.http.HttpRequest;

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
			// 1 解析请求
			// 实例化请求对象,实例化过程也是解析请求的过程
			HttpRequest request = new HttpRequest(socket);
			
			// 2 处理请求
			
			
			// 3 发送响应
			
		
			
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
