package client_frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import client.TreeMenu;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class AddTableFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField_tableName;
	private JTable table;
	private MyAbstractTableModel model;
	public JButton btnOK;
	private String[] listData = new String[]{"int", "double", "varchar"};
	private JComboBox comboBox;
	private JButton btn_addProperty;
	private int num=0;

	/**
	 * Create the frame.
	 */
	public AddTableFrame(TreeMenu treeMenu,PrintWriter pw,BufferedReader br) {
		setTitle("\u65B0\u5EFA\u8868");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 432, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_tableName = new JPanel();
		contentPane.add(panel_tableName, BorderLayout.NORTH);
		panel_tableName.setLayout(new BoxLayout(panel_tableName, BoxLayout.X_AXIS));

		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u8868\u540D\uFF1A");
		panel_tableName.add(lblNewLabel);
		
		textField_tableName = new JTextField();
		panel_tableName.add(textField_tableName);
		textField_tableName.setColumns(10);
		
		JPanel panel_table = new JPanel();
		contentPane.add(panel_table, BorderLayout.CENTER);
		panel_table.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		comboBox = new JComboBox<String>(listData);
		model = new MyAbstractTableModel();
		table.setModel(model);
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor((JComboBox<String>)comboBox));
		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		addDataChangeListener();
	
		/*用JScrollPane装载JTable，这样超出范围的列就可以通过滚动条来查看*/  
        JScrollPane scroll = new JScrollPane(table); 
        panel_table.add(scroll);
        
        JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		btn_addProperty = new JButton("\u6DFB\u52A0\u5B57\u6BB5");
		btn_addProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
            	model.addEmptyRow();
            	table.updateUI();
            }
        });
		panel.add(btn_addProperty);
		
		btnOK = new JButton("\u786E\u8BA4\u521B\u5EFA\u8868");
		btnOK.addMouseListener(treeMenu.newTable_mouseListner(pw, br));
		
		panel.add(btnOK);
	}
	private void addDataChangeListener(){
        //检测单元格数据变更
        Action action = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                TableCellListener tcl = (TableCellListener)e.getSource();
                int row = tcl.getRow();
                int col = tcl.getColumn();
                if(col==2) {
                	for(int i=0;i<table.getRowCount();i++) {
                		if(row!=i&&(Boolean)table.getValueAt(i, 2)==true) {
                			table.setValueAt(false, row, 2);
                			JOptionPane.showMessageDialog(null,"您已设置主键","错误",JOptionPane.PLAIN_MESSAGE); 
                			return;
                		}
                	}
                	table.setValueAt(true, row, 3);
                	table.setValueAt(true, row, 4);
                }
                //Object oldValue = tcl.getOldValue();
                //if(oldValue == null)
                    //oldValue = "";
                //Object newValue = tcl.getNewValue();       
                //if(newValue == null)
                    //newValue = "";
               // System.out.printf("cell changed at [%d,%d] : %s -> %s%n",row, col, oldValue, newValue);
            }
        };
        @SuppressWarnings("unused")
       TableCellListener tcl1 = new TableCellListener(table, action);
    }
	public String getPropertyStr(){
		String str="";
		String p_name="";
		String p_type="";
		String p_pk="";
		String p_notnull="";
		String p_unique="";
		for(int i=0;i<table.getRowCount();i++) {
			if(table.getValueAt(i, 0).equals("")) {
				continue;
			}
			if(i!=0) {
				str+=",";
			}
			p_name=table.getValueAt(i, 0).toString();
			str+=p_name;
			p_type=table.getValueAt(i, 1).toString();
			if(p_type.equals("varchar"))
				p_type="varchar(8)";
			str+=" ";
			str+=p_type;
			if((Boolean)table.getValueAt(i, 2)==true) {
				p_pk=p_name;
				p_notnull="not null";
				str+=" ";str+=p_notnull;
				p_unique="unique";
				str+=" ";str+=p_unique;
			}
			else {
				if((Boolean)table.getValueAt(i, 3)==true) {
					p_notnull="not null";
					str+=" ";str+=p_notnull;
				}
				if((Boolean)table.getValueAt(i, 4)==true) {
					p_notnull="unique";
					str+=" ";str+=p_unique;
				}	
			}
		}
		if(p_pk.equals("")==false) {
			str+=",";
			str+="primary key(";
			str+=p_pk;
			str+=")";
		}
		System.out.printf(str);
		return str;
	}
	public String[][] getPropertys() {
		String[] property=new String[5];
		String[][] propertys = new String[table.getRowCount()][5];
		for(int i=0;i<table.getRowCount();i++) {
			if(table.getValueAt(i, 0).equals("")) {
				continue;
			}
			num++;
			for(int j=0;j<5;j++) {
				propertys[i][j]=table.getValueAt(i, j).toString();
			}
		}
		return propertys;
	}
	public String getTableName(){
		return textField_tableName.getText();
	}
	public int getPropertyNum() {
		return num;
	}
	
}
