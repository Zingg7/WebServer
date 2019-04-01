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
				System.out.println("�ȴ��ͻ�������...");
				Socket socket = server.accept();
				System.out.println("һ���ͻ���������!");

				// �����̴߳���ͻ��˽���
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
