package serialization;

import java.io.Serializable;
import java.util.ArrayList;

public class Database implements Serializable {
	public ArrayList<String> table = null;
	public Database() {
		table = new ArrayList<>();
	}
}
