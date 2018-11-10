package sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalysisSQL {

	String sql = null;

	String root="D:\\����\\�γ�\\���ݿ�\\DBMS\\DBMS\\data";

	File rootFile = new File(root+"\\root\\root.db");     //root database �ļ���·��
	
	
	//������ʽ
	String[] regrex= {
			"^create database (.+)$",    //0  �������ݿ�
			"drop database (.+)$",  //1 ɾ�����ݿ�
			"^create table (.+)$",  //2 ��ӱ�
			"^drop table (.+)$",  //3 ɾ����
			"^alter table add column (.+) (.+)$",  //4 ������
			"^alter table modify column (.+) (.+)$",  //5 �޸���
			"^alter table drop column (.+) (.+)$",  //6 ɾ����
			"^insert into (.+)\\((.+)\\) values\\((.+)\\)",  //7 ������
			"^update (.+) set (.+)\\s?=\\s?(.+)(.*)$", //8 ������
			"^select (.+) from (.+)(.*)$",  //9 ��ѯ
			"^delete from (.+)(.*)$",  //10 ɾ����
	};


	//������
	public AnalysisSQL(String sql) {
		this.sql = sql;
	}
	
	//�������ݿ�
	public Message create_database(String database) throws IOException {
		if(database.length()>128) {
			return(new Message(2, "length of database's name should not be more than 128."));
		}
		String dir = root+"\\"+database;
		File file = new File(dir);
		if(!file.exists()) {            //����.tb .log .online
			FileOutputStream fo;
			file.mkdir();
			file = new File(dir+"\\"+database+".tb");
			file.createNewFile();
			file = new File(dir+"\\"+database+".log");
			file.createNewFile();
			fo = new FileOutputStream(rootFile,true);
			fo.write(new String("||"+database).getBytes());
			//			file = new File(dir+"\\"+database+".online");
			//			fo = new FileOutputStream(file,false);
			//			fo.write(new String("false").getBytes());
			fo.close();

			return(new Message(1, ""));
		}else {
			return(new Message(2, "database already exsists."));
		}
	}
	
	
	//ɾ���ļ���
	public static boolean delAllFile(String path) {       
		boolean flag = false;  
		File file = new File(path);  
		if (!file.exists()) {  
			return flag;  
		}  
		if (!file.isDirectory()) {  
			return flag;  
		}  
		String[] tempList = file.list();  
		File temp = null;  
		for (int i = 0; i < tempList.length; i++) {  
			if (path.endsWith(File.separator)) {  
				temp = new File(path + tempList[i]);  
			} else {  
				temp = new File(path + File.separator + tempList[i]);  
			}  
			if (temp.isFile()) {  
				temp.delete();  
			}  
			if (temp.isDirectory()) {  
				delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�  
				temp.delete();
				flag = true;  
			}  
		}
		file.delete();
		return flag;  
	}  
	
	//ɾ�����ݿ�
	public Message drop_database(String database) throws IOException {
		String dir = root+"\\"+database;
		File file =new File(dir);
		if(!file.exists()) {
			return(new Message(2, "database doesn't exsist."));
		}

		else {
			FileInputStream fi = new FileInputStream(rootFile);
			byte[] b = new byte[5];
			int len;
			String temp = "";
			while((len=fi.read(b))!=-1) {
				temp+=new String(b, 0, len);
			}
			temp = temp.replaceAll("\\|\\|"+database, "");
			fi.close();
			FileOutputStream fo = new FileOutputStream(rootFile);
			fo.write(temp.getBytes());
			fo.close();
			delAllFile(file.getAbsolutePath());
			return(new Message(1, ""));
		}

	}
	
	//����sql
	public Message work() throws IOException {
		int index = 0;
		boolean finish = false;
		for (String target : regrex) {    //����ƥ��������ʽ
			Pattern pattern = Pattern.compile(target);
			Matcher m = pattern.matcher(sql);
			if(m.find()) {
				if(index==0) {    //�������ݿ�
					String database = m.group(1);
					return create_database(database);
				}
				if(index==1) {   //ɾ�����ݿ�
					String database = m.group(1);
					return drop_database(database);
				}
				finish = true;
				break;
			}
			index++;
		}

		if(!finish) {        //�Ƿ�sql����
			return (new Message(0, "invalid construction"));
		}
		return null;
	}
}
