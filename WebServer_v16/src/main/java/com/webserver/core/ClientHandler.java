package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;

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
			System.out.println("Show All Users:");
			ShowAllUserTest show = new ShowAllUserTest(socket);
			// 2 ��������                                                                                                                                         
			// ��ͨ��request��ȡ�û��������Դ�ĳ���·��
			String path = request.getRequestURI();
			// �����жϸ�����·���Ƿ�Ϊ����һ��ҵ��,��������·��ȡServerContext��ȡ��Ӧ��Servlet
			// ������ֵ����null,��˵��������ʱ����һ��ҵ��,��ô�õ��ö�ӦServlet��service����
			HttpServlet servlet = ServerContext.getServlet(path);
			if (servlet != null){ // ��Ϊ��̬ҳ��ʱ,��ServerContext���Ҳ�����Ӧ�Ĵ���취,servlet����null
				servlet.service(request, response);
			}
			else{
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
			
		} catch (EmptyRequestException e){
			// ���ﵥ������������쳣,���ClentHandler��һ��ʼʵ�����������HttpRequestʱ,�ù��췽���׳��˿������쳣
			// ��ô��ֱ������catch����,�����͵ƻ�������ClientHandler����Ӧ���еĴ����������Ӧ�ͻ��˵Ĳ�����
			System.out.println("������");
		} catch (Exception e){
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
