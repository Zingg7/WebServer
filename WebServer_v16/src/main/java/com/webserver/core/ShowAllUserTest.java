package com.webserver.core;

import java.io.RandomAccessFile;
import java.net.Socket;

public class ShowAllUserTest {
	public ShowAllUserTest(Socket socket){
		try(RandomAccessFile raf = new RandomAccessFile("user.dat", "rw")){
			for(int i = 0; i < raf.length()/100; i++){
				// 读取用户名
				byte[] data = new byte[32];
				raf.read(data);
				String username = new String(data, "utf-8").trim();
				
				raf.read(data);
				String password = new String(data, "utf-8").trim();
				
				raf.read(data);
				String nickname = new String(data, "utf-8").trim();
				
				int age = raf.readInt();
				
				System.out.println("username: "+ username + "\npassword: "+ password +"\nnickname: " + nickname + "\nage: "+ age);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
