package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
			// 先通过request获取用户请求的资源的抽象路径
			String path = request.getUrl();
			// 从webapps目录下根据该路径寻找请求资源
			File file = new File("./webapps"+path);
			if (file.exists()){
				System.out.println("该资源已找到");
				// 将该资源以一个HTTP响应发送给哭护短
				/*
				 * HTTP/1.1 200 OK(CPLF)
				 * Content-Type: text/html
				 * Content-Length:224586(CRLF)(CRLF)
				 * 1101010101001.....2进制字节数据
				 */
				OutputStream out = socket.getOutputStream();
				// 发送状态行
				String line = "HTTP/1.1 200 OK";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				// 发送响应头
				line = "Content-Type: text/html";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				
				line = "Content-Length: "+file.length();
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				
				// 单独发送CRLF表示响应头部分发送完毕
				out.write(13);
				out.write(10);
				
				// 发送响应正文(用户实际请求的资源数据)
				FileInputStream fis = new FileInputStream(file);
				int len = -1;
				byte[] data = new byte[1024*10];
				while ((len = fis.read(data))!= -1){
					out.write(data,0,len);
				}
				
				// 所有响应内容发送完毕
				
			}else{
				System.out.println("该资源不存在");
			}
			
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
