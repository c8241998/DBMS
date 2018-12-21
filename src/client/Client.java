package client;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class Client extends JFrame{

	// network
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private CMenu cm;
		
	// frame
	private JPanel contentPanel;
	private JPanel rightPanel;
	private JPanel TreePanel;
	private JPanel tablePanel;
	private JPanel commandPanel;
	private JMenuBar menuBar;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
					Client client = new Client();
					client.setVisible(true);
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

		addWindowListener(new WindowAdapter() {             //窗口关闭前关闭socket
			@Override
			public void windowClosing(WindowEvent e) {
				if(cm.getConnected())
					cm.connectedClose();
			}
		});
		setTitle("Database Management System ");
		setBounds(100, 100, 1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentPanel = new JPanel();
		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		rightPanel=new JPanel();
		contentPanel.add(rightPanel, BorderLayout.CENTER);
		rightPanel.setLayout(new BorderLayout(0, 0));
		
		TreePanel = new JPanel();
		contentPanel.add(TreePanel, BorderLayout.WEST);
		TreePanel.setLayout(new BorderLayout(0, 0));
		TreePanel.setPreferredSize(new Dimension(180, 700));//用来设置JPanel的大小
		
		tablePanel = new JPanel();
		rightPanel.add(tablePanel, BorderLayout.CENTER);
		tablePanel.setLayout(new BorderLayout(0, 0));
		tablePanel.setBackground(Color.white);
		
		commandPanel = new JPanel();
		rightPanel.add(commandPanel, BorderLayout.SOUTH);
		commandPanel.setLayout(new BorderLayout(0, 0));
		commandPanel.setPreferredSize(new Dimension(0, 200));//用来设置JPanel的大小
			
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);	
		cm=new CMenu(menuBar,socket,pw,br,commandPanel,TreePanel,tablePanel);
		cm.setMenu();
	}
}
