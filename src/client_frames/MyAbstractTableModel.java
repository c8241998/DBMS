package client_frames;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
class MyAbstractTableModel extends AbstractTableModel {
	// �����ͷ����
	private String[] head = {"Name", "Type", "primary key","not null","unique"};
 
	// ��������������
	private Object[] emptyRow= new Object[] {new String(),"int",new Boolean(false),new Boolean(false),new Boolean(false)};
	
	// ������ÿһ�е���������
	private Class[] typeArray = { String.class, JComboBox.class, Boolean.class, Boolean.class, Boolean.class };
 
	private List<Object> data=new ArrayList<Object>();
//	Object[][] data;
 
	public MyAbstractTableModel(){
		addEmptyRow();
	}
	// ��ñ�������
	public int getColumnCount() {
		return head.length;
	}
 
	public void addEmptyRow() {
		data.add(new Object[] {new String(),"int",new Boolean(false),new Boolean(false),new Boolean(false)});
	}

	// ��ñ�������
	public int getRowCount() {
		return data.size();
	}
 
	// ��ñ���������
	@Override
	public String getColumnName(int column) {
		return head[column];
	}
 
	// ��ñ��ĵ�Ԫ�������
	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((Object[])data.get(rowIndex))[columnIndex];
	}
 
	// ʹ�����пɱ༭��
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
 
	// �滻��Ԫ���ֵ
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		((Object[])data.get(rowIndex))[columnIndex] = aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}
 
	// ʵ���������boolean�Զ�ת��JCheckbox
	/*
	 * ��Ҫ�Լ���celleditor��ô�鷳�ɡ�jtable�Զ�֧��Jcheckbox��
	 * ֻҪ����tablemodel��getColumnClass����һ��boolean��class�� jtable���Զ���һ��Jcheckbox���㣬
	 * ���value��true����falseֱ�Ӷ�table���Ǹ�cell��ֵ�Ϳ���
	 */
	public Class getColumnClass(int columnIndex) {
		return typeArray[columnIndex];// ����ÿһ�е���������
	}
}

