package server_sql;


public class Message {
	public int flag;     //0 �Ƿ�sql  1 �ɹ� 2 �쳣
	public String msg;
	public Message(int flag,String msg) {
		this.flag=flag;
		this.msg=msg;
	}
}
