package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ��Ӧ����
 * �����ÿһ��ʵ�����ڱ�ʾ����˷��͸��޻��̵�http��Ӧ����
 */
public class HttpResponse {
	/*
	 *  ������Զ���
	 */
	// ״̬�������Ϣ����
	
	// ��Ӧͷ�����Ϣ����
	
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
			String line = "HTTP/1.1 200 OK";
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
			String line = "Content-Type: text/html";
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
			
			line = "Content-Length: "+entity.length();
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
			
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

	public void setEntity(File entity) {
		this.entity = entity;
	}
	
	
}
