package server_sql;


public class Message {
	public int flag;     //0 非法sql  1 成功 2 异常
	public String msg;
	public Message(int flag,String msg) {
		this.flag=flag;
		this.msg=msg;
	}
}
