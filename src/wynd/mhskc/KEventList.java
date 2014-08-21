package wynd.mhskc;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
@FetchGroup(name="otherfield", members={@Persistent(name="list")})
public class KEventList {
	
	public static final String KEY = "kEventListKey";
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String key = KEY;
	
	@Persistent(serialized = "true")
	public ArrayList<KEventSmall> list;
	
	public KEventList(){
		list = new ArrayList<KEventSmall>();
	}
	
	public static KEventList getObject(PersistenceManager pm){
		KEventList o = null;
    	try{
    		o = pm.getObjectById(KEventList.class, KEY);
    		o = pm.detachCopy(o);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	if( o == null)
    		o = new KEventList();
    	return o;
	}
	
}
