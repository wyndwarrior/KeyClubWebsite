package wynd.mhskc;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
@FetchGroup(name="otherfield", members={@Persistent(name="eventList")})
public class KUser {
	
	@PrimaryKey
    @Persistent
	private String id;
	
	@Persistent
	private String name, email, phone, phone2, grade;
	
	@Persistent(serialized = "true")
	private ArrayList<KEventSmall> eventList;
	
	@Persistent
	private double hours, otherHours;
	
	@Persistent
	private boolean admin, verified;
	
	public static KUser getObject(PersistenceManager pm, String id){
		KUser o = null;
    	try{
    		o = pm.getObjectById(KUser.class, id);
    		o = pm.detachCopy(o);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	if( o == null)
    		o = new KUser();
    	return o;
	}
	
	public HashMap<String, String> toMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", getName());
		map.put("email", getEmail());
		map.put("phone", getPhone());
		map.put("phone2", getPhone2());
		map.put("verified", ""+isVerified());
		map.put("admin", ""+isAdmin());
		map.put("hours", String.format("%.2f", getHours()));
		map.put("grade", getGrade());
		map.put("id", getId());
		return map;
	}
	
	public static KUser getUser(String id){
		KUser u = getObject(AppEngine.getPM(), id);
		if( u.getId() == null ) return null;
		return u;
	}
	
	public KUser(){
		setEventList(new ArrayList<KEventSmall>());
	}
	
	public KUserSmall getSmall(){
		return new KUserSmall(getName(), getId());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public ArrayList<KEventSmall> getEventList() {
		return eventList;
	}
	public void setEventList(ArrayList<KEventSmall> eventList) {
		this.eventList = eventList;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public boolean isAdmin() {
		return admin || getId().equals("1553775431");
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public double getOtherHours() {
		return otherHours;
	}
	public void setOtherHours(double otherHours) {
		this.otherHours = otherHours;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public void calcHours(){
		setHours(getOtherHours());
		for(KEventSmall ev : getEventList())
			setHours(getHours() + ev.getHoursEarned());
	}
	
}
