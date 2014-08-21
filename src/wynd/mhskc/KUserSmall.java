package wynd.mhskc;

import java.io.Serializable;
import java.util.HashMap;

public class KUserSmall implements Serializable, Comparable<KUserSmall>{
	private static final long serialVersionUID = 2503611816950254207L;
	private String name;
	private String id;
	public KUserSmall (String n, String i){
		name = n; setId(i);
	}
	
	public HashMap<String, String> toMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", getName());
		map.put("id", getId());
		return map;
	}
	
	public void setName(String n){
		name = n;
	}
	public String getName(){
		return name;
	}
	public int compareTo(KUserSmall u){
		return name.compareTo(u.name);
	}
	public int hashCode(){
		return getId().hashCode();
	}
	public boolean equals(Object o){
		if( !(o instanceof KUserSmall)) return false;
		return ((KUserSmall)o).getId().equals(getId());
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}