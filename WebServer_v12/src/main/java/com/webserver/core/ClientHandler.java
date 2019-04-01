package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.webserver.http.HttpContext;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.RegServlet;

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
			// ʵ������Ӧ����
			HttpResponse response = new HttpResponse(socket);
			
			// 2 ��������
			// ��ͨ��request��ȡ�û��������Դ�ĳ���·��
			String path = request.getRequestURI();
			// �����жϸ�����·���Ƿ�Ϊ����һ��ҵ��
			if("/myweb/reg".equals(path)){   // ��Ϊ����ע��ҵ��
				RegServlet servlet = new RegServlet();
				servlet.service(request, response);
			}else{
				// ��webappsĿ¼�¸��ݸ�·��Ѱ��������Դ
				File file = new File("./webapps"+path);
				if (file.exists()){
					System.out.println("����Դ���ҵ�");
					// ��Ҫ��Ӧ����Դ���õ�response��
					response.setEntity(file);
					
				}else{
					System.out.println("����Դ������");
					// ��Ӧ404ҳ��
					response.setEntity(new File("./webapps/root/404.html"));
									
					// ����״̬����������
					response.setStausCode(404);
					response.setStatusReason("NOT FOUND");
				}
			}
		
			
			// 3 ������Ӧ
			// ��Ӧ�ͻ���
			response.flush();
			
		
			
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
