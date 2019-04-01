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
 * ��Ӧ����
 * �����ÿһ��ʵ�����ڱ�ʾ����˷��͸��ͻ��˵�http��Ӧ����
 */
public class HttpResponse {
	/*
	 *  ������Զ���
	 */
	// ״̬�������Ϣ����
	private int statusCode = 200;     // ״̬����,Ĭ��ֵΪ200
	private String statusReason ="OK";// ״̬����,Ĭ��ֵΪOK
	
	// ��Ӧͷ�����Ϣ����
	// key:��Ӧͷ����   value:��Ӧͷ��Ӧ��ֵ
	private Map<String, String> headers = new HashMap<>();
	
	
	// ��Ӧ���������Ϣ����
	private File entity;        // ��Ӧ���ĵ�ʵ���ļ�
	
	
	private Socket socket;
	private OutputStream out;
	/*
	 * ʵ������������ͬʱ��Socket����,�Ա㵱ǰ��Ӧ����ͨ��������ȡ���������Ӧ�ͻ��˷�����Ӧ����
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
	 * ���ڽ���ǰ��Ӧ�����������һ����׼��HTTP��Ӧ��ʽ���͸��ͻ���
	 */
	public void flush(){
		sendStatusLine();
		sendHeaders();
		sendContent();
		
	}
	
	// ����״̬��
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
	// ������Ӧͷ
	private void sendHeaders(){
		try {
			// ͨ������headers,��������Ҫ���͸��ͻ��˵���Ӧͷ���з���
			Set<Entry<String, String>> entrySet = headers.entrySet();
			for(Entry<String, String> e: entrySet){
				String name = e.getKey();
				String value = e.getValue();
				String line = name+": "+value;
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
			}
			// ��������CRLF��ʾ��Ӧͷ���ַ������
			out.write(13);
			out.write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ������Ӧ����
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
	
	// ��������ʵ���ļ����õ�response��,���õ�ͬʱ���Զ����ݸ��ļ���Ӷ�Ӧ��������Ӧͷ:
	// Content-Type��Content-Length
	public void setEntity(File entity) {
		this.entity = entity;
		// ����������Դ��ʵ������,����Content-Typeͷ
		String fileName = entity.getName();     // ��ȡ�ļ�����Ӧ�ĺ�׺
		int index = fileName.lastIndexOf(".")+1;
		String ext = fileName.substring(index);
		String line = HttpContext.getMimeType(ext);    // ��ú�׺��Ӧ������
		this.putHeader("Content-Type", line);     // ���Content-Type
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
	
	// ����������Ϣͷ���õ�HttpResponse�� 
	public void putHeader(String name, String value){
		this.headers.put(name, value);
	}
	public String getHeader(String name){
		return this.headers.get(name);
	}
	
}
