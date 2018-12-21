package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import client_DatabaseTree.DataBase;
import client_DatabaseTree.DbList;
import client_DatabaseTree.Property;
import client_DatabaseTree.Table;

public class Tree {
	//主页面传参
	    private JPanel TreePanel;
	    private JPanel contentPanel;
	    private JPanel tablePanel;
	    private PrintWriter pw;
	    private BufferedReader br;
	//方法调用
	    private DbList dblist=new DbList();
		private CDatabase cdb = new CDatabase(dblist);
		private CTable ctb = new CTable(dblist);
		private CProperty cpt =new CProperty(dblist);
		private JTree tree= new JTree();
	//树右键菜单
		private TreeMenu treeMenu;
		private JTable table;
		private JButton btnadd=new JButton("增加数据");
		private JButton btndelete=new JButton("删除数据");
		//空白处
		private JPopupMenu popMenu_tree_newdb;
		JMenuItem pop_tree_newdb;
		JMenuItem pop_tree_refreshdb;
		//数据库
		private JPopupMenu popMenu_tree_db;
		JMenuItem pop_tree_db_newTable;
		JMenuItem pop_tree_db_del;
		JMenuItem pop_tree_db_rename;
		JMenuItem pop_tree_db_reRead;
		JMenuItem pop_tree_db_readSQLFile;
		//表
		private JPopupMenu popMenu_tree_table;
		JMenuItem pop_tree_table_newProperty;
		JMenuItem pop_tree_table_del;
		JMenuItem pop_tree_table_rename;
		//字段
		private JPopupMenu popMenu_tree_property;
		JMenuItem pop_tree_property_set;
		JMenuItem pop_tree_property_del;
		JMenuItem pop_tree_property_rename;
			
		public Tree(JPanel TreePanel,JPanel tablePanel,PrintWriter pw,BufferedReader br){
			this.TreePanel=TreePanel;
			this.tablePanel=tablePanel;
			this.pw=pw;
			this.br=br;
			//树
			popMenu_tree_newdb=new JPopupMenu();
			pop_tree_newdb=new JMenuItem("新建数据库");
			popMenu_tree_newdb.add(pop_tree_newdb);
			pop_tree_refreshdb=new JMenuItem("刷新");
			popMenu_tree_newdb.add(pop_tree_refreshdb);
			
			popMenu_tree_db=new JPopupMenu();
			pop_tree_db_newTable=new JMenuItem("新建表");
			pop_tree_db_del=new JMenuItem("删除数据库");
			pop_tree_db_rename=new JMenuItem("重命名");
			pop_tree_db_reRead=new JMenuItem("刷新");
			pop_tree_db_readSQLFile=new JMenuItem("运行SQL文件");
			popMenu_tree_db.add(pop_tree_db_newTable);
			popMenu_tree_db.add(pop_tree_db_del);
			popMenu_tree_db.add(pop_tree_db_rename);
			popMenu_tree_db.add(pop_tree_db_reRead);
			popMenu_tree_db.add(pop_tree_db_readSQLFile);
			
			popMenu_tree_table=new JPopupMenu();
			pop_tree_table_newProperty=new JMenuItem("新建字段");
			pop_tree_table_del=new JMenuItem("删除表");
			pop_tree_table_rename=new JMenuItem("重命名");
			popMenu_tree_table.add(pop_tree_table_newProperty);
			popMenu_tree_table.add(pop_tree_table_del);
			popMenu_tree_table.add(pop_tree_table_rename);
			
			popMenu_tree_property=new JPopupMenu();
			pop_tree_property_set=new JMenuItem("修改属性");
			pop_tree_property_del=new JMenuItem("删除字段");
			pop_tree_property_rename=new JMenuItem("重命名");
			popMenu_tree_property.add(pop_tree_property_set);
			popMenu_tree_property.add(pop_tree_property_del);
			popMenu_tree_property.add(pop_tree_property_rename);
			
		}
	
		public void setTree() {
			treeMenu=new TreeMenu(cdb,ctb,cpt,dblist,tree);
			tree.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			treeMenu.setTree();
			tree.setEditable(false);
			this.tree.setToggleClickCount(1);
			this.tree.setRootVisible(false);
			this.tree.addMouseListener(new MouseAdapter() {
				 
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					//super.mouseReleased(e);
//					System.out.println("点击");
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					tree.setSelectionPath(path);
					DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					if(e.getClickCount()==2){
//						System.out.println("双击");
						try{
							if(selectNode.getLevel()==2) {//如果双击表节点 打开表	
								cdb.exchangDatabase(selectNode.getParent().toString(), pw, br);//切换数据库
								//插入表
								contentTable ctable=new contentTable(tablePanel,cpt,pw, br);
								
								int childNum = selectNode.getChildCount();
								String[] headers = new String[childNum];
								for(int i=0;i<childNum;i++){
									headers[i]=selectNode.getChildAt(i).toString();
								}
								String[][] content=cpt.select(selectNode.toString(), "*", null, pw, br);
								ctable.setTable(headers,content,selectNode.toString());
							}
						}
						catch(Exception e1){
						}
						
							
					}
					else if (e.getClickCount()==1&&e.getButton() == MouseEvent.BUTTON3) { 
//						System.out.println("右键");
						if(selectNode!=null) {
							if(selectNode.getLevel()==1) {//0是根节点 1是数据库 2是表 3是列
								popMenu_tree_db.show(TreePanel, e.getX(), e.getY()+30);
							}
							else if(selectNode.getLevel()==2) {//0是根节点 1是数据库 2是表 3是列
								popMenu_tree_table.show(TreePanel, e.getX(), e.getY()+30);
							}
							else if(selectNode.getLevel()==3) {//0是根节点 1是数据库 2是表 3是列
								popMenu_tree_property.show(TreePanel, e.getX(), e.getY()+30);
							}
						}
						else {
							popMenu_tree_newdb.show(TreePanel, e.getX(), e.getY()+30);
						}
							
	                }
					else if(e.getClickCount()==1) {

						try{
//							if(selectNode.getLevel()==1)
//								treeMenu.getTable(pw, br);
//							if(selectNode.getLevel()==2){
//								String tableName=selectNode.toString();
//								treeMenu.getProperty(tableName, pw, br);
//							}
						}
						catch(Exception e1){
							
						}
					}
					
				}
				
			});
			TreePanel.add(tree, BorderLayout.CENTER);
		}
		
//		public void test() {
//				Property<Integer> id=new Property<Integer>("id","int");
//				Table student =new Table("student");
//				DataBase db=new DataBase("ggg");
//				List<Integer> list=new ArrayList<Integer>();
//				list.add(1630);
//				id.setContent(list);
//				student.addProperty(id);
//				db.addTable(student);
//				dblist.addDb(db);
//			}
		
		
		public void setListener(){
			pop_tree_newdb.addActionListener(treeMenu.newdb_mouseListner(pw,br));//新建数据库
			pop_tree_refreshdb.addActionListener(new ActionListener(){//刷新树表
				@Override
				public void actionPerformed(ActionEvent e) {
					refreshTree();
					
				}
			});
			pop_tree_db_newTable.addActionListener(treeMenu.newTableFrame_mouseListner(treeMenu,pw,br));//新建表
			pop_tree_db_del.addActionListener(treeMenu.deleteDb_mouseListner(pw,br));//删除数据库
//			pop_tree_db_rename.addMouseListener(treeMenu.renameDb_mouseListner(pw,br));//数据库重命名
			pop_tree_db_reRead.addMouseListener(treeMenu.reReadDb_mouseListner());//刷新数据库
//			pop_tree_db_readSQLFile.addMouseListener(treeMenu.readSQL_mouseListner());//运行sql文件
			pop_tree_table_newProperty.addActionListener(treeMenu.newPropertyFrame_actionListner(treeMenu,pw,br));//新建字段
			pop_tree_table_del.addActionListener(treeMenu.deleteTable_mouseListner(pw,br));//删除表
			pop_tree_table_rename.addMouseListener(treeMenu.renameTable_mouseListner(pw,br));//表重命名
//			pop_tree_property_set.addMouseListener(treeMenu.setProperty_mouseListner(tree));//修改字段属性
			pop_tree_property_del.addActionListener(treeMenu.deleteProperty_mouseListner(pw,br));//删除字段
	//		pop_tree_property_rename.addMouseListener(treeMenu.renameProperty_mouseListner(tree));//字段重命名
			treeMenu.setTree();
		}
		
		public void refreshTree(){
			TreePanel.removeAll();
			tablePanel.removeAll();
			TreePanel.repaint();
			//建树
			dblist=new DbList();
			setTree();
			setListener();
			setTreeMenu();
		}
		
		public void setTreeMenu(){
			treeMenu.getDatabase(pw, br);
		}
		
		public TreeMenu getTreeMenu(){
			return treeMenu;
		}
}
