package wynd.mhskc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
@FetchGroup(name="otherfield", members={@Persistent(name="userList")})
public class KEvent {
	
	@PrimaryKey
    @Persistent
	private String id;
	
	@Persistent
	private String name, description, picture, location;
	
	@Persistent(serialized = "true")
	private ArrayList<KUserSmall> userList;
	
	@Persistent
	private Date sdate, edate;
	
	@Persistent
	private double hours;
	
	@Persistent
	private int quota;
	
	public static String getUID(){
		return "ev"+UUID.randomUUID().toString().replace("-", "");
	}
	
	public static KEvent getObject(PersistenceManager pm, String id){
		KEvent o = null;
    	try{
    		o = pm.getObjectById(KEvent.class, id);
    		o = pm.detachCopy(o);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	if( o == null)
    		o = new KEvent();
    	return o;
	}
	
	public HashMap<String, Object> toMap(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", getName());
		map.put("id", getId());
		map.put("desc", getDescription());
		map.put("pic", getPicture());
		map.put("loc", getLocation());
		map.put("sdate", convertDate(getSdate()));
		map.put("edate", convertDate(getEdate()));
		map.put("hours", formatHours(getHours()));
		map.put("quota", ""+getQuota());
		ArrayList<HashMap<String, String>> ar = new ArrayList<HashMap<String, String>>();
		for(KUserSmall us : getUserList())
			ar.add(us.toMap());
		map.put("users", ar);
		return map;
	}
	
	private static final SimpleDateFormat parse = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	private static final SimpleDateFormat parse2 = new SimpleDateFormat("MM/dd/yyyy");
	private static final SimpleDateFormat convert = new SimpleDateFormat("MMMM d, yyyy @ h:mm a");
	static{
		parse2.setTimeZone(TimeZone.getTimeZone("CST"));
		parse.setTimeZone(TimeZone.getTimeZone("CST"));
		convert.setTimeZone(TimeZone.getTimeZone("CST"));
	}
	public static Date parseDate(String s) throws ParseException{
		return parse.parse(s);
	}
	public static Date parseDate2(String s) throws ParseException{
		return parse2.parse(s);
	}

	public static String convertDate(Date d){
		return convert.format(d);
	}
	
	public static String formatHours(double h){
		return String.format("%.2f", h);
	}
	
	public KEvent(){
		setUserList(new ArrayList<KUserSmall>());
	}
	
	public KEventSmall getSmall(){
		return new KEventSmall(getName(), getId(), getSdate(), getEdate(), getHours());
	}
	
	public void updateSmall(KEventSmall e){
		e.setName(getName());
		//e.setId(getId());
		e.setSdate(getSdate());
		e.setEdate(getEdate());
		e.setHours(getHours());
	}
	
	public void updateList(ArrayList<KEventSmall> list){
		boolean updated = false;
		for(KEventSmall ev : list)
			if(getId().equals(ev.getId())){
				updateSmall(ev);
				updated = true;
			}
		if( !updated ) 
			list.add(getSmall());
		Collections.sort(list);
	}
	
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date date) {
		this.sdate = date;
	}
	public Date getEdate() {
		return edate;
	}
	public void setEdate(Date date) {
		this.edate = date;
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
	public ArrayList<KUserSmall> getUserList() {
		return userList;
	}
	public void setUserList(ArrayList<KUserSmall> userList) {
		this.userList = userList;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}
}
