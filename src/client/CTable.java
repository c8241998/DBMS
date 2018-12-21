package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import client_DatabaseTree.DbList;
import client_DatabaseTree.Property;
import client_DatabaseTree.Table;

public class CTable {
	DbList dblist;
	
	public CTable(DbList dblist){
		this.dblist=dblist;
	}
	//创建表
	public String createTable(String dbName,String tableName,String propertys,PrintWriter pw,BufferedReader br){
		pw.println("create table "+tableName+" ("+propertys+")");
		try {
			String get=br.readLine();
			if(get!=null){
				return get;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "the server has some question";
	}
	
	//删除表
	public String dropTable(String tableName,PrintWriter pw,BufferedReader br){
		pw.println("drop table "+tableName);
		try {
			String get=br.readLine();
			if(get!=null) {
				return get;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "the server has some question";
	}
	
	//更新表名
	public String updateName(String tableName,String newTableName,PrintWriter pw,BufferedReader br){
		pw.println("alert table "+tableName+" rename to "+newTableName);
		try {
			String get=br.readLine();
			if(get!=null) {
				return get;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "the server has some question";
	}
	
	//添加列
	public String addColumn(String name,String columnName,String type,PrintWriter pw,BufferedReader br){
		pw.println("alert table "+name+" add ("+columnName+" "+type+")");
		try {
			String get=br.readLine();
			if(get!=null)
				return get;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "the server has some question";
	} 
	
	
}
