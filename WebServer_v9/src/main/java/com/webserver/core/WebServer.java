package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	private ServerSocket server;

	public WebServer() {

		try {
			server = new ServerSocket(8088);
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
				Thread t = new Thread(handler);
				t.start();

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
