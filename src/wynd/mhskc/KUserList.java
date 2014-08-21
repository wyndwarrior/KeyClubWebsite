package wynd.mhskc;

import java.util.ArrayList;
import java.util.Collections;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(detachable="true")
@FetchGroup(name="otherfield", members={@Persistent(name="list")})
public class KUserList {

	public static final String KEY = "kUserListKey";
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String key = KEY;
	
	@Persistent(serialized = "true")
	public ArrayList<KUserSmall> list;
	
	public KUserList(){
		list = new ArrayList<KUserSmall>();
	}
	
	public static KUserList getObject(PersistenceManager pm){
		KUserList o = null;
    	try{
    		o = pm.getObjectById(KUserList.class, KEY);
    		o = pm.detachCopy(o);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	if( o == null)
    		o = new KUserList();
    	return o;
	}
	
	public void add(KUserSmall u){
		if( !list.contains(u)){
			list.add(u);
			Collections.sort(list);
		}
	}
	
}
