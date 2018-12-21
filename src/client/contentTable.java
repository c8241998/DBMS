package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
/**
 * 主要是用来把tablePanel的参数传进来，然后利用这里面的方法对tablePanel进行操作
 * @author Yang
 *
 */
public class contentTable {
	//初始变量
	private JPanel tablePanel;
	private JButton btnadd;
	private JButton btndelete;
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel model;
	private String[] header;
	private CProperty cpt;
	private String tableName;
	//构造函数
	public contentTable(JPanel tablePanel,final CProperty cpt,final PrintWriter pw,final BufferedReader br){
		this.cpt=cpt;
		this.tablePanel=tablePanel;
		btnadd=new JButton("增加数据");
		btndelete=new JButton("删除数据");
		btnadd.setPreferredSize(new Dimension(96, 40));
		btndelete.setPreferredSize(new Dimension(96, 40));
		//增加数据
		btnadd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				model.fireTableDataChanged();
				int rowNum=model.getRowCount();//行数
				//拼接字符串
				String typeStr="";
				String contentStr="";
				for(int i=0;i<model.getColumnCount();i++){
					typeStr+=header[i];
//					if(isNumeric(model.getValueAt(rowNum-1, i).toString()))
					contentStr+=model.getValueAt(rowNum-1, i).toString();
//					else
//						contentStr=contentStr+"\""+model.getValueAt(rowNum-1, i).toString()+"\"";
					if(i!=model.getColumnCount()-1){
						typeStr+=",";
						contentStr+=",";
					}
				}
//				System.out.println(typeStr);
//				System.out.println(contentStr);
				String message=cpt.insert(tableName, typeStr, contentStr, pw, br);
				if(message.equals("success")){
					//加一行空行用于添加数据
					String[] white=new String[header.length];
					for(int i=0;i<white.length;i++)
						white[i]="";
					model.addRow(white);
				}
				else{
					model.removeRow(model.getRowCount()-1);
					JOptionPane.showMessageDialog(null,message,"错误",JOptionPane.PLAIN_MESSAGE);
					//加一行空行用于添加数据
					String[] white=new String[header.length];
					for(int i=0;i<white.length;i++)
						white[i]="";
					model.addRow(white);
				}
				
			}
		});
		
		//删除数据
		btndelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int selectedRow = table.getSelectedRow();//获得选中行的索引
				model.fireTableDataChanged();
				int rowNum=model.getRowCount();//行数
//				System.out.println(selectedRow);
//				System.out.println(rowNum);
//				JOptionPane.showMessageDialog(null,rowNum,"删除数量",JOptionPane.PLAIN_MESSAGE);
//				JOptionPane.showMessageDialog(null,selectedRow,"删除索引",JOptionPane.PLAIN_MESSAGE);
				if(selectedRow!=-1&&selectedRow!=(rowNum-1)) //存在选中行
				{
					String limit="";
					limit+=header[0];
					limit+="=";
					limit+=model.getValueAt(selectedRow, 0);
					String message=cpt.delete(tableName, limit, pw, br);
					JOptionPane.showMessageDialog(null,message,"删除成功",JOptionPane.PLAIN_MESSAGE);
					model.removeRow(selectedRow); //删除行
				}
				else
					JOptionPane.showMessageDialog(null,"请正确的选择要删除的内容","错误",JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
	
	//构建table
	public void setTable(String[] header,String[][] content,String tableName){//一下变注视代码可以实现表格的一般操作
		this.tableName=tableName;
		tablePanel.removeAll();
		table=new JTable();
		this.header=header;
//		String[] columns={"ID","姓名","性别"};//此处需要获得字段名称
		model=new DefaultTableModel(header,0);
		table.setModel(model);
		TableColumnModel columnModel=table.getColumnModel();
		int count=columnModel.getColumnCount();
		for(int i=0;i<count;i++){
			javax.swing.table.TableColumn column=columnModel.getColumn(i);
			column.setPreferredWidth(800/count);//设置列的宽度
			}
		for(int i=0;i<content.length;i++){
			String[] str=new String[content[i].length];
			for(int j=0;j<content[i].length;j++){
				str[j]=content[i][j];
			}
			model.addRow(str);
		}
		
		//加一行空行用于添加数据
		String[] white=new String[header.length];
		for(int i=0;i<white.length;i++)
			white[i]="";
		model.addRow(white);
		
		
		JTableHeader myt=table.getTableHeader();
		JPanel jtb=new JPanel();
		jtb.setLayout(new BorderLayout(0, 0));
		jtb.add(myt,BorderLayout.NORTH);
		jtb.add(table,BorderLayout.CENTER);
		scrollPane=new JScrollPane(table);
		scrollPane.add(jtb);
		tablePanel.add(scrollPane,BorderLayout.CENTER);
		JPanel butt=new JPanel();
		butt.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
		butt.setPreferredSize(new Dimension(0,50));
		butt.add(btnadd);
		butt.add(btndelete);
		tablePanel.add(butt, BorderLayout.SOUTH);
		tablePanel.updateUI();
	}
	
	public void addProperty(){
		
	}
	
	//判断数字和字符串
	public static boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }
	
}
