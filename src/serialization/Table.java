package serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Table implements Serializable {
	public int size = 0;
	public ArrayList<String> name = new ArrayList<>();    //列名
	public ArrayList<String> type = new ArrayList<>();   //数据类型
//	public ArrayList<Column> columns = new ArrayList<>();  //数据
//	
//	void addColumn() {
//		
//	}
//	
//	void addType() {
//		
//	}
}
