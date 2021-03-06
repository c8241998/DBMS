package client_frames;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import client.TreeMenu;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;

public class AddPropertyFrame extends JFrame {

	private JPanel contentPane;
	private JPanel panel_propertyName;
	private JTextField textField_propertyName;
	public JButton btnOK;
	private String[] listData = new String[]{"int", "double", "varchar"};
	private JComboBox comboBox;
	private JCheckBox chckbxNotNull;
	private JCheckBox chckbxUnique;
	private JTextPane textPane;

	/**
	 * Create the frame.
	 */
	public AddPropertyFrame(TreeMenu treeMenu,PrintWriter pw,BufferedReader br) {
		setTitle("\u65B0\u5EFA\u8868");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 410, 180);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panel_propertyName = new JPanel();
		contentPane.add(panel_propertyName, BorderLayout.NORTH);
		panel_propertyName.setLayout(new BoxLayout(panel_propertyName, BoxLayout.X_AXIS));

		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u5B57\u6BB5\u540D\uFF1A");
		panel_propertyName.add(lblNewLabel);
		
		textField_propertyName = new JTextField();
		panel_propertyName.add(textField_propertyName);
		textField_propertyName.setColumns(10);
		
		comboBox = new JComboBox(listData);
		comboBox.addItemListener(new ItemListener() {
		      public void itemStateChanged(final ItemEvent e) {
		          int index = comboBox.getSelectedIndex();
		          if (index ==2) { // ==0表示选中的事第一个
		        	if(textPane!=null)
		        		panel_propertyName.remove(textPane);
		            textPane = new JTextPane();
		    		panel_propertyName.add(textPane);
		    		panel_propertyName.updateUI();
		          }
		          else{
		        	  if(textPane!=null){
		        		  panel_propertyName.remove(textPane);
		        		  panel_propertyName.updateUI();
		        	  }
		          }
		        }
		      });
		panel_propertyName.add(comboBox);
		
		JPanel panel_type = new JPanel();
		contentPane.add(panel_type, BorderLayout.CENTER);
		
		chckbxNotNull = new JCheckBox("Not Null");
		chckbxUnique = new JCheckBox("Unique");
		GroupLayout gl_panel_type = new GroupLayout(panel_type);
		gl_panel_type.setHorizontalGroup(
			gl_panel_type.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_type.createSequentialGroup()
					.addGap(62)
					.addComponent(chckbxNotNull)
					.addGap(43)
					.addComponent(chckbxUnique)
					.addGap(68))
		);
		gl_panel_type.setVerticalGroup(
			gl_panel_type.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_type.createParallelGroup(Alignment.BASELINE)
					.addComponent(chckbxUnique)
					.addComponent(chckbxNotNull))
		);
		panel_type.setLayout(gl_panel_type);
		
        JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		btnOK = new JButton("\u786E\u8BA4\u6DFB\u52A0\u5B57\u6BB5");
		btnOK.addActionListener(treeMenu.newProperty_actionListner(pw, br));
		
		panel.add(btnOK);
	}
	
	public String getPropertyName() {
		return textField_propertyName.getText();
	}
	
	public String getPropertyStr(){
		String str="";
		String p_name="";
		String p_type="";
		String p_notnull="";
		String p_unique="";
		
		p_name=getPropertyName();
		str=p_name;
		p_type=getP_type();
		str+=" ";
		str+=p_type;
		if(chckbxNotNull.isSelected()) {
			p_notnull="not null";
			str+=" ";
			str+=p_notnull;
		}
		if(chckbxUnique.isSelected()) {
			p_unique="unique";
			str+=" ";
			str+=p_unique;
		}
		return str;
	}
	
	public String getP_type() {
		String get=comboBox.getSelectedItem().toString();
		if(get.equals("varchar"))
			get=get+"("+textPane.getText()+")";
//		System.out.println(get+"test");
		return get;
	}
	public boolean getP_notnull() {
		return chckbxNotNull.isSelected();
	}
	public boolean getP_unique() {
		return chckbxUnique.isSelected();
	}
}
