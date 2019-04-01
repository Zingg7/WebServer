package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示服务端发送给客户端的http响应内容
 */
public class HttpResponse {
	/*
	 *  相关属性定义
	 */
	// 状态行相关信息定义
	private int statusCode = 200;     // 状态代码,默认值为200
	private String statusReason ="OK";// 状态描述,默认值为OK
	
	// 响应头相关信息定义
	// key:响应头名字   value:响应头对应的值
	private Map<String, String> headers = new HashMap<>();
	
	
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
			String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
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
			// 通过遍历headers,将所有需要发送给客户端的响应头进行发送
			Set<Entry<String, String>> entrySet = headers.entrySet();
			for(Entry<String, String> e: entrySet){
				String name = e.getKey();
				String value = e.getValue();
				String line = name+": "+value;
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
			}
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
	
	// 将给定的实体文件设置到response中,设置的同时会自动根据该文件添加对应的两个响应头:
	// Content-Type和Content-Length
	public void setEntity(File entity) {
		this.entity = entity;
		// 根据请求资源的实际类型,设置Content-Type头
		String fileName = entity.getName();     // 获取文件名对应的后缀
		int index = fileName.lastIndexOf(".")+1;
		String ext = fileName.substring(index);
		String line = HttpContext.getMimeType(ext);    // 获得后缀对应的类型
		this.putHeader("Content-Type", line);     // 输出Content-Type
		this.putHeader("Content-Length", entity.length()+"");
	}

	public int getStausCode() {
		return statusCode;
	}

	public void setStausCode(int stausCode) {
		this.statusCode = stausCode;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String stausReason) {
		this.statusReason = stausReason;
	}
	
	// 将给定的消息头设置到HttpResponse中 
	public void putHeader(String name, String value){
		this.headers.put(name, value);
	}
	public String getHeader(String name){
		return this.headers.get(name);
	}
	
}
