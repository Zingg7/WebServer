package com.webserver.core;

import java.io.File;
import java.io.IOException;
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
