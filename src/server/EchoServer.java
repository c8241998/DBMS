package server;

import java.io.*;
import java.net.*;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import sql.AnalysisSQL;
 
public class EchoServer{
	private int port=8000;
	private ThreadPool threadPool;//�̳߳�
	private ServerSocket serverSocket;
	private final int POOL_SIZE=4;//����Cpuʱ�̳߳��й�������Ŀ
	
	public EchoServer()throws IOException{
		serverSocket=new ServerSocket(port);
		//�����̳߳�
		//Rumtime��availableProcessors()�������ص�ǰϵͳ��cup��Ŀ
		//ϵͳ��cpuԽ�࣬�̳߳��е���ĿҲԽ��
		threadPool=new ThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
		System.out.println("������������");
	}
	public void service(){
		while (true) {
			Socket socket=null;
			try {
				socket=serverSocket.accept();
				threadPool.execute(new Handler(socket));//����ͻ�ͨѶ�����񽻸��߳�
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	public static void main(String args[])throws IOException{
	
			new EchoServer().service();
	
		
	}
	
 
}
 
//�����뵥���ͻ�ͨѶ����
class Handler implements Runnable{
	private Socket socket;
	public Handler(Socket socket){
		this.socket=socket;
	}
	
	private PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut=socket.getOutputStream();	
		return new PrintWriter(socketOut, true);//����Ϊtrue��ʾÿдһ�У�PrintWriter������Զ������������д��Ŀ��
	}
 	
	private BufferedReader getReader(Socket socket)throws IOException {
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
		
	}
	

	
	
	public void run(){
		try {
			//�õ��ͻ��˵ĵ�ַ�Ͷ˿ں�
			System.out.println("New connection accepted"+socket.getInetAddress()+":"+socket.getPort());
			BufferedReader br=getReader(socket);
			PrintWriter pw=getWriter(socket);
			String msg=null;
			while ((msg=br.readLine())!=null) {        //���տͻ��˷��͵�sql���
				
				sql.Message message = new AnalysisSQL(msg).work();        //����sql���
				if(message.flag!=1) {
					pw.println(message.msg);          // �쳣
				}else {
					pw.println("success");          //sqlִ�гɹ�
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(socket!=null){
					socket.close();
				}
				
			} catch (IOException  e) {
				e.printStackTrace();
			}
		}
	}
}