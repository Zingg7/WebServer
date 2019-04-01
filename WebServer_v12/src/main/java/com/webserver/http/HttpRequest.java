package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private String method;    // 1.����ʽ
	private String url;       // 2.������Դ�ĳ���·��
	private String protocol;  // 3.����ʹ�õ�HTTPЭ��汾
	
	private String requestURI;    // ����url��?�������󲿷�
	private String queryString;   // ����url��?�Ҳ�Ĳ�������
	private Map<String, String> parameters = new HashMap<>();   // ��������ÿһ�����,key:������,value:����ֵ

	// ��Ϣͷ�����Ϣ����
	private Map<String, String> headers = new HashMap<>();
	
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
			   1. ����������
			   2. ������Ϣͷ
			   3. ������Ϣ����
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
			        ͨ��Socket��ȡ����������ȡ�ͻ��˷��͹����������е�һ���ַ���
			        ��һ��Ӧ�����������е�������
			        ��ȡ���ٽ������а��տո���в��,��ʱӦ�����Բ�ֳ���������:
			   method, url, protocol
			        �����ٽ��������õ���Ӧ�������ϼ���ɽ���
			 */
			String line = readLine();
			// ������ڿ���ѭ�����ܿͻ�������ʱż��������±�Խ������.ʵ������Ϊ�����������.���ڽ��
			String[] str = line.split("\\s");
			method = str[0];
			url = str[1];       // ����������ܻ�Խ��
			parseURL();        // ��һ������url
			protocol = str[2];
			System.out.println("method: "+method);
			System.out.println("url: "+url);
			System.out.println("protocol: "+protocol);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest: �����н������");
	}
	// ��һ�������������е�url����,��Ϊһ��url�������������:���в����򲻺��в���,������в���Ҫ�Բ������н���
	private void parseURL(){
		System.out.println("HttpRequest: ��һ������url");
		/* url���������
		 * 1. �����в�����,��:myweb/index.html: �������ֱ�ӽ�url��ֵ���õ�����requestURI�ϼ���
		 * 2. �в�����,��:/myweb/reg?username=zhangsan&password=123&...: 
		 * ��������������Ƚ�url����"?"��ֳ�������:
		 * ��һ����Ϊ���󲿷�,��ֵ��requestURI,�ڶ�����Ϊ��������,��ֵ��queryString
		 * Ȼ���ٶԲ������ֽ�һ�����:
		 * ���Ȱ���"&"��ֳ�ÿһ�����,Ȼ��ÿһ������ڰ���"="��ֳ�������,�ֱ��ǲ������Ͳ���ֵ,
		 * �ٽ�������key,value���뵽parameter���Map����������ɹ���0
		 */
		String[] split = url.split("\\?"); 
		requestURI = split[0];
		if (split.length>1){
			queryString = split[1];
			split = queryString.split("&");           // ��һ����ֺ��� 
			for(String paraStr : split){
				String[] arr = paraStr.split("=");    // ÿ����λ�ٰ���"="���
				if (arr.length > 1){
					parameters.put(arr[0], arr[1]);
				}else{
					parameters.put(arr[0], null);
				}
			}
		}
		
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
		System.out.println("HttpRequest: ����url���");
	}
	
	// 2. ������Ϣͷ
	private void parseHeaders(){
		System.out.println("HttpRequest: ������Ϣͷ");
		/* 
		   parseRequestLine�����Ѿ�ͨ���������������е����������ݶ�ȡ���
		        ��ô��paseHeader�������ʱ��ͨ����������ȡ�����ݾ�Ӧ������Ϣͷ������
		  
		         ����˼·:
		         ˳���ȡ�������ַ���,ÿһ�ж���һ����Ϣͷ
		         ����Ϣͷ����"ð�ſո�"(: )��ֳ�������,�ֱ�����Ϣͷ���������Ӧ��ֵ
		         ����ÿ����Ϣͷ��������Ϊkey,��Ϣͷ��ֵ��Ϊvalue,���浽headers���map�мȿ���ɽ�������
		         �����ȡһ���ַ���ʱ���ص�ʱһ�����ַ���,��:"",��˵�����ε�����ȡ����CRLF(��trim)
		         ��ô�Ϳ���ֹͣ��ȡ������
		   
		 */
		try {
			while (true) {
				String line = readLine();
				if ("".equals(line)) {
					break;
				}
				String[] str = line.split(": ");
				headers.put(str[0], str[1]);
			}
			System.out.println("headers: "+headers);
			/*Set<Entry<String, String>> entrySet = headers.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + ": " + value);
			}*/
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("HttpRequest: ��Ϣͷ�������");
	}
	
	// 3. ������Ϣ����
	private void parseContent(){
		System.out.println("HttpRequest: ������Ϣ����");

		
		System.out.println("HttpRequest: ��Ϣ���Ľ������");
	}
	
	// ͨ����Ӧ�ͻ��˵�������,��ȡһ�пͻ��˷��͹������ַ���(1.2.3�о�ʹ��)
	private String readLine() throws IOException {
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
		return builder.toString().trim();
	}

	
	/*
	 * ����HttpRequest��Ӧ���Խ������ṩget����
	 * ��Ϊ���������ǿͻ��˷��͹�����,���Բ���Ҫ�������Ķ�,������ֻ���ļȿ�
	 */
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	// ����ֱ�ӷ���Map�����û����Խ���put����
	// ���û�ͨ������key��ȡ��Ӧ��value
	public String getHeaders(String name) {
		return headers.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getParameters(String name) {
		return parameters.get(name);
	}
	
	
	
}
