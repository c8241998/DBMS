package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;




public class Test {
	
	private static int port=8000;
	private static ServerSocket serverSocket;
	
	private static PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut=socket.getOutputStream();	
		return new PrintWriter(socketOut, true);//参数为true表示每写一行，PrintWriter缓存就自动溢出，把数据写到目的
	}
 	
	private static BufferedReader getReader(Socket socket)throws IOException {
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
		
	}
	
	public static void main(String[] args) {
	
		try {
			serverSocket=new ServerSocket(port);
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
		while (true) {
			Socket socket=null;
			try {
				socket=serverSocket.accept();
				BufferedReader br = getReader(socket);
				PrintWriter pw = getWriter(socket);
				br.readLine();
				pw.println("hello");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		}
		// TODO 自动生成的方法存根
	}

}
