package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;

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
			System.out.println("Show All Users:");
			ShowAllUserTest show = new ShowAllUserTest(socket);
			// 2 处理请求                                                                                                                                         
			// 先通过request获取用户请求的资源的抽象路径
			String path = request.getRequestURI();
			// 首先判断该请求路径是否为请求一个业务,根据请求路径取ServerContext提取对应的Servlet
			// 若返回值不是null,则说明该请求时请求一个业务,那么久调用对应Servlet的service方法
			HttpServlet servlet = ServerContext.getServlet(path);
			if (servlet != null){ // 当为静态页面时,在ServerContext中找不到对应的处理办法,servlet返回null
				servlet.service(request, response);
			}
			else{
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
			
		} catch (EmptyRequestException e){
			// 这里单独捕获空请求异常,如果ClentHandler在一开始实例化请求对象HttpRequest时,该构造方法抛出了空请求异常
			// 那么会直接跳到catch这里,这样就灯虎忽略了ClientHandler后续应该有的处理请求和响应客户端的操作了
			System.out.println("空请求");
		} catch (Exception e){
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
