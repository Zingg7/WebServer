package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class RegServlet extends HttpServlet{
	public void service(HttpRequest request, HttpResponse response){
		System.out.println("RegServlet: 开始注册用户业务");
		/*
		 * 1:通过request获取用户在注册页面上输入的注册信息
		 * 2:将该用户信息写入到文件user.dat中保存起来
		 * 3:响应客户端注册成功页面
		 */
		// 1
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		String nickname = request.getParameters("nickname");
		int age = Integer.parseInt(request.getParameters("age"));
		System.out.println(username+","+password+","+nickname+","+age);
		// 2 
		// 将用户信息写入user.dat文件,每个用户占用100字节,其中用户名,密码,昵称为字符串,各站32字节,年龄是int占4字节
		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");){
			// 将指针移动到文件末尾
			raf.seek(raf.length());
			// 写用户名
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);  // 写了32字节
			// 写密码
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			// 写昵称
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			// 写年龄
			raf.writeInt(65);
			// 3
			// 设置Response响应注册成功页面
			response.setEntity(new File("./webapps/myweb/reg_success.html"));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("RegServlet: 注册用户业务完毕");
	}
}
