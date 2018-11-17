package serialization;

import java.io.Serializable;
import java.util.ArrayList;

public class Root implements Serializable {
	public ArrayList<String> database = new ArrayList<>();
	public void addDatabase(String name) {
		database.add(name);
	}
}
