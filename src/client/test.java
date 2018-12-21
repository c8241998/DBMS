package client;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import client_DatabaseTree.DbList;

public class test extends JFrame{

	
	// network
	private Socket socket;
	private Socket[]sockets=new Socket[1000];
	private BufferedReader br;
	private PrintWriter pw;
	private DbList dblist=new DbList();
	private CDatabase cdb = new CDatabase(dblist);
	private CTable ctb = new CTable(dblist);
	private CProperty cpt =new CProperty(dblist);
	private CMenu cm;
	private Command command;
		
	// frame
	private JPanel contentPanel;
	private JPanel rightPanel;
	private JPanel TreePanel;
	private JPanel commandPanel;
	private JMenuBar menuBar;
	private JTree tree= new JTree();
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnNewButton;
	private JTextPane textPane;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test client = new test();
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
	public test() throws UnknownHostException, IOException {
		initialize();
	}

	/**
	 * ���ڻ滭
	 * @throws IOException 
	 */
	private void initialize() throws IOException {

		addWindowListener(new WindowAdapter() {             //���ڹر�ǰ�ر�socket
			@Override
			public void windowClosing(WindowEvent e) {
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
		TreePanel.setPreferredSize(new Dimension(180, 700));
		
		commandPanel = new JPanel();
		rightPanel.add(commandPanel, BorderLayout.SOUTH);
		commandPanel.setLayout(new BorderLayout(0, 0));
		commandPanel.setPreferredSize(new Dimension(0, 200));//��������JPanel�Ĵ�С
		
		textPane = new JTextPane();
		commandPanel.add(textPane, BorderLayout.CENTER);
			
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		table = new JTable();
		String[] columns={"ID","����","�Ա�"};//�˴���Ҫ����ֶ�����
		final DefaultTableModel model=new DefaultTableModel(columns,0);
		table.setModel(model);
		TableColumnModel columnModel=table.getColumnModel();
		int count=columnModel.getColumnCount();
		for(int i=0;i<count;i++){
			javax.swing.table.TableColumn column=columnModel.getColumn(i);
			column.setPreferredWidth(800/count);//�����еĿ��
			}
		model.addRow(new Object[]{16301064,"fdd","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301065,"zxc","Ů"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301125,"jgb","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301064,"fdd","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301065,"zxc","Ů"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301125,"jgb","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301064,"fdd","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301065,"zxc","Ů"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301125,"jgb","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301064,"fdd","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301065,"zxc","Ů"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		model.addRow(new Object[]{16301125,"jgb","��"});//�˴���Ҫһ��һ�ж����ݣ��ӽ�ȥ
		
		
		table.setShowHorizontalLines(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rightPanel.add(scrollPane, BorderLayout.CENTER);
		
		btnNewButton = new JButton("New button");
		scrollPane.add(btnNewButton);
		

		
		
		
//		cm.setMenu();
	}
}
