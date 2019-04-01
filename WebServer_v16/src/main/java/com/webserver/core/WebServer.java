package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
	private ServerSocket server;

	// �̳߳�,���ڹ�����ͻ���������߳�
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
				System.out.println("�ȴ��ͻ�������...");
				Socket socket = server.accept();
				System.out.println("һ���ͻ���������!");

				// �����̴߳���ͻ��˽���
				ClientHandler handler = new ClientHandler(socket);
				// �����񽻸��̳߳ش���
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
