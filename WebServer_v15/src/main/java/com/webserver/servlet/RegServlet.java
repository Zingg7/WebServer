package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class RegServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response){
		System.out.println("RegServlet: ��ʼע���û�ҵ��");
		/*
		 * 1:ͨ��request��ȡ�û���ע��ҳ���������ע����Ϣ
		 * 2:�����û���Ϣд�뵽�ļ�user.dat�б�������
		 * 3:��Ӧ�ͻ���ע��ɹ�ҳ��
		 */
		// 1
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		String nickname = request.getParameters("nickname");
		int age = Integer.parseInt(request.getParameters("age"));
		System.out.println(username+","+password+","+nickname+","+age);
		// 2 
		// ���û���Ϣд��user.dat�ļ�,ÿ���û�ռ��100�ֽ�,�����û���,����,�ǳ�Ϊ�ַ���,��վ32�ֽ�,������intռ4�ֽ�
		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");){
			// ��ָ���ƶ����ļ�ĩβ
			raf.seek(raf.length());
			// д�û���
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);  // д��32�ֽ�
			// д����
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			// д�ǳ�
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			// д����
			raf.writeInt(65);
			// 3
			// ����Response��Ӧע��ɹ�ҳ��
			response.setEntity(new File("./webapps/myweb/reg_success.html"));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("RegServlet: ע���û�ҵ�����");
	}
}
