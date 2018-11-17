package sql;

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.sun.org.apache.bcel.internal.generic.NEW;

import javafx.scene.chart.PieChart.Data;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import serialization.Database;
import serialization.Root;
import serialization.Table;

public class AnalysisSQL {

	String sql = null;

	String rootPath="D:\\资料\\课程\\数据库\\DBMS\\DBMS\\data";

	File rootFile = new File(rootPath+"\\root\\root.db");     //rootPath database 文件夹路径
	
	
	String database = null;
	
	
	//正则表达式
	String[] regrex= {
			"^create\\s+database\\s+(.+)$",    //0  创建数据库  done
			"drop\\s+database\\s+(.+)$",  //1 删除数据库  done
			"^create\\s+table\\s*(.+)\\s+\\((.+)\\)$",  //2 添加表  done
			"^drop\\s+table\\s*(.+)$",  //3 删除表  done
			"^alter\\s+table\\s+(.+)\\s+add\\s+(.+)\\s+(.+)$",  //4 增加列  done
			"^alter table modify Column (.+) (.+)$",  //5 修改列  
			"^alter\\s+table\\s+(.+)\\s+drop\\s+(.+)$",  //6 删除列  done
			"^insert into (.+)\\((.+)\\) values\\((.+)\\)",  //7 插入行
			"^update (.+) set (.+)\\s?=\\s?(.+)(.*)$", //8 更新行
			"^select (.+) from (.+)(.*)$",  //9 查询
			"^delete from (.+)(.*)$",  //10 删除行
			"^use\\s+(.+)$"  //11 切换数据库
	};


	//构造器
	public AnalysisSQL() {
		
	}
	
	//创建数据库
	public Message create_database(String database) throws IOException, ClassNotFoundException {
		if(database.length()>128) {
			return(new Message(2, "length of database's name should not be more than 128."));
		}
		String dir = rootPath+"\\"+database;
		File file = new File(dir);
		if(!file.exists()) {            //创建.tb .log
			FileOutputStream fo;
			file.mkdir();
			file = new File(dir+"\\"+database+".tb");
			file.createNewFile();
			
			ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
			Database tableList = new Database();
			ooStream.writeObject(tableList);
			ooStream.close();
			
			file = new File(dir+"\\"+database+".log");
			file.createNewFile();
			fo = new FileOutputStream(rootFile,true);
			fo.write(new String("||"+database).getBytes());
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
		String dir = rootPath+"\\"+database;
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
	
	//切换数据库
	public Message use_database(String database) throws IOException {
		FileInputStream fi = new FileInputStream(rootFile);
		byte[] b = new byte[5];
		int len;
		String temp = "";
		while((len=fi.read(b))!=-1) {
			temp+=new String(b, 0, len);
		}
		if(!temp.contains(database)) {
			return new Message(2, "database not exists!");
		}
		this.database = database;
		return new Message(1, "");
	}
	
	//添加表
	public Message create_table(String table,String types_) throws ClassNotFoundException, IOException {
		String dir = rootPath+"\\"+database+"\\";
		File file = new File(dir+database+".tb");
		ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));        //读入database.tb
		Database tableList = (Database)oiStream.readObject();
		oiStream.close();
		
		if(tableList.table.contains(table)) {            //重复表
			return new Message(2, "table already exists!");
		}
		
		
		ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));    //更新database.tb
		tableList.table.add(table);
		ooStream.writeObject(tableList);
		ooStream.close();
		
		file = new File(dir+table);     // 生成表文件夹
		file.mkdir();
		file = new File(dir+table+"\\"+table+".csv");
		file.createNewFile();
		CsvWriter csvWriter = new CsvWriter(dir+table+"\\"+table+".csv",',',Charset.forName("GBK"));
		String[] types = types_.split("\\s*\\,\\s*");
		int i=0;
		String[] name = new String[types.length];
		String[] type = new String[types.length];
		for (String type_ : types) {
			String[] strings = type_.split("\\s+");
			name[i]=strings[0];
			type[i]=strings[1];
			if(!type[i].equals("int")&&!type[i].equals("double")&&!type[i].matches("varchar\\([0-9]+\\)")) {
				return new Message(2, "invalid type");
			}
			i++;
		}
        csvWriter.writeRecord(name);
        csvWriter.writeRecord(type);
        csvWriter.close();
		
		return new Message(1, "");
		
	}
	
	//删除表
	public Message drop_table(String table) throws ClassNotFoundException, IOException {
		String dir = rootPath+"\\"+database+"\\";
		File file = new File(dir+database+".tb");
		
		ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
		Database database = (Database) oiStream.readObject();
		oiStream.close();
		if(!database.table.contains(table)) {
			return new Message(2, "table does not exist!");
		}
		
		database.table.remove(table);
		
		ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
		ooStream.writeObject(database);
		ooStream.close();
		
		delAllFile(dir+table);
		
		return new Message(1, "");
	}
	
	//添加字段
	public Message add_column(String table,String header,String type) throws IOException {
		
		if(!type.equals("int")&&!type.equals("double")&&!type.matches("varchar\\([0-9]+\\)")) {    //非法类型
			return new Message(2, "invalid type");
		}
		
		String dir = rootPath+"\\"+database+"\\";
		String csvdir = dir+table+"\\"+table+".csv";
		CsvReader csvReader = new CsvReader(csvdir);
		CsvWriter csvWriter = new CsvWriter(dir+table+"\\__TEMP__.csv");
		while(csvReader.readRecord()) {
			int num = csvReader.getColumnCount();
			num++;
			String[] values = csvReader.getValues();
			String[] temp = new String[num];
			for(int i=0;i<num-1;i++) {
				temp[i]=values[i];
			}
			if(csvReader.getCurrentRecord()==0) {
				temp[num-1] = header;
			}else if(csvReader.getCurrentRecord()==1) {
				temp[num-1] = type;
			}else {
				temp[num-1] = "";
			}
			
			csvWriter.writeRecord(temp);
		}
		
		csvReader.close();
		csvWriter.close();
		
		File file = new File(csvdir);
		file.delete();
		File tempFile = new File(dir+table+"\\__TEMP__.csv");
		tempFile.renameTo(file);
		
		return new Message(1, "");
	}
	
	//删除列
	public Message drop_column(String table,String header) throws IOException {
		
		String dir = rootPath+"\\"+database+"\\";
		String csvdir = dir+table+"\\"+table+".csv";
		CsvReader csvReader = new CsvReader(csvdir);
		CsvWriter csvWriter = new CsvWriter(dir+table+"\\__TEMP__.csv");
		
		csvReader.readHeaders();
		int num = csvReader.getHeaderCount();
		String[]headers = csvReader.getHeaders();    //获取header为第几列
		int order = 0;
		for(int i=0;i<num;i++) {
			if(headers[i].equals(header)) {
				order = i;
				break;
			}
		}
		
		String[] temp = new String[num-1];          //写入新headers
		for(int i=0;i<order;i++) {
			temp[i]=headers[i];
		}
		for(int i=order+1;i<num;i++) {
			temp[i-1]=headers[i];
		}
		csvWriter.writeRecord(temp);
		
		while(csvReader.readRecord()) {          //写入record
			
			String[] values = csvReader.getValues();
			temp = new String[num-1];
			for(int i=0;i<order;i++) {
				temp[i]=values[i];
			}
			for(int i=order+1;i<num;i++) {
				temp[i-1]=values[i];
			}
			csvWriter.writeRecord(temp);
		}
		
		csvReader.close();
		csvWriter.close();
		
		File file = new File(csvdir);
		file.delete();
		File tempFile = new File(dir+table+"\\__TEMP__.csv");
		tempFile.renameTo(file);
		
		return new Message(1, "");
	}
	
	//处理sql
	public Message work(String sql) throws IOException, ClassNotFoundException {
		this.sql =sql;
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
				if(index==2) {   //添加表
					if(database==null) {
						return new Message(2, "Please choose database first.");
					}
					String table = m.group(1);
					String types = m.group(2);
					
					return create_table(table,types);
				}
				if(index==3) {    //删除表
					if(database==null) {
						return new Message(2, "Please choose database first.");
					}
					String table = m.group(1);
					return drop_table(table);
				}
				if(index==4) {   //添加列     "^alter\\s+table\\s+(.+)\\s+add\\s+(.+)\\s+(.+)$"
					if(database==null) {
						return new Message(2, "Please choose database first.");
					}
					String table = m.group(1);
					String header = m.group(2);
					String type = m.group(3);
					return add_column(table,header,type);
				}
				if(index==6) {   //删除列     
					if(database==null) {
						return new Message(2, "Please choose database first.");
					}
					String table = m.group(1);
					String header = m.group(2);
					return drop_column(table,header);
				}
				if(index==11) {   //切换数据库
					String database = m.group(1);
					return use_database(database);
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
