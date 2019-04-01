package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
	private ServerSocket server;

	// 线程池,用于管理处理客户端请求的线程
	private ExecutorService threadPool;

	public WebServer() {
		
		try {
			server = new ServerSocket(8088);
			threadPool = Executors.newFixedThreadPool(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void start() {

		try {
			while (true) {
				System.out.println("等待客户端链接...");
				Socket socket = server.accept();
				System.out.println("一个客户端链接了!");

				// 启动线程处理客户端交互
				ClientHandler handler = new ClientHandler(socket);
				// 将任务交给线程池处理
				threadPool.execute(handler);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}
