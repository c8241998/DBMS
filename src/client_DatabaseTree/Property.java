package client_DatabaseTree;

import java.util.ArrayList;
import java.util.List;

public class Property<T> {
	/**
	 * 常用变量
	 */
	private String name;//名字
	private String type;//类型
	private List<T> content;//内容
	private boolean primaryKey;//判定是否为主键
	private boolean foreignKey;//判定是否为外键
	private T defaultKey;//默认值
	/**
	 * 约束条件变量
	 */
	//数值大小约束
	private int Max;//最大长度
	private int Min=0;//最小长度
	//数值内容约束
	private T[] limit;//禁止输入内容
	private boolean isNull;//判定是否为空
	private boolean unique;//判定可否有重复内容
	
	public Property(String name,String type){
		content=new ArrayList<T>();
		this.name=name;
		this.type=type;
	}
	public Property(String name,String type,List<T> content,boolean primaryKey,boolean foreignKey,T defaultKey,int Max,int Min,T[] limit,boolean isNull,boolean unique) {
		this.name=name;
		this.type=type;
		this.content=content;
		this.primaryKey=primaryKey;
		this.foreignKey=foreignKey;
		this.defaultKey=defaultKey;
		this.Max=Max;
		this.Min=Min;
		this.limit=limit;
		this.isNull=isNull;
		this.unique=unique;
	}
	/**
	 * 常用方法
	 */
	
	
	public List<T> getAllContent(){
		return content;
	}
	
	public T getContent(int num){
		return content.get(num);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String new_name){
		name=new_name;
	}
	
	public void updateContent(int num,T new_content){
		content.set(num, new_content);
	}
	
	public void updateMax(int num){
		Max=num;
	}
	
	public void updateMin(int num){
		Min=num;
	}
	
	public void setPrimaryKey(boolean judge){
		primaryKey=judge;
	}
	
	public void setForeignKey(boolean judge){
		foreignKey=judge;
	}
	
	public void setDefaultKey(T defaultkey){
		defaultKey=defaultkey;
	}
	
	public void setNull(boolean judge){
		isNull=judge;
	}
	
	public void setUnique(boolean judge){
		unique=judge;
	}
	
	public void setContent(List list){
		content=list;
	}
}
