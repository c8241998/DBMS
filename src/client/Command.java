package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Command {
	
	private JPanel commandPanel;
	private JScrollPane scrollPane;
	private JTextPane textArea_out;
	private JTextField textField_in;
	
	public Command(JPanel commandPanel,final PrintWriter pw,final BufferedReader br){
		this.commandPanel=commandPanel;
		//设置textArea和textField的属性
		textArea_out=new JTextPane();
		textArea_out.setEditable(false);
		textArea_out.setBackground(Color.black);
		textArea_out.setText("Microsoft Windows [版本 10.0.17134.407]\n(c) 2018 Microsoft Corporation。保留所有权利。\nSQL*Plus: Release 11.2.0.1.0\nCopyright (c) 1982, 2010, SQL.All rights reserved.\nSQL>");
		textArea_out.setForeground(Color.white);
		textField_in=new JTextField();
		textField_in.setPreferredSize(new Dimension(0, 30));
		textField_in.addActionListener(new ActionListener(){
		   public void actionPerformed(ActionEvent arg0) {
			   String content=textField_in.getText();
			   String back="";
			   pw.println(content);
			   try {
				   back=br.readLine();
			  } catch (IOException e) {
				// TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			   String out=textArea_out.getText();
			   if(content.contains("select")){
				   if(back.equals("success")){
					   String getone;
					   try{
						   int row=Integer.valueOf(br.readLine());
						   int col=Integer.valueOf(br.readLine());
						   String tableStr="";
						   for(int i=0;i<row;i++){
								for(int j=0;j<col;j++){
									getone=br.readLine();
									//加字符串
									tableStr+=getone;
									//加空格
									for(int k=0;k<(12-getone.length());k++){
										tableStr=tableStr+" ";
									}	
								}
								tableStr+="\n";
							}
						   out+=content+"\n\n----";
						   out+=back+"----\n\n"+tableStr+"\n\nSQL>";
						   textArea_out.setText(out);
					   	}catch(IOException e){
					   	}
				   }
			   }
			   else{
				   out+=content+"\n\n----";
				   out+=back+"----\n\nSQL>";
				   textArea_out.setText(out);
			   }
			   
			   textField_in.setText("");
			   }    
			  });
	}
	
	public void setCommand(){	
		scrollPane=new JScrollPane(textArea_out);
		commandPanel.add(scrollPane ,BorderLayout.CENTER);
		commandPanel.add(textField_in, BorderLayout.SOUTH);
	}
	
	public void clear(){
		textArea_out.setText("");
	}
}
