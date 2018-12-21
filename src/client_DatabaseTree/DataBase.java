package client_DatabaseTree;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
	//初始化变量
	private String name;
	private boolean read=false;
	private List<Table> tableList;
	/**
	 * 初始化数据库
	 * @return
	 */
	public DataBase(String name){
		this.name=name;
		tableList=new ArrayList<Table>();
	}
	
	/**
	 * 常用方法
	 * @return
	 */
	
	public void setDataBase(ArrayList<Table> list){
		tableList=list;
	}
	
	public List<Table> getDataBase(){
		return tableList;
	}
	
	public int getTableNum() {
		return tableList.size();
	}
	public String getName(){
		return name;
	}
	
	public void setName(String new_name){
		name=new_name;
	}
	
	public boolean addTable(Table table){
		tableList.add(table);
		return true;
	}
	
	public boolean deleteTable(String TableName){
		int num=-1;
		for(int i=0;i<tableList.size();i++){
			if(tableList.get(i).getName().equals(TableName)){
				num=i;
				break;
			}
		}
		if(num>-1){
			tableList.remove(num);
			return true;
		}
		else
			return false;
	}
	
	public boolean getRead(){
		return read;
	}
	
	public void setRead(boolean now){
		read=now;
	}
}
