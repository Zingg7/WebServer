package com.webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTPЭ����ض�������
 * 
 * ���������Ŀ���ǽ�����HTTPЭ�鶨������ݶ���������
 * �������������ĸ�����Ҫ�õ�HTTPЭ��Ķ�����,�������������ҵ�
 * @author soft01
 *
 */
public class HttpContext {
	// Content-Type����Դ���͵�ӳ��
	// key: ��Դ����(��׺��
	// value: Content-Type��Ӧ��ֵ
	private static final Map<String, String> MIME_MAPPING = new HashMap<>();
	
	static{
		// ��ʼ��
		initMimeMapping();
	}
	
	// ��ʼ��MIME_MAPPING
	private static void initMimeMapping(){
		MIME_MAPPING.put("html", "text/html");
		MIME_MAPPING.put("css", "text/css");
		MIME_MAPPING.put("js", "application/javascript");
		MIME_MAPPING.put("png", "image/png");
		MIME_MAPPING.put("jpg", "image/jpeg");
		MIME_MAPPING.put("gif", "image/gif");
	}
	
	// ������Դ��������ȡ��Ӧ��Content-Typeֵ
	// @param ext ��Դ������(��png,html,css��)
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
