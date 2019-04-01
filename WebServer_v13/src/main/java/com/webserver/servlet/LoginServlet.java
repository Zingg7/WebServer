package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class LoginServlet {
	public void service(HttpRequest request, HttpResponse response){
		System.out.println("RegServlet: ��ʼ�����¼ҵ��");
		/*
		 * 1:ͨ��request��ȡ�û���ע��ҳ���������ע����Ϣ
		 * 2:ʹ��RandomAccessFile��ȡuser.dat�ļ�
		 * 3:˳���ȡÿ����¼,���ȶ��û���������
  		 *	���бȶԳɹ���,������response��Ӧ��¼�ɹ�ҳ��
  		 *  ���û�����ȷ�����벻��ȷ,��user.dat�ļ���û�и��û�,������response��Ӧ��¼ʧ��ҳ��
		 */
		// 1
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		// 2
		try(RandomAccessFile raf = new RandomAccessFile("user.dat", "rw")){
			String userTemp = "";
			String passwordTemp = "";		
			for (int i = 0; i < raf.length()/100; i ++){
				raf.seek(i*100);
				byte[] data = new byte[32];
				raf.read(data);
				userTemp = new String(data, "utf-8").trim();
				raf.read(data);
				passwordTemp = new String(data, "utf-8").trim();
				// 3
				if (userTemp.equals(username) && passwordTemp.equals(password)){
					response.setEntity(new File("./webapps/myweb/login_success.html"));
					break;
				}
			}
			if (!userTemp.equals(username)|| !passwordTemp.equals(password)){
				response.setEntity(new File("./webapps/myweb/login_fail.html"));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
				
		
	}
	
}
