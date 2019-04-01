package com.webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP协议相关定义内容
 * 
 * 设计这个类的目的是将所有HTTP协议定义的内容都放在这里
 * 这样我们无论哪个类需要用到HTTP协议的东西是,都可以来这里找到
 * @author soft01
 *
 */
public class HttpContext {
	// Content-Type与资源类型的映射
	// key: 资源类型(后缀名
	// value: Content-Type对应的值
	private static final Map<String, String> MIME_MAPPING = new HashMap<>();
	
	static{
		// 初始化
		initMimeMapping();
	}
	
	// 初始化MIME_MAPPING
	private static void initMimeMapping(){
		MIME_MAPPING.put("html", "text/html");
		MIME_MAPPING.put("css", "text/css");
		MIME_MAPPING.put("js", "application/javascript");
		MIME_MAPPING.put("png", "image/png");
		MIME_MAPPING.put("jpg", "image/jpeg");
		MIME_MAPPING.put("gif", "image/gif");
	}
	
	// 根据资源类型名获取对应的Content-Type值
	// @param ext 资源类型名(如png,html,css等)
	public static String getMimeType(String ext){
		return MIME_MAPPING.get(ext);
	}
	
	public static void main(String[] args) {
		String fileName = "xxx.png";
		/*String[] ans = fileName.split("\\.");
		System.out.println(HttpContext.getMimeType(ans[ans.length-1]));*/
		int index = fileName.lastIndexOf(".")+1;
		String ext = fileName.substring(index);
		String line = HttpContext.getMimeType(ext);
		System.out.println(line);
		
		// String line = HttpContext.getMimeType("css");
		// System.out.println(line);
	}
}
