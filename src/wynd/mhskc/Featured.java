package wynd.mhskc;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
public class Featured {

	public static final String KEY = "kFeaturedKey";
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String key = KEY;
	
	private String featured;
	
	public Featured(){
		
	}
	
	public void setFeatured(String s){
		featured = s;
	}
	
	public KEvent getFeatured(){
		if( featured  == null )return null;
		KEvent ev = KEvent.getObject(AppEngine.getPM(), featured);
		if( ev.getId() == null ) return null;
		return ev;
	}
	
	public static Featured getObject(PersistenceManager pm){
		Featured o = null;
    	try{
    		o = pm.getObjectById(Featured.class, KEY);
    		o = pm.detachCopy(o);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	if( o == null)
    		o = new Featured();
    	return o;
	}
	
}
