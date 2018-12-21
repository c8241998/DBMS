package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import client_DatabaseTree.DataBase;
import client_DatabaseTree.DbList;

public class CDatabase {
	DbList dblist;
	
	public CDatabase(DbList dblist){
		this.dblist=dblist;
	}
	
	//切换数据库
	public String exchangDatabase(String name,PrintWriter pw,BufferedReader br){
		pw.println("use "+name);
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
	
	//创建数据库
	public String createDatabase(String name,PrintWriter pw,BufferedReader br){
		pw.println("create database "+name);
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
	
	//移除数据库
	public String dropDatabase(String name,PrintWriter pw,BufferedReader br){
		pw.println("drop database "+name);
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
	
	
}
