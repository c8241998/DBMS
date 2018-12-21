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
	//端口和IP
	private String host="localhost";
	private int port=8000;
	private Socket socket;
	//menu按钮
	private JMenuBar menuBar;
	private JMenu mnNewMenu1;
	private JMenu mnNewMenu2;
	private JMenu mnNewMenu3;
	private JMenu mnNewMenu4;
	private JMenu mnNewMenu5;
	private JMenuItem mntmNewMenuItem;
	//传参
	private PrintWriter pw;
	private BufferedReader br;
	private JPanel commandPanel;
	private JPanel TreePanel;
	private JPanel tablePanel;
	//判断按钮
	private boolean connected=false;
	//建树
	private Tree tree;
	//建命令行
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
		//第一个menu标签
		mnNewMenu1 = new JMenu("服务器");
		menuBar.add(mnNewMenu1);
		
		mntmNewMenuItem = new JMenuItem("连接服务器");
		
		
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					if(!connected){
						//连接服务器
						socket=new Socket(host,port);
						br=getReader(socket);
						pw=getWriter(socket);
						connected=true;
					
						//建树
						tree=new Tree(TreePanel,tablePanel,pw, br);
						tree.setTree();
						tree.setListener();
						tree.setTreeMenu();
						//建命令行
						command=new Command(commandPanel,pw,br);
						command.setCommand();
					}
						
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"服务器连接失败","Message",JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		mnNewMenu1.add(mntmNewMenuItem);
		
		mntmNewMenuItem = new JMenuItem("备份数据库");
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){	
				try {
					pw.println("copy");
					String get=br.readLine();
					if(get.equals("success")){
						JOptionPane.showMessageDialog(null,"备份成功","错误",JOptionPane.PLAIN_MESSAGE);
					}
					else
						JOptionPane.showMessageDialog(null,"备份失败","错误",JOptionPane.PLAIN_MESSAGE);
				} catch (Exception e1) {
				}
			}
		});
		mnNewMenu1.add(mntmNewMenuItem);
		
//		mntmNewMenuItem = new JMenuItem("测试");
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
		
		//第二个标签
		mnNewMenu2 = new JMenu("数据库");
		menuBar.add(mnNewMenu2);
		
		mntmNewMenuItem = new JMenuItem("新建数据库");
		mnNewMenu2.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
				tree.pop_tree_newdb.doClick();
				}catch(Exception e1){
					
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("删除数据库");
		mnNewMenu2.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_db_del.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("切换数据库");
		mnNewMenu2.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					if(pw==null)
						throw new Exception();
					CDatabase cdb=new CDatabase(null);
					String inputValue = JOptionPane.showInputDialog("Please input the database name");
					if(inputValue!=null){
						String message=cdb.exchangDatabase(inputValue, pw, br);//切换数据库
						JOptionPane.showMessageDialog(null,message,"Message",JOptionPane.PLAIN_MESSAGE);
					}
					}catch(Exception e1){
					}
				
			}
		});
		mntmNewMenuItem = new JMenuItem("重命名");
		mnNewMenu2.add(mntmNewMenuItem);
		
		//第三个menu标签
		mnNewMenu3 = new JMenu("表");
		menuBar.add(mnNewMenu3);
		
		mntmNewMenuItem = new JMenuItem("创建表");
		mnNewMenu3.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_db_newTable.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("删除表");
		mnNewMenu3.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_table_del.doClick();
					}catch(Exception e1){
				}
			}
		});
		
		mntmNewMenuItem = new JMenuItem("重命名");
		mnNewMenu3.add(mntmNewMenuItem);
//		mntmNewMenuItem.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				try{
//					CDatabase cdb=new CDatabase(null);
//					String inputValue = JOptionPane.showInputDialog("Please input the new table name");
//					//String message=cdb.exchangDatabase(inputValue, pw, br);//切换数据库
//					JOptionPane.showMessageDialog(null,"success","Message",JOptionPane.PLAIN_MESSAGE);
//					}catch(Exception e1){
//				}
//			}
//		});
		
		//第四个menu标签
		mnNewMenu4 = new JMenu("字段");
		menuBar.add(mnNewMenu4);
		
		mntmNewMenuItem = new JMenuItem("新字段");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_table_newProperty.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("删除字段");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					tree.pop_tree_property_del.doClick();
					}catch(Exception e1){
				}
			}
		});
		mntmNewMenuItem = new JMenuItem("修改类型");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("重命名");
		mnNewMenu4.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("修改约束");
		mnNewMenu4.add(mntmNewMenuItem);
		
		//第五个menu标签
		mnNewMenu5 = new JMenu("关于");
		menuBar.add(mnNewMenu5);
		
		mntmNewMenuItem = new JMenuItem("使用说明");
		mnNewMenu5.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("服务器介绍");
		mnNewMenu5.add(mntmNewMenuItem);
		mntmNewMenuItem = new JMenuItem("联系我们");
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
