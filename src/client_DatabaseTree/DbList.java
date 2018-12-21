package client_DatabaseTree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableColumnModelListener;

public class DbList {
	/**
	 * 数据库树的基本变量
	 */
	private List<DataBase> dbList;
	
	/*
	 * 初始化数据库列表
	 */
	public DbList(){
		dbList=new ArrayList<DataBase>();
	}
	
	//初始化一个database，从后端传入
	public void setDbList(ArrayList<DataBase> list){//传一个ArrayList
		dbList=list;
	}
	
	//添加数据库
	public void addDb(DataBase db){
		dbList.add(db);
	}
	
	//返回数据库链表
	public List<DataBase> getList(){
		return dbList;
	}
	
	public int getDbNum() {
		return dbList.size();
	}
	
	//删除数据库
	public void deleteDb(String dbName){
		int num=-1;
		for(int i=0;i<dbList.size();i++){
			if(dbList.get(i).getName().equals(dbName)){
				num=i;
				break;
			}
		}
		if(num>-1){
			dbList.remove(num);
			num--;
		}
	}
	
	
}
