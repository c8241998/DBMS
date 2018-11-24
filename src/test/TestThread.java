package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.jb2011.lnf.beautyeye.resources.beautyeye;

public class TestThread implements Runnable {
	
	private String host="localhost";
	private int port=8000;
	
	private PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut=socket.getOutputStream();	
		return new PrintWriter(socketOut, true);
	}

	private BufferedReader getReader(Socket socket)throws IOException {
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));

	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		Socket socket;
		try {
			socket = new Socket(host,port);
			BufferedReader br=getReader(socket);
			PrintWriter pw=getWriter(socket);
			pw.println("123");
			br.readLine();
//			System.out.println(br.readLine());
			pw.close();
			br.close();
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}

}
