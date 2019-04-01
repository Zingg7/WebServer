package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * ���ڴ���ͻ�������
 * 
 * @author soft01
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// ��ȡ�ͻ��˷��͹���������
			InputStream in = socket.getInputStream();
			int d = -1;
			while ((d = in.read()) != -1) {
				System.out.print((char) d);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// ������Ϻ���ͻ��˶Ͽ��ͷ���Դ
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
