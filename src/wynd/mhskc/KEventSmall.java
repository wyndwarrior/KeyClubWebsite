package wynd.mhskc;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class KEventSmall implements Serializable, Comparable<KEventSmall>{
	private static final long serialVersionUID = 7519346917313715976L;
	private String name;
	private Date sdate, edate;
	private String id;
	private double hours, hoursEarned;
	
	public KEventSmall(String n, String id, Date sd, Date ed, double h){
		setName(n);
		setId(id);
		setSdate(sd);
		setEdate(ed);
		setHours(h);
	}

	
	public HashMap<String, String> toMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name",	getName());
		map.put("id", getId());
		map.put("sdate", KEvent.convertDate(getSdate()));
		map.put("edate", KEvent.convertDate(getEdate()));
		map.put("hours", KEvent.formatHours(getHours()));
		map.put("earned", KEvent.formatHours(getHoursEarned()));
		return map;
	}
	
	public int compareTo(KEventSmall e){
		int c = getSdate().compareTo(e.getSdate());
		if( c == 0)
			return id.compareTo(e.id);
		return c;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public int hashCode(){
		return getId().hashCode();
	}
	public boolean equals(Object o){
		if( !(o instanceof KEventSmall)) return false;
		return ((KEventSmall)o).getId().equals(getId());
	}

	public double getHoursEarned() {
		return hoursEarned;
	}

	public void setHoursEarned(double hoursEarned) {
		this.hoursEarned = hoursEarned;
	}

	public Date getEdate() {
		return edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}

	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
}
