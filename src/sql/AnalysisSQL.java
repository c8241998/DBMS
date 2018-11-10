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

	String root="D:\\资料\\课程\\数据库\\DBMS\\DBMS\\data";

	File rootFile = new File(root+"\\root\\root.db");     //root database 文件夹路径
	
	
	//正则表达式
	String[] regrex= {
			"^create database (.+)$",    //0  创建数据库
			"drop database (.+)$",  //1 删除数据库
			"^create table (.+)$",  //2 添加表
			"^drop table (.+)$",  //3 删除表
			"^alter table add column (.+) (.+)$",  //4 增加列
			"^alter table modify column (.+) (.+)$",  //5 修改列
			"^alter table drop column (.+) (.+)$",  //6 删除列
			"^insert into (.+)\\((.+)\\) values\\((.+)\\)",  //7 插入行
			"^update (.+) set (.+)\\s?=\\s?(.+)(.*)$", //8 更新行
			"^select (.+) from (.+)(.*)$",  //9 查询
			"^delete from (.+)(.*)$",  //10 删除行
	};


	//构造器
	public AnalysisSQL(String sql) {
		this.sql = sql;
	}
	
	//创建数据库
	public Message create_database(String database) throws IOException {
		if(database.length()>128) {
			return(new Message(2, "length of database's name should not be more than 128."));
		}
		String dir = root+"\\"+database;
		File file = new File(dir);
		if(!file.exists()) {            //创建.tb .log .online
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
	
	
	//删除文件夹
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
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件  
				temp.delete();
				flag = true;  
			}  
		}
		file.delete();
		return flag;  
	}  
	
	//删除数据库
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
	
	//处理sql
	public Message work() throws IOException {
		int index = 0;
		boolean finish = false;
		for (String target : regrex) {    //遍历匹配正则表达式
			Pattern pattern = Pattern.compile(target);
			Matcher m = pattern.matcher(sql);
			if(m.find()) {
				if(index==0) {    //创建数据库
					String database = m.group(1);
					return create_database(database);
				}
				if(index==1) {   //删除数据库
					String database = m.group(1);
					return drop_database(database);
				}
				finish = true;
				break;
			}
			index++;
		}

		if(!finish) {        //非法sql命令
			return (new Message(0, "invalid construction"));
		}
		return null;
	}
}
