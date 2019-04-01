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
 * 用于处理客户端请求
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
			// 1 解析请求
			// 实例化请求对象,实例化过程也是解析请求的过程
			HttpRequest request = new HttpRequest(socket);
			// 实例化响应对象
			HttpResponse response = new HttpResponse(socket);
			
			// 2 处理请求
			// 先通过request获取用户请求的资源的抽象路径
			String path = request.getRequestURI();
			// 首先判断该请求路径是否为请求一个业务
			if("/myweb/reg".equals(path)){   // 即为请求注册业务
				RegServlet servlet = new RegServlet();
				servlet.service(request, response);
			}else{
				// 从webapps目录下根据该路径寻找请求资源
				File file = new File("./webapps"+path);
				if (file.exists()){
					System.out.println("该资源已找到");
					// 将要响应的资源设置到response中
					response.setEntity(file);
					
				}else{
					System.out.println("该资源不存在");
					// 响应404页面
					response.setEntity(new File("./webapps/root/404.html"));
									
					// 设置状态代码与描述
					response.setStausCode(404);
					response.setStatusReason("NOT FOUND");
				}
			}
		
			
			// 3 发送响应
			// 响应客户端
			response.flush();
			
		
			
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				// 处理完毕后与客户端断开释放资源
				socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
