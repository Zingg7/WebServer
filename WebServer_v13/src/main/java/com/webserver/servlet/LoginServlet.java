package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class LoginServlet {
	public void service(HttpRequest request, HttpResponse response){
		System.out.println("RegServlet: 开始处理登录业务");
		/*
		 * 1:通过request获取用户在注册页面上输入的注册信息
		 * 2:使用RandomAccessFile读取user.dat文件
		 * 3:顺序读取每条记录,并比对用户名与密码
  		 *	若有比对成功的,则设置response响应登录成功页面
  		 *  若用户名正确但密码不正确,或user.dat文件中没有该用户,则设置response响应登录失败页面
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
