package client_frames;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
class MyAbstractTableModel extends AbstractTableModel {
	// 定义表头数据
	private String[] head = {"Name", "Type", "primary key","not null","unique"};
 
	// 定义表的内容数据
	private Object[] emptyRow= new Object[] {new String(),"int",new Boolean(false),new Boolean(false),new Boolean(false)};
	
	// 定义表格每一列的数据类型
	private Class[] typeArray = { String.class, JComboBox.class, Boolean.class, Boolean.class, Boolean.class };
 
	private List<Object> data=new ArrayList<Object>();
//	Object[][] data;
 
	public MyAbstractTableModel(){
		addEmptyRow();
	}
	// 获得表格的列数
	public int getColumnCount() {
		return head.length;
	}
 
	public void addEmptyRow() {
		data.add(new Object[] {new String(),"int",new Boolean(false),new Boolean(false),new Boolean(false)});
	}

	// 获得表格的行数
	public int getRowCount() {
		return data.size();
	}
 
	// 获得表格的列名称
	@Override
	public String getColumnName(int column) {
		return head[column];
	}
 
	// 获得表格的单元格的数据
	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((Object[])data.get(rowIndex))[columnIndex];
	}
 
	// 使表格具有可编辑性
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
 
	// 替换单元格的值
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		((Object[])data.get(rowIndex))[columnIndex] = aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}
 
	// 实现了如果是boolean自动转成JCheckbox
	/*
	 * 需要自己的celleditor这么麻烦吧。jtable自动支持Jcheckbox，
	 * 只要覆盖tablemodel的getColumnClass返回一个boolean的class， jtable会自动画一个Jcheckbox给你，
	 * 你的value是true还是false直接读table里那个cell的值就可以
	 */
	public Class getColumnClass(int columnIndex) {
		return typeArray[columnIndex];// 返回每一列的数据类型
	}
}

