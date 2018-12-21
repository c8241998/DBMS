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
	 * Ϊ�˷��㴫�䣬����������ȫ������������string�ַ�����
	 * typeString�д洢Ҫ������ֶμ���,�ֶ���','��Ϊ�ָ���
	 * contentString�д洢����ÿ���ֶζ�Ӧ�ľ���ֵ��ÿ������ֵ��','��Ϊ�ָ��!!!!!ע�����п��ܻ���varchar���͵Ĳ�����varchar���ͱ��������
	 * 
	 * 
	 * ���磺insert into student(sno,sname) values(1630,'����')
	 * @param TableName
	 * @param typeStr
	 * @param contentStr
	 * @param pw
	 * @param br
	 * @return ������������
	 */
	//�����ֶ�����
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
	 * �����ֶ�����
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
	 * propertyType�д洢����ɾ��������ѡȡ���������ֶ�
	 * propertyContent�д洢���������ֶ��е�����
	 * ���磺propertyType=ID    propertyContent=16301001
	 * @param TableName
	 * @param propertyType
	 * @param propertyContent
	 * @param pw
	 * @param br
	 * @return ������������
	 */
	//ɾ���ֶ�����
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
	 * TableName����Ҫ��ѯ�ı���
	 * propertyType�Ǳ��е���Ҫ��ѯ���ֶεļ���
	 * limit�Ƕ��ֶ������ݵ���������
	 * @param TableName
	 * @param propertyType
	 * @param limit
	 * @param pw
	 * @param br
	 * @return ���������Ӧ�÷���һ����ά���飬����������·���null
	 */
	//��ѯ�ֶ�����
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
     * ����µ��ֶ�����
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
     * ɾ���ֶ�����
     * @param TableName
     * @param propertyName
     * @param pw
     * @param br
     * @return
     */
  //ɾ����
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
     * �޸��ֶ�����
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
