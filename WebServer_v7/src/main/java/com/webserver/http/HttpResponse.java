package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 响应对象
 * 该类的每一个实例用于表示服务端发送给哭护短的http响应内容
 */
public class HttpResponse {
	/*
	 *  相关属性定义
	 */
	// 状态行相关信息定义
	
	// 响应头相关信息定义
	
	// 响应正文相关信息定义
	private File entity;        // 响应正文的实体文件
	
	
	private Socket socket;
	private OutputStream out;
	/*
	 * 实例化请求对象的同时将Socket传入,以便当前响应对象通过他来获取输出流给对应客户端发送响应内容
	 */
	public HttpResponse(Socket socket){
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/*
	 * 用于将当前响应对象的内容以一个标准的HTTP响应格式发送给客户端
	 */
	public void flush(){
		sendStatusLine();
		sendHeaders();
		sendContent();
		
	}
	
	// 发送状态行
	private void sendStatusLine(){
		try {
			String line = "HTTP/1.1 200 OK";
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 发送响应头
	private void sendHeaders(){
		try {
			String line = "Content-Type: text/html";
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
			
			line = "Content-Length: "+entity.length();
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
			
			// 单独发送CRLF表示响应头部分发送完毕
			out.write(13);
			out.write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 发送响应正文
	private void sendContent(){
		try (FileInputStream fis = new FileInputStream(entity);){
			int len = -1;
			byte[] data = new byte[1024*10];
			while ((len = fis.read(data))!= -1){
				out.write(data,0,len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public File getEntity() {
		return entity;
	}

	public void setEntity(File entity) {
		this.entity = entity;
	}
	
	
}
