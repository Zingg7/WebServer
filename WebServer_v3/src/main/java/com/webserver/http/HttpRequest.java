package com.webserver.http;

import java.io.InputStream;
import java.net.Socket;

/**
 * �������(��������) 
 * ����ĵ�ÿһ��ʵ�����ڱ�ʾһ��HTTP��������
 * 
 * һ��HTTP��������������� ������,��Ϣͷ,��Ϣ����
 * 
 * @author soft01
 *
 */
public class HttpRequest {

	/*
	 * ������Զ���
	 */
	// �����������Ϣ����
	private String method; // 1.����ʽ
	private String url; // 2.������Դ�ĳ���·��
	private String protocol; // 3.����ʹ�õ�HTTPЭ��汾

	// ��Ϣͷ�����Ϣ����


	// ��Ϣ���������Ϣ����


	//�����������Ϣ����
	private Socket socket;
	private InputStream in;
	
	
	/* 
	 * ���췽��,������ʼ���������
	 */
	public HttpRequest(Socket socket) {
		try {
			this.socket = socket;
			this.in = socket.getInputStream();
			/*
			 * 1. ����������
			 * 2. ������Ϣͷ
			 * 3. ������Ϣ����
			 */
			parseRequestLine();
			parseHeaders();
			parseContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// ��д���췽���е��õ�����
	// 1. ����������----��ȡ�ͻ��˷��͹���������
	private void parseRequestLine(){
		System.out.println("HttpRequest: ����������");
		try {
			/*
			 * ͨ��Socket��ȡ����������ȡ�ͻ��˷��͹����������е�һ���ַ���
			 * ��һ��Ӧ�����������е�������
			 * ��ȡ���ٽ������а��տո���в��,��ʱӦ�����Բ�ֳ���������:
			 * method, url, protocol
			 * �����ٽ��������õ���Ӧ�������ϼ���ɽ���
			 */

			// ��ȡ�ͻ��˷��͹���������
			// (InputStream�����������⹹����)
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
			
			// ������ڿ���ѭ�����ܿͻ�������ʱż��������±�Խ������.ʵ������Ϊ�����������.���ڽ��
			String[] str = line.split("\\s");
			method = str[0];
			url = str[1];       // ����������ܻ�Խ��
			protocol = str[2];
			System.out.println("method: "+method);
			System.out.println("url: "+url);
			System.out.println("protocol: "+protocol);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest: �����н������");
	}
	
	// 2. ������Ϣͷ
	private void parseHeaders(){
		System.out.println("HttpRequest: ������Ϣͷ");
		
	}
	
	// 3. ������Ϣ����
	private void parseContent(){
		System.out.println("HttpRequest: ������Ϣ����");
	}
	

}
