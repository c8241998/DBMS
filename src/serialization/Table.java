package serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Table implements Serializable {
	public int size = 0;
	public ArrayList<String> name = new ArrayList<>();    //����
	public ArrayList<String> type = new ArrayList<>();   //��������
//	public ArrayList<Column> columns = new ArrayList<>();  //����
//	
//	void addColumn() {
//		
//	}
//	
//	void addType() {
//		
//	}
}
