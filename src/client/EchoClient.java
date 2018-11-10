package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
	private String host="localhost";
	private int port=8000;
	private Socket socket;
	Socket[]sockets=new Socket[1000];
	public EchoClient()throws IOException{

		//		for(int i=0;i<1000;i++){
		socket=new Socket(host,port);
		//		}

	}
//	public static void main(String args[])throws IOException{
//		new EchoClient().talk();
//
//	}

	private PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut=socket.getOutputStream();	
		return new PrintWriter(socketOut, true);
	}

	private BufferedReader getReader(Socket socket)throws IOException {
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));

	}

	public void talk()throws IOException{
		try {
			BufferedReader br=getReader(socket);
			PrintWriter pw=getWriter(socket);
			BufferedReader localReader=new BufferedReader(new InputStreamReader(System.in));
			
//			Client.main(null);
			String msg=null;
			while ((msg=localReader.readLine())!=null) {
				pw.println(msg);
//				System.out.println(br.readLine());
//
//				if(msg.equals("bye")){
//					break;
//				}
			}


		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}

	}



}
