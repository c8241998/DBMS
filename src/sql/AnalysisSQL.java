package sql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import serialization.Database;

public class AnalysisSQL {

	PrintWriter pw = null;
	
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
			"^insert\\s+into\\s+(.+)\\s*\\((.+)\\)\\s*values\\s*\\((.+)\\)",  //7 插入行 done
			"^update (.+) set (.+)\\s?=\\s?(.+)(.*)$", //8 更新行
			"^select\\s+(.+)\\s+from\\s+(.+)",  //9 查询
//			"^select\\s+(.+)\\s+from\\s+(.+)(\\s+where\\s+(.+))?$",  //9 查询
			"^delete from (.+)(.*)$",  //10 删除行
			"^use\\s+(.+)$"  //11 切换数据库  done
	};


	//构造器
	public AnalysisSQL(PrintWriter pw) {
		this.pw = pw;
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
	
	public boolean isTableExists(String table) throws IOException, ClassNotFoundException {
		
		String dir = rootPath+"\\"+database+"\\";
		File file = new File(dir+database+".tb");
		
		ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
		Database database = (Database) oiStream.readObject();
		oiStream.close();
		if(!database.table.contains(table)) {
			return false;
		}
		else {
			return true;
		}
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
	
	public boolean isTypeValid(String type) {
		if(!type.equals("int")&&!type.equals("double")&&!type.matches("varchar\\([0-9]+\\)")) {    //非法类型
			return false;
		}
		return true;
	}
	
	//添加字段
	public Message add_column(String table,String header,String type) throws IOException, ClassNotFoundException {
		
		if(!isTableExists(table)) {
			return new Message(2, "table does not exsist.");
		}
		
		if(!isTypeValid(type)) {    //非法类型
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
	public Message drop_column(String table,String header) throws IOException, ClassNotFoundException {
		
		if(!isTableExists(table)) {
			return new Message(2, "table does not exsist.");
		}
		
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
	
	/****
	 * @comments 写CSV文件
	 * @param str
	 * @param file
	 */
	public void writeFileToCsv(String[] str, String file) {
		File f = new File(file);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
			CsvWriter cwriter = new CsvWriter(writer,',');
			cwriter.writeRecord(str,false);
			cwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//value 与 type 是否匹配
	public boolean isValueType(String value,String type) {
		if(type.equals("int")) {
			try {
				Integer t = Integer.valueOf(value);
			}catch (Exception e) {
				return false;
			}
		}
		else if(type.equals("double")) {
			try {
				Double t = Double.valueOf(value);
			}catch (Exception e) {
				return false;
			}
		}
		else if(type.contains("varchar")) {
			String num = type.substring(8,type.length()-1);
//			String regrex = "^varchar\\((.+)\\)$";
//			Pattern pattern = Pattern.compile(regrex);
//			Matcher m = pattern.matcher(type);
			Integer t = Integer.valueOf(num);
			if(value.length()>t) {
				return false;
			}
		}
		return true;
	}
	
	//添加行
	public Message add_row(String table,String headers_,String values_) throws ClassNotFoundException, IOException {
		
		if(!isTableExists(table)) {
			return new Message(2, "table does not exsist.");
		}
		
		String[] headers = headers_.split("\\,");
		String[] values = values_.split("\\,");
		if(headers.length!=values.length) {
			return new Message(2, "invalid instruction");
		}
		
		String dir = rootPath+"\\"+database+"\\";
		String csvdir = dir+table+"\\"+table+".csv";
		CsvReader csvReader = new CsvReader(csvdir);
		
		csvReader.readHeaders();    //header
		String[] headers_all = csvReader.getHeaders();
		csvReader.readRecord();     //type
		
 		HashMap<String, String> map = new HashMap<>();
		int i=0;
		for (String header : headers) {
			boolean flag = false;
			for(String temp: headers_all) {
				if(header.equals(temp)) {
					flag=true;
					break;
				}
			}
			if(!flag) {
				return new Message(2, "invalid field name");
			}
			String value = values[i];
			String type = csvReader.get(header);
			if(!isValueType(value,type)) {
				return new Message(2, "type and value mismatch.");
			}
			map.put(header, value);
			i++;
		}
		
		String[] temp = new String[csvReader.getHeaderCount()];
		csvReader.close();
		
		i=0;
		for (String s : headers_all) {
			if(map.containsKey(s)) {
				temp[i]=map.get(s);
			}
			else {
				temp[i]="";
			}
			i++;
		}
		
		writeFileToCsv(temp, csvdir);
		
		return new Message(1, "");
	}
	
	// 检验是否为合法header
	public boolean isHeaderValid(String header,String table) throws IOException {
		
		String dir = rootPath+"\\"+database+"\\";
		String csvdir = dir+table+"\\"+table+".csv";
		CsvReader csvReader = new CsvReader(csvdir);
		
		csvReader.readHeaders();
		String[] headers = csvReader.getHeaders();
		for (String string : headers) {
			if(string.equals(header)) {
				csvReader.close();
				return true;
			}
		}
		
		csvReader.close();
		
		return false;
	}
	
	//查询
	public Message select_row(String headers_,String table,String limit_) throws ClassNotFoundException, IOException {
		
		if(!isTableExists(table)) {
			return new Message(2, "table does not exsist.");
		}
		
		String[] headers = headers_.split("\\,");
		for (String string : headers) {
			if(!isHeaderValid(string, table)) {
				return new Message(2, "invalid table field.");
			}
		}
		
		String[] limits = null;
		HashMap<String, String> limitMap = new HashMap<>();
		
		String dir = rootPath+"\\"+database+"\\";
		String csvdir = dir+table+"\\"+table+".csv";
		CsvReader csvReader = new CsvReader(csvdir);
		
		csvReader.readHeaders();
		csvReader.readRecord();
		
		if(!limit_.equals("null")) {
			if(!limit_.contains("and")) {
				String string = limit_;
				if(!string.contains("=")) {
					return new Message(2, "invalid limit.");
				}
				String[] t = string.split("\\=");
				if(!isHeaderValid(t[0], table)) {
					return new Message(2, "invalid table field.");
				}
				
				if(!isValueType(t[1], csvReader.get(t[0]))) {
					return new Message(2, "value and type mismatch.");
				}
				
				limitMap.put(t[0], t[1]);
			}
			else {
				limits = limit_.split("and");
				for (String string : limits) {
					if(!string.contains("=")) {
						return new Message(2, "invalid limit.");
					}
					String[] t = string.split("\\=");
					if(!isHeaderValid(t[0], table)) {
						return new Message(2, "invalid table field.");
					}
					
					if(!isValueType(t[1], csvReader.get(t[0]))) {
						return new Message(2, "value and type mismatch.");
					}
					
					limitMap.put(t[0], t[1]);
				}
			}
				
		}
		
		ArrayList<String[]> ans = new ArrayList<>();
		
		while(csvReader.readRecord()) {
			Iterator it = limitMap.entrySet().iterator();
			boolean flag=true;
			while(it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if(!csvReader.get(key).equals(value)) {
					flag=false;
					break;
				}
			}
			if(flag==true) {
				int i=0;
				String[] t = new String[headers.length];
				for (String header : headers) {
					t[i]=csvReader.get(header);
					i++;
				}
				ans.add(t);
			}
			
		}
		
		csvReader.close();
		
		pw.println("success");
		
		pw.println(ans.size());
		pw.println(headers.length);
		for (String[] strings : ans) {
			for (String string : strings) {
				pw.println(string);
			}
		}
		
		return new Message(1, "select");
		
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
				if(index==7) {   //插入行   "^insert\\s+into\\s+(.+)\\s*\\((.+)\\)\\s*values\\s*\\((.+)\\)"
					if(database==null) {
						return new Message(2, "Please choose database first.");
					}
					String table = m.group(1);
					String headers = m.group(2);
					String values = m.group(3);
					return add_row(table,headers,values);
				}
				if(index==9) {    // 查询 "^select\\s+(.+)\\s+from\\s+(.+)"
					if(database==null) {
						return new Message(2, "Please choose database first.");
					}
					String headers = m.group(1);
					String table = null;
					String limit = null;
					if(m.group(2).contains("where")) {
						String[] t = m.group(2).split("\\s+where\\s+");
						table = t[0].replaceAll("\\s+", "");
						limit = t[1].replaceAll("\\s+", "");
					}else {
						table =  m.group(2).replaceAll("\\s+", "");
						limit = "null";
					}
					
//					System.out.println(headers);
//					System.out.println(table);
//					System.out.println(limit);
					return select_row(headers,table,limit);
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
