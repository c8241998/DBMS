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
	//��ҳ�洫��
	    private JPanel TreePanel;
	    private JPanel contentPanel;
	    private JPanel tablePanel;
	    private PrintWriter pw;
	    private BufferedReader br;
	//��������
	    private DbList dblist=new DbList();
		private CDatabase cdb = new CDatabase(dblist);
		private CTable ctb = new CTable(dblist);
		private CProperty cpt =new CProperty(dblist);
		private JTree tree= new JTree();
	//���Ҽ��˵�
		private TreeMenu treeMenu;
		private JTable table;
		private JButton btnadd=new JButton("��������");
		private JButton btndelete=new JButton("ɾ������");
		//�հ״�
		private JPopupMenu popMenu_tree_newdb;
		JMenuItem pop_tree_newdb;
		JMenuItem pop_tree_refreshdb;
		//���ݿ�
		private JPopupMenu popMenu_tree_db;
		JMenuItem pop_tree_db_newTable;
		JMenuItem pop_tree_db_del;
		JMenuItem pop_tree_db_rename;
		JMenuItem pop_tree_db_reRead;
		JMenuItem pop_tree_db_readSQLFile;
		//��
		private JPopupMenu popMenu_tree_table;
		JMenuItem pop_tree_table_newProperty;
		JMenuItem pop_tree_table_del;
		JMenuItem pop_tree_table_rename;
		//�ֶ�
		private JPopupMenu popMenu_tree_property;
		JMenuItem pop_tree_property_set;
		JMenuItem pop_tree_property_del;
		JMenuItem pop_tree_property_rename;
			
		public Tree(JPanel TreePanel,JPanel tablePanel,PrintWriter pw,BufferedReader br){
			this.TreePanel=TreePanel;
			this.tablePanel=tablePanel;
			this.pw=pw;
			this.br=br;
			//��
			popMenu_tree_newdb=new JPopupMenu();
			pop_tree_newdb=new JMenuItem("�½����ݿ�");
			popMenu_tree_newdb.add(pop_tree_newdb);
			pop_tree_refreshdb=new JMenuItem("ˢ��");
			popMenu_tree_newdb.add(pop_tree_refreshdb);
			
			popMenu_tree_db=new JPopupMenu();
			pop_tree_db_newTable=new JMenuItem("�½���");
			pop_tree_db_del=new JMenuItem("ɾ�����ݿ�");
			pop_tree_db_rename=new JMenuItem("������");
			pop_tree_db_reRead=new JMenuItem("ˢ��");
			pop_tree_db_readSQLFile=new JMenuItem("����SQL�ļ�");
			popMenu_tree_db.add(pop_tree_db_newTable);
			popMenu_tree_db.add(pop_tree_db_del);
			popMenu_tree_db.add(pop_tree_db_rename);
			popMenu_tree_db.add(pop_tree_db_reRead);
			popMenu_tree_db.add(pop_tree_db_readSQLFile);
			
			popMenu_tree_table=new JPopupMenu();
			pop_tree_table_newProperty=new JMenuItem("�½��ֶ�");
			pop_tree_table_del=new JMenuItem("ɾ����");
			pop_tree_table_rename=new JMenuItem("������");
			popMenu_tree_table.add(pop_tree_table_newProperty);
			popMenu_tree_table.add(pop_tree_table_del);
			popMenu_tree_table.add(pop_tree_table_rename);
			
			popMenu_tree_property=new JPopupMenu();
			pop_tree_property_set=new JMenuItem("�޸�����");
			pop_tree_property_del=new JMenuItem("ɾ���ֶ�");
			pop_tree_property_rename=new JMenuItem("������");
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
//					System.out.println("���");
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					tree.setSelectionPath(path);
					DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					if(e.getClickCount()==2){
//						System.out.println("˫��");
						try{
							if(selectNode.getLevel()==2) {//���˫����ڵ� �򿪱�	
								cdb.exchangDatabase(selectNode.getParent().toString(), pw, br);//�л����ݿ�
								//�����
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
//						System.out.println("�Ҽ�");
						if(selectNode!=null) {
							if(selectNode.getLevel()==1) {//0�Ǹ��ڵ� 1�����ݿ� 2�Ǳ� 3����
								popMenu_tree_db.show(TreePanel, e.getX(), e.getY()+30);
							}
							else if(selectNode.getLevel()==2) {//0�Ǹ��ڵ� 1�����ݿ� 2�Ǳ� 3����
								popMenu_tree_table.show(TreePanel, e.getX(), e.getY()+30);
							}
							else if(selectNode.getLevel()==3) {//0�Ǹ��ڵ� 1�����ݿ� 2�Ǳ� 3����
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
			pop_tree_newdb.addActionListener(treeMenu.newdb_mouseListner(pw,br));//�½����ݿ�
			pop_tree_refreshdb.addActionListener(new ActionListener(){//ˢ������
				@Override
				public void actionPerformed(ActionEvent e) {
					refreshTree();
					
				}
			});
			pop_tree_db_newTable.addActionListener(treeMenu.newTableFrame_mouseListner(treeMenu,pw,br));//�½���
			pop_tree_db_del.addActionListener(treeMenu.deleteDb_mouseListner(pw,br));//ɾ�����ݿ�
//			pop_tree_db_rename.addMouseListener(treeMenu.renameDb_mouseListner(pw,br));//���ݿ�������
			pop_tree_db_reRead.addMouseListener(treeMenu.reReadDb_mouseListner());//ˢ�����ݿ�
//			pop_tree_db_readSQLFile.addMouseListener(treeMenu.readSQL_mouseListner());//����sql�ļ�
			pop_tree_table_newProperty.addActionListener(treeMenu.newPropertyFrame_actionListner(treeMenu,pw,br));//�½��ֶ�
			pop_tree_table_del.addActionListener(treeMenu.deleteTable_mouseListner(pw,br));//ɾ����
			pop_tree_table_rename.addMouseListener(treeMenu.renameTable_mouseListner(pw,br));//��������
//			pop_tree_property_set.addMouseListener(treeMenu.setProperty_mouseListner(tree));//�޸��ֶ�����
			pop_tree_property_del.addActionListener(treeMenu.deleteProperty_mouseListner(pw,br));//ɾ���ֶ�
	//		pop_tree_property_rename.addMouseListener(treeMenu.renameProperty_mouseListner(tree));//�ֶ�������
			treeMenu.setTree();
		}
		
		public void refreshTree(){
			TreePanel.removeAll();
			tablePanel.removeAll();
			TreePanel.repaint();
			//����
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
