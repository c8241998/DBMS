package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import client_DatabaseTree.DbList;

public class CProperty {
	DbList dblist;
	public CProperty(DbList dblist){
		this.dblist=dblist;
	}
	
	/**
	 * 为了方便传输，将插入内容全部集成在两个string字符串中
	 * typeString中存储要插入的字段集合,字段由','作为分隔符
	 * contentString中存储插入每个字段对应的具体值，每个具体值由','作为分割符!!!!!注意其中可能会有varchar类型的参数，varchar类型必须带‘’
	 * 
	 * 
	 * 例如：insert into student(sno,sname) values(1630,'张三')
	 * @param TableName
	 * @param typeStr
	 * @param contentStr
	 * @param pw
	 * @param br
	 * @return 服务器处理结果
	 */
	//插入字段内容
	public String insert(String TableName,String typeStr,String contentStr,PrintWriter pw,BufferedReader br){
		pw.println("insert into "+TableName+"("+typeStr+") values("+contentStr+")");
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
	
	/**
	 * 更新字段内容
	 * @param TableName
	 * @param typeStr
	 * @param contentStr
	 * @param pw
	 * @param br
	 * @return
	 */
	public String update(String TableName,String typeStr,String contentStr,String limit,PrintWriter pw,BufferedReader br){
		pw.println("update "+TableName+" set "+typeStr+"="+contentStr+"where "+limit);
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
	
	/**
	 * propertyType中存储的是删除的内容选取出的特殊字段
	 * propertyContent中存储的事特殊字段中的内容
	 * 例如：propertyType=ID    propertyContent=16301001
	 * @param TableName
	 * @param propertyType
	 * @param propertyContent
	 * @param pw
	 * @param br
	 * @return 服务器处理结果
	 */
	//删除字段内容
	public String delete(String TableName,String limit,PrintWriter pw,BufferedReader br){
		pw.println("delete from "+TableName+" where "+limit);
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
	
	/**
	 * TableName是所要查询的表名
	 * propertyType是表中的所要查询的字段的集合
	 * limit是对字段中内容的限制条件
	 * @param TableName
	 * @param propertyType
	 * @param limit
	 * @param pw
	 * @param br
	 * @return 正常情况下应该返回一个二维数组，非正常情况下返回null
	 */
	//查询字段数据
    public String[][] select(String TableName,String propertyType,String limit,PrintWriter pw,BufferedReader br){
    	if(limit!=null)
    		pw.println("select "+propertyType+" from "+TableName+"where "+limit);
    	else
    		pw.println("select "+propertyType+" from "+TableName);
		try {
			if(br.readLine().equals("success")){
				int row=Integer.valueOf(br.readLine());
				int col=Integer.valueOf(br.readLine());
				@SuppressWarnings("unused")
				String[][] content=new String[row][col];
				for(int i=0;i<row;i++){
					for(int j=0;j<col;j++){
						content[i][j]=br.readLine();
//						System.out.println(content[i][j]);
					}
				}
				return content;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 添加新的字段类型
     * @param TableName
     * @param propertyName
     * @param propertyType
     * @param pw
     * @param br
     * @return
     */
    public String add(String TableName,String propertyStr,PrintWriter pw,BufferedReader br){
    	pw.println("alter table "+TableName+" add "+propertyStr);
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
    
    /**
     * 删除字段类型
     * @param TableName
     * @param propertyName
     * @param pw
     * @param br
     * @return
     */
  //删除列
  	public String dropColumn(String tableName,String columnName,PrintWriter pw,BufferedReader br){
  		pw.println("alter table "+tableName+" drop "+columnName);
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
    
    
    /**
     * 修改字段类型
     * @param TableName
     * @param propertyName
     * @param propertyType
     * @param pw
     * @param br
     * @return
     */
    public String modify(String TableName,String propertyName,String propertyType,PrintWriter pw,BufferedReader br){
    	pw.println("alter table "+TableName+" modify("+propertyName+" "+propertyType+")");
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
