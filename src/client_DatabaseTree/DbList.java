package client_DatabaseTree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableColumnModelListener;

public class DbList {
	/**
	 * ���ݿ����Ļ�������
	 */
	private List<DataBase> dbList;
	
	/*
	 * ��ʼ�����ݿ��б�
	 */
	public DbList(){
		dbList=new ArrayList<DataBase>();
	}
	
	//��ʼ��һ��database���Ӻ�˴���
	public void setDbList(ArrayList<DataBase> list){//��һ��ArrayList
		dbList=list;
	}
	
	//������ݿ�
	public void addDb(DataBase db){
		dbList.add(db);
	}
	
	//�������ݿ�����
	public List<DataBase> getList(){
		return dbList;
	}
	
	public int getDbNum() {
		return dbList.size();
	}
	
	//ɾ�����ݿ�
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
