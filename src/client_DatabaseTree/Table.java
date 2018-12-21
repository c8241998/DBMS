package client_DatabaseTree;

import java.util.ArrayList;
import java.util.List;

public class Table {
	//初始化变量
	private String name;
	private List<Property> propertyList;
	private boolean read=false;
	public Table(String name) {
		this.name=name;
		propertyList=new ArrayList<Property>();
	}
	/**
	 * 常用方法
	 */
	
	public Property getProperty(String name){
		for(int i=0;i<propertyList.size();i++){
			if(propertyList.get(i).getName().equals(name)){
				return propertyList.get(i);
			}
		}
		return null;
	}
	
	public void setPropertyList(ArrayList<Property> list){
		propertyList=list;
	}
	
	public List<Property> getPropertyList(){
		return propertyList;
	}
	
	public int getPropertyNum() {
		return propertyList.size();
	}
	public String getName(){
		return name;
	}
	
	public void setName(String new_name){
		name=new_name;
	}
	
	public boolean addProperty(Property property){
		propertyList.add(property);
		return true;
	}
	
	public boolean deleteProperty(String TableName){
		int num=-1;
		for(int i=0;i<propertyList.size();i++){
			if(propertyList.get(i).getName().equals(TableName)){
				num=i;
				break;
			}
		}
		if(num>-1){
			propertyList.remove(num);
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
