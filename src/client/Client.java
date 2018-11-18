package client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import com.sun.javafx.fxml.BeanAdapter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;

public class Client {

	
	
	// network
	private String host="localhost";
	private int port=8000;
	private Socket socket;
	Socket[]sockets=new Socket[1000];
	BufferedReader br;
	PrintWriter pw;

	private PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut=socket.getOutputStream();	
		return new PrintWriter(socketOut, true);
	}

	private BufferedReader getReader(Socket socket)throws IOException {
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));

	}
	
	

	
	// frame
	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client client = new Client();
					client.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Client() throws UnknownHostException, IOException {
		initialize();
	}

	
	
	/**
	 * 窗口绘画
	 * @throws IOException 
	 */
	private void initialize() throws IOException {

		socket=new Socket(host,port);
		br=getReader(socket);
		pw=getWriter(socket);


		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {             //窗口关闭前关闭socket
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(109, 41, 210, 81);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = textField.getText();
				pw.println(sql);
				if(sql.contains("select")) {
					Integer x,y;
					try {
						String msg = br.readLine();
						if(msg.equals("success")) {
							x = Integer.valueOf(br.readLine());
							y = Integer.valueOf(br.readLine());
							for(int i=0;i<x;i++) {
								for(int j=0;j<y;j++) {
									System.out.print(br.readLine()+"...");
								}
								System.out.println();
							}
						}
						else {
							System.out.println(msg);
						}
					} catch (NumberFormatException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					
				}
				else {
					String msg;
					try {
						msg = br.readLine();
						System.out.println(msg);
					} catch (IOException e1) {
					}
				}
					
				
			}
		});
		btnNewButton.setBounds(152, 158, 113, 27);
		frame.getContentPane().add(btnNewButton);
	}

}
