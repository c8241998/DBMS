package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CMenu {
	//�˿ں�IP
	private String host="localhost";
	private int port=8000;
	private Socket socket;
	//menu��ť
	private JMenuBar menuBar;
	private JMenu mnNewMenu1;
	private JMenu mnNewMenu2;
	private JMenu mnNewMenu3;
	private JMenu mnNewMenu4;
	private JMenu mnNewMenu5;
	private JMenuItem mntmNewMenuItem;
	//����
	private PrintWriter pw;
	private BufferedReader br;
	private JPanel commandPanel;
	private JPanel TreePanel;
	private JPanel tablePanel;
	//�жϰ�ť
	private boolean connected=false;
	//����
	private Tree tree;
	//��������
	private Command command;
	public CMenu(JMenuBar menuBar,Socket socket,final PrintWriter pw,final BufferedReader br,JPanel commandPanel,JPanel TreePanel,JPanel tablePanel){
		this.menuBar=menuBar;
		this.pw=pw;
		this.br=br;
		this.socket=socket;
		this.commandPanel=commandPanel;
		this.TreePanel=TreePanel;
		this.tablePanel=tablePanel;
	}
	
	private PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut=socket.getOutputStream();	
		return new PrintWriter(new OutputStreamWriter(socketOut,"GB18030"), true);
	}

	private BufferedReader getReader(Socket socket)throws IOException {
		InputStream socketIn=socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn,"GB18030"));

	}
	
	public void setMenu(){
		//��һ��menu��ǩ
		mnNewMenu1 = new JMenu("������");
		menuBar.add(mnNewMenu1);
		
		mntmNewMenuItem = new JMenuItem("���ӷ�����");
		
		
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					if(!connected){
						//���ӷ�����
						socket=new Socket(host,port);
						br=getReader(socket);
						pw=getWriter(socket);
						connected=true;
					
						//����
						tree=new Tree(TreePanel,tablePanel,pw, br);
						tree.setTree();
						tree.setListener();
						tree.setTreeMenu();
						//��������
						command=new Command(commandPanel,pw,br);
						command.setCommand();
					}
						
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"����������ʧ��","Message",JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		mnNewMenu1.add(mntmNewMenuItem);
		
		mntmNewMenuItem = new JMenuItem("�������ݿ�");
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){	
				try {
					pw.println("copy");
					String get=br.readLine();
					if(get.equals("success")){
						JOptionPane.showMessageDialog(null,"���ݳɹ�","����",JOptionPane.PLAIN_MESSAGE);
					}
					else
						JOptionPane.showMessageDialog(null,"����ʧ��","����",JOptionPane.PLAIN_MESSAGE);
				} catch (Exception e1) {
				}
			}
		});
		mnNewMenu1.add(mntmNewMenuItem);
		
//		mntmNewMenuItem = new JMenuItem("����");
//		mntmNewMenuItem.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				@SuppressWarnings("unused")
//				String inputValue = JOptionPane.showInputDialog("Please input the table name");
//				pw.println(inputValue);
//				String ttt;
//				try {
//					ttt = br.readLine();
//					JOptionPane.showConfirmDialog(null, ttt, ttt, JOptionPane.YES_NO_OPTION);
//				} catch (IOException e1) {
//				}	
//			}
//		});
//		mnNewMenu1.add(mntmNewMenuItem);
		
		//�ڶ�����ǩ
		mnNewMenu2 = new JMenu("���ݿ�");
		menuBar.add(mnNewMenu2);
		
		mntmNewMenuItem = new JMenuItem("�½����ݿ�");
		mnNewMenu2.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
				tree.pop_tree_newdb.doClick();
				}catch(Exception e1){
					
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("ɾ�����ݿ�");
		mnNewMenu2.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_db_del.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("�л����ݿ�");
		mnNewMenu2.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					if(pw==null)
						throw new Exception();
					CDatabase cdb=new CDatabase(null);
					String inputValue = JOptionPane.showInputDialog("Please input the database name");
					if(inputValue!=null){
						String message=cdb.exchangDatabase(inputValue, pw, br);//�л����ݿ�
						JOptionPane.showMessageDialog(null,message,"Message",JOptionPane.PLAIN_MESSAGE);
					}
					}catch(Exception e1){
					}
				
			}
		});
		mntmNewMenuItem = new JMenuItem("������");
		mnNewMenu2.add(mntmNewMenuItem);
		
		//������menu��ǩ
		mnNewMenu3 = new JMenu("��");
		menuBar.add(mnNewMenu3);
		
		mntmNewMenuItem = new JMenuItem("������");
		mnNewMenu3.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_db_newTable.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("ɾ����");
		mnNewMenu3.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_table_del.doClick();
					}catch(Exception e1){
				}
			}
		});
		
		mntmNewMenuItem = new JMenuItem("������");
		mnNewMenu3.add(mntmNewMenuItem);
//		mntmNewMenuItem.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				try{
//					CDatabase cdb=new CDatabase(null);
//					String inputValue = JOptionPane.showInputDialog("Please input the new table name");
//					//String message=cdb.exchangDatabase(inputValue, pw, br);//�л����ݿ�
//					JOptionPane.showMessageDialog(null,"success","Message",JOptionPane.PLAIN_MESSAGE);
//					}catch(Exception e1){
//				}
//			}
//		});
		
		//���ĸ�menu��ǩ
		mnNewMenu4 = new JMenu("�ֶ�");
		menuBar.add(mnNewMenu4);
		
		mntmNewMenuItem = new JMenuItem("���ֶ�");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_table_newProperty.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("ɾ���ֶ�");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_property_del.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("�޸�����");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("������");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("�޸�Լ��");
		mnNewMenu4.add(mntmNewMenuItem);
		
		//�����menu��ǩ
		mnNewMenu5 = new JMenu("����");
		menuBar.add(mnNewMenu5);
		
		mntmNewMenuItem = new JMenuItem("ʹ��˵��");
		mnNewMenu5.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("����������");
		mnNewMenu5.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("��ϵ����");
		mnNewMenu5.add(mntmNewMenuItem);
	}
	

	
	public boolean getConnected(){
		return connected;
	}
	
	public void connectedClose(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
