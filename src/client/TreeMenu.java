package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import client_DatabaseTree.*;
import client_frames.AddPropertyFrame;
import client_frames.AddTableFrame;

public class TreeMenu {
	AddTableFrame frame;
	AddPropertyFrame addPropertyFrame;
	CDatabase cdb;
	CTable ctb;
	CProperty cpt;
	DbList dblist;
	JTree tree;
	public TreeMenu(CDatabase cdb,CTable ctb,CProperty cpt,DbList dblist,JTree tree) {
		this.cdb=cdb;
		this.ctb=ctb;
		this.cpt=cpt;
		this.dblist=dblist;
		this.tree=tree;
	}
	
	//刷新tree
	public int setTree() {//返回数据库个数int
		tree.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("\u670D\u52A1\u5668") {
					{	
						DefaultMutableTreeNode node_db;
						DefaultMutableTreeNode node_table;
						DefaultMutableTreeNode node_column;
						for(int i=0;i<dblist.getDbNum();i++) {//数据库节点
							node_db=new DefaultMutableTreeNode(dblist.getList().get(i).getName());
							for(int j=0;j<dblist.getList().get(i).getTableNum();j++) {//表节点
								node_table=new DefaultMutableTreeNode(dblist.getList().get(i).getDataBase().get(j).getName());
								for(int k=0;k<dblist.getList().get(i).getDataBase().get(j).getPropertyNum();k++) {//字段节点
									node_column=new DefaultMutableTreeNode(dblist.getList().get(i).getDataBase().get(j).getPropertyList().get(k).getName());
									node_table.add(node_column);
								}
								node_db.add(node_table);
							}
							add(node_db);
						}
					}
				}
			));				
		return dblist.getList().size();
	}
	
	//刷新数据库 ok
		public ActionListener refreshdb_mouseListner(final PrintWriter pw,final BufferedReader br) {
			ActionListener listener=new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
//					System.out.println("进入点击事件");
					
				}
			};
			return listener;
		}
	
	//新建数据库 ok
	public ActionListener newdb_mouseListner(final PrintWriter pw,final BufferedReader br) {
		ActionListener listener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name;
//				System.out.println("进入点击事件");
				while(true) {
					name=JOptionPane.showInputDialog(null,"请输入新建数据库的名字：\n","新建数据库",JOptionPane.PLAIN_MESSAGE); 
					if(name!=null) { 
						String string=cdb.createDatabase(name, pw, br);
						if(string.equals("success")) {
							DataBase new_db=new DataBase(name);
							new_db.setRead(true);
							dblist.addDb(new_db);
							setTree();
							break;
						}
						else {
							JOptionPane.showMessageDialog(null,string,"错误",JOptionPane.PLAIN_MESSAGE); 
							break;
						}
					}
					else return;
				}
			}
		};
		return listener;
	}
	
	//新建表 ok
	public ActionListener newTableFrame_mouseListner(final TreeMenu treeMenu,final PrintWriter pw,final BufferedReader br) {
		ActionListener listener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
		            public void run() {
		                try {
		                	DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		                	if(selectNode!=null&&selectNode.getLevel()==1){
		                		frame = new AddTableFrame(treeMenu,pw,br);
								frame.setLocation(830, 350);
								frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
								frame.setVisible(true);
		                	}	
		                	else 
		                		JOptionPane.showMessageDialog(null,"请先选择建表的数据库","错误",JOptionPane.PLAIN_MESSAGE);
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
		        });
			}
		};
		return listener;
	}
	
	//新建表窗口调用
	public MouseListener newTable_mouseListner(final PrintWriter pw,final BufferedReader br) {
		MouseListener listener=new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				String name;
//				System.out.println("进入点击事件");
				if(selectNode!=null&&selectNode.getLevel()==1){
					label:
						while(true) {
							String change=cdb.exchangDatabase(selectNode.toString(), pw, br);//切换数据库
							if(change.equals("success")) {
								String propertys;
								name=frame.getTableName();
								propertys=frame.getPropertyStr();
								frame.dispose();
								String string=ctb.createTable(selectNode.toString(), name, propertys, pw, br);
								if(string.equals("success")) {
									for(int i=0;i<dblist.getDbNum();i++) {
										if(dblist.getList().get(i).getName().equals(selectNode.toString())) {
											Table table=new Table(name);
											table.setRead(true);
											Property property;
											String[] str2;
											String[] strings=new String[2];
											str2=propertys.split(",");
											for(String temp:str2) {
												strings=temp.split(" ");
												if(!strings[0].equals("primary")&&!strings[1].contains("key")){
													property=new Property<>(strings[0], strings[1]);
													table.addProperty(property);
												}
											}
											dblist.getList().get(i).addTable(table);
											break;
										}
									}
									setTree();
									break;
								}
								else {
									JOptionPane.showMessageDialog(null,string,"错误",JOptionPane.PLAIN_MESSAGE); 
									break;
								}
							}
							else {
								JOptionPane.showMessageDialog(null,change,"错误",JOptionPane.PLAIN_MESSAGE); 
								continue label;
							}
						}
				}
			}
		};
		return listener;
	}
	//删除数据库 ok
	public ActionListener deleteDb_mouseListner(final PrintWriter pw,final BufferedReader br) {
		ActionListener listener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
//				System.out.println("进入点击事件");
				if(selectNode!=null&&selectNode.getLevel()==1){
					String string=cdb.dropDatabase(selectNode.toString(), pw, br);
					if(string.equals("success")) {
						dblist.deleteDb(selectNode.toString());
						JOptionPane.showMessageDialog(null,"删除成功！","删除数据库",JOptionPane.PLAIN_MESSAGE); 
						setTree();
					}
					else {
						JOptionPane.showMessageDialog(null,string,"错误",JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
			}
		};
		
		return listener;
	}

	//重新读入数据库
	public MouseListener reReadDb_mouseListner() {
		MouseListener listener=new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				setTree();
			}
		};
		return listener;
	}
	
	
	//新建字段 ok
		public ActionListener newPropertyFrame_actionListner(final TreeMenu treeMenu,final PrintWriter pw,final BufferedReader br) {
			ActionListener listener=new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					EventQueue.invokeLater(new Runnable() {
			            public void run() {
			                try {
			                	DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			                	if(selectNode!=null&&selectNode.getLevel()==2){
			                		addPropertyFrame = new AddPropertyFrame(treeMenu,pw,br);
				                	addPropertyFrame.setLocation(830, 350);
				                	addPropertyFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				                	addPropertyFrame.setVisible(true);
			                	}
			                	else{
			                		JOptionPane.showMessageDialog(null,"请先选择创建新字段的表","错误",JOptionPane.PLAIN_MESSAGE);
			                	}
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			            }
			        });
				}
			};
			return listener;
		}
			
		//新建字段窗口调用
		public ActionListener newProperty_actionListner(final PrintWriter pw,final BufferedReader br) {
			ActionListener listener=new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//选中某表
//					System.out.println("进入点击事件");
					String change=cdb.exchangDatabase(selectNode.getParent().toString(), pw, br);//切换数据库
					if(change.equals("success")) {
						String p_name=addPropertyFrame.getPropertyName();
						if(p_name.equals("")) {
							JOptionPane.showMessageDialog(null,"请输入字段名！","错误",JOptionPane.PLAIN_MESSAGE); 
							return;
						}
						String property=addPropertyFrame.getPropertyStr();
						String p_type=addPropertyFrame.getP_type();
						Boolean p_notnull=addPropertyFrame.getP_notnull();
						Boolean p_unique=addPropertyFrame.getP_unique();
//						System.out.println(property);
						addPropertyFrame.dispose();
						String string=cpt.add(selectNode.toString(),property, pw, br);
						if(string.equals("success")) {
							for(int i=0;i<dblist.getDbNum();i++) {
								if(dblist.getList().get(i).getName().equals(selectNode.getParent().toString())) {
									for(int j=0;j<dblist.getList().get(i).getTableNum();j++) {
										if(dblist.getList().get(i).getDataBase().get(j).getName().equals(selectNode.toString())) {
											for(int k=0;k<dblist.getList().get(i).getDataBase().get(j).getPropertyNum();k++) {
												if(dblist.getList().get(i).getDataBase().get(j).getProperty(p_name)!=null) {
													JOptionPane.showMessageDialog(null,"字段名重复！创建失败！","错误",JOptionPane.PLAIN_MESSAGE); 
													return;
												}
											}
											dblist.getList().get(i).getDataBase().get(j).addProperty(new Property(p_name, p_type));
											setTree();
											break;
										}
									}
									break;
								}
							}

						}
						else {
							JOptionPane.showMessageDialog(null,string,"错误",JOptionPane.PLAIN_MESSAGE); 
						}
					}
					else {
						JOptionPane.showMessageDialog(null,change,"错误",JOptionPane.PLAIN_MESSAGE); 
					}
				}
			};
			return listener;
		}
	
	//删除表 ok
	public ActionListener deleteTable_mouseListner(final PrintWriter pw,final BufferedReader br) {
		ActionListener listener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//选中表
//				System.out.println("进入点击事件");
				if(selectNode!=null&&selectNode.getLevel()==2){
					String change=cdb.exchangDatabase(selectNode.getParent().toString(), pw, br);//切换数据库
					if(change.equals("success")) {
						String string=ctb.dropTable(selectNode.toString(), pw, br);
						if(string.equals("success")) {
							for(int i=0;i<dblist.getDbNum();i++) {
								if(dblist.getList().get(i).getName().equals(selectNode.getParent().toString())) {//判断数据库
									for(int j=0;j<dblist.getList().get(i).getTableNum();j++) {
										if(dblist.getList().get(i).getDataBase().get(j).getName().equals(selectNode.toString())) {//判断表
											dblist.getList().get(i).deleteTable(selectNode.toString());
											JOptionPane.showMessageDialog(null,"删除成功！","删除表",JOptionPane.PLAIN_MESSAGE); 
											setTree();
											return;
										}
									}
								}
							}
						}
						else {
							JOptionPane.showMessageDialog(null,string,"错误",JOptionPane.PLAIN_MESSAGE); 
							return;
						}
					}
					else{
						JOptionPane.showMessageDialog(null,"change","错误",JOptionPane.PLAIN_MESSAGE); 
						return;
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"请先选中要删除的表","错误",JOptionPane.PLAIN_MESSAGE);
					return;
				}
			}
		};
		return listener;
	}
	//表重命名
	public MouseListener renameTable_mouseListner(final PrintWriter pw,final BufferedReader br) {
		MouseListener listener=new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//选中表
				String newTableName;
//				System.out.println("进入点击事件");
				label:
					while(true) {
						newTableName=JOptionPane.showInputDialog(null,"请输入重命名的表的名字：\n","表重命名",JOptionPane.PLAIN_MESSAGE);
						if(newTableName!=null) {
							String change=cdb.exchangDatabase(selectNode.getParent().toString(), pw, br);//切换数据库
							if(change.equals("success")) {
								String string=ctb.updateName(selectNode.toString(), newTableName, pw, br);
								if(string.equals("success")) {
									for(int i=0;i<dblist.getDbNum();i++) {
										if(dblist.getList().get(i).getName().equals(selectNode.getParent().toString())) {//判断数据库
											for(int j=0;j<dblist.getList().get(i).getTableNum();j++) {
												if(dblist.getList().get(i).getDataBase().get(j).getName().equals(selectNode.toString())) {
													dblist.getList().get(i).getDataBase().get(j).setName(newTableName);
													JOptionPane.showMessageDialog(null,"重命名成功！","表重命名",JOptionPane.PLAIN_MESSAGE); 
													setTree();
													return;
												}
											}
										}
									}	
								}
								else {
									JOptionPane.showMessageDialog(null,string,"错误",JOptionPane.PLAIN_MESSAGE); 
									continue label;
								}
							}
							else {
								JOptionPane.showMessageDialog(null,change,"错误",JOptionPane.PLAIN_MESSAGE); 
								continue label;
							}
						}
						else return;
					}
			}
		};
		return listener;
	}

	//删除字段 
	public ActionListener deleteProperty_mouseListner(final PrintWriter pw,final BufferedReader br) {
		ActionListener listener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//选中字段
//				System.out.println("进入点击事件");
				if(selectNode!=null&&selectNode.getLevel()==3){
					String change=cdb.exchangDatabase(selectNode.getParent().getParent().toString(), pw, br);//切换数据库
					if(change.equals("success")) {
						String string=cpt.dropColumn(selectNode.getParent().toString(), selectNode.toString(), pw, br);
						if(string.equals("success")) {
							for(int i=0;i<dblist.getDbNum();i++) {
		  						if(dblist.getList().get(i).getName().equals(selectNode.getParent().getParent().toString())) {
		  							for(int j=0;j<dblist.getList().get(i).getTableNum();j++) {
		  								if(dblist.getList().get(i).getDataBase().get(j).getName().equals(selectNode.getParent().toString())) {
		  									for(int k=0;k<dblist.getList().get(i).getDataBase().get(j).getPropertyNum();k++)
		  										if(dblist.getList().get(i).getDataBase().get(j).getPropertyList().get(k).getName().equals(selectNode.toString())) {
		  											dblist.getList().get(i).getDataBase().get(j).deleteProperty(selectNode.toString());
		  											JOptionPane.showMessageDialog(null,"删除成功！","删除字段",JOptionPane.PLAIN_MESSAGE); 
		  											setTree();
		  											return;
		  										}
		  								}
		  							}
		  						}
							}
						}
						else {
							JOptionPane.showMessageDialog(null,string,"错误",JOptionPane.PLAIN_MESSAGE); 
							return;
						}
					}
					else {
						JOptionPane.showMessageDialog(null,change,"错误",JOptionPane.PLAIN_MESSAGE); 
						return;
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"请正确选择字段","错误",JOptionPane.PLAIN_MESSAGE); 
					return;
				}
			}
		};
		return listener;
	}
	
	//获取数据库中的数据库个数及名称
	public void getDatabase(PrintWriter pw,BufferedReader br){
		pw.println("get database");
		String[] databaseName = null;
		try {
			int num=Integer.valueOf(br.readLine());
			databaseName=new String[num];
			for(int i=0;i<num;i++){
				databaseName[i]=br.readLine();				
				dblist.addDb(new DataBase(databaseName[i]));			
			}
			
			for(int i=0;i<num;i++){
				cdb.exchangDatabase(databaseName[i].toString(), pw, br);
				getTable(databaseName[i],pw,br);
			}
			setTree();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"出错了","错误",JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	//获取数据库中的表个数及名称
	public void getTable(String databaseName,PrintWriter pw,BufferedReader br){
		String[] tableName = null;
		String change="success";
		DataBase db = null;
		for(int i=0;i<dblist.getDbNum();i++) {
			if(dblist.getList().get(i).getName().equals(databaseName)) {
				db = dblist.getList().get(i);
				break;
			}
		}
		if(!db.getRead()){
			if(change.equals("success")){
				pw.println("get table");
				try {
					int num=Integer.valueOf(br.readLine());
//					System.out.println(num);
					tableName=new String[num];
					for(int i=0;i<num;i++){
						tableName[i]=br.readLine(); 
						db.addTable(new Table(tableName[i]));
					}
					for(int i=0;i<num;i++){
						getProperty(databaseName,tableName[i],pw,br);
					}
					db.setRead(true);
					setTree();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null,"出错了","错误",JOptionPane.PLAIN_MESSAGE);
				}
			}
		}
	}
		
	//获取数据库中的表中字段数
	public void getProperty(String databaseName,String tableName,PrintWriter pw,BufferedReader br){
		String change="success";
		Table table=null;
		for(int i=0;i<dblist.getDbNum();i++) {
			if(dblist.getList().get(i).getName().equals(databaseName)) {
				for(int j=0;j<dblist.getList().get(i).getTableNum();j++) {
					if(dblist.getList().get(i).getDataBase().get(j).getName().equals(tableName)) {
						table=dblist.getList().get(i).getDataBase().get(j);
						break;
					}
				}
				break;
			}
		}
		if(!table.getRead()){
			if(change.equals("success")){
				pw.println("get property on "+tableName);
				String[] propertyName = null;
				try {
					int num=Integer.valueOf(br.readLine());
//					System.out.println(num);
					propertyName=new String[num];
					for(int i=0;i<num;i++){
						propertyName[i]=br.readLine(); 
						table.addProperty(new Property(propertyName[i],"varchar"));
					}
					table.setRead(true);
					setTree();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null,"你出错了","错误",JOptionPane.PLAIN_MESSAGE);
				}
			}
		}
	}

}
