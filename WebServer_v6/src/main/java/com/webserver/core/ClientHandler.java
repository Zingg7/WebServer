package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.webserver.http.HttpRequest;

/**
 * ���ڴ���ͻ�������
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
			// 1 ��������
			// ʵ�����������,ʵ��������Ҳ�ǽ�������Ĺ���
			HttpRequest request = new HttpRequest(socket);
			
			// 2 ��������
			// ��ͨ��request��ȡ�û��������Դ�ĳ���·��
			String path = request.getUrl();
			// ��webappsĿ¼�¸��ݸ�·��Ѱ��������Դ
			File file = new File("./webapps"+path);
			if (file.exists()){
				System.out.println("����Դ���ҵ�");
				// ������Դ��һ��HTTP��Ӧ���͸��޻���
				/*
				 * HTTP/1.1 200 OK(CPLF)
				 * Content-Type: text/html
				 * Content-Length:224586(CRLF)(CRLF)
				 * 1101010101001.....2�����ֽ�����
				 */
				OutputStream out = socket.getOutputStream();
				// ����״̬��
				String line = "HTTP/1.1 200 OK";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				// ������Ӧͷ
				line = "Content-Type: text/html";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				
				line = "Content-Length: "+file.length();
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				
				// ��������CRLF��ʾ��Ӧͷ���ַ������
				out.write(13);
				out.write(10);
				
				// ������Ӧ����(�û�ʵ���������Դ����)
				FileInputStream fis = new FileInputStream(file);
				int len = -1;
				byte[] data = new byte[1024*10];
				while ((len = fis.read(data))!= -1){
					out.write(data,0,len);
				}
				
				// ������Ӧ���ݷ������
				
			}else{
				System.out.println("����Դ������");
			}
			
			// 3 ������Ӧ
			
		
			
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				// ������Ϻ���ͻ��˶Ͽ��ͷ���Դ
				socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
