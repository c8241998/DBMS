package client_frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.TreeMenu;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;

public class AddPropertyName extends JFrame {

	private JPanel contentPane;
	private JTextField textField_tableName;
	private JTable table;
	private MyAbstractTableModel model;
	public JButton btnOK;
	private String[] listData = new String[]{"int", "double", "varchar"};
	private JComboBox comboBox;
	private JCheckBox checkPK;
	private JCheckBox checkNotNull;
	private JCheckBox checkUnique;
	private JButton btn_addProperty;

	/**
	 * Create the frame.
	 */
	public AddPropertyName(String tableName,TreeMenu treeMenu,PrintWriter pw,BufferedReader br) {
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
		textField_tableName.setEditable(false);
		
		JPanel panel_table = new JPanel();
		contentPane.add(panel_table, BorderLayout.CENTER);
		panel_table.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		comboBox = new JComboBox<String>(listData);
		checkPK=new JCheckBox();
		checkNotNull=new JCheckBox();
		checkUnique=new JCheckBox();
		model = new MyAbstractTableModel();
		table.setModel(model);
		table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(checkPK));
		table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(checkNotNull));
		table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(checkUnique));
		
	
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
//		btnOK.addMouseListener(treeMenu.newTable_mouseListner(pw, br));
		
		panel.add(btnOK);
	}
}
