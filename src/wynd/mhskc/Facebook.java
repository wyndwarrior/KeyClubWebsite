package wynd.mhskc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.User;


public class Facebook {
    // I will be very sad if this is used improperly
    private static final String secret = "6bfc2200e051062d55bb91381da42f6c";
    private static final String client_id = "537438109647390";
    
    public static String redirect_uri = null; 
    private static final String[] perms = "user_about_me friends_about_me email rsvp_event".split(" ");
    
    public static void setRedir(String s){
    	if( s != null) s = s.replace("http://www.", "http://");
    	redirect_uri = s;
    }
    
    public static String getLoginRedirectURL() throws UnsupportedEncodingException {
        return "https://graph.facebook.com/oauth/authorize?client_id=" + 
            client_id + "&display=page&redirect_uri=" + 
            URLEncoder.encode(redirect_uri+"?action="+MHSKeyClubServlet.FB_CON, "UTF8")+"&scope="+combine(perms);
    }
    
    private static String combine(String[] s){
    	StringBuffer sb = new StringBuffer(s[0]);
    	for(int i=1; i<s.length; i++)
    		sb.append(","+s[i]);
    	return sb.toString();
    }

    public static String getAuthURL(String authCode) throws UnsupportedEncodingException {
        return "https://graph.facebook.com/oauth/access_token?client_id=" + 
            client_id+"&redirect_uri=" + 
            URLEncoder.encode(redirect_uri+"?action="+MHSKeyClubServlet.FB_CON, "UTF8")+"&client_secret="+secret+"&code="+authCode;
    }
    

	private FacebookClient fb = null;
	private User user = null;
    
	public Facebook(String authToken){
		if( authToken != null)
			fb = new DefaultFacebookClient(authToken);
		if( fb != null)
			try{
				user = fb.fetchObject("me", User.class);
			}catch(Exception e){}
	}
	
	public boolean loggedIn(){
		return user != null;
	}
	
	public JsonObject fetch(String s){
		return fb.fetchObject(s, JsonObject.class);
	}
	
	public boolean ensurePermissions(){
		HashSet<String> set = new HashSet<String>(Arrays.asList(perms));
		JsonObject o = fetch("me/permissions");
		JsonObject d = o.getJsonArray("data").getJsonObject(0);
		for(String s : JsonObject.getNames(d))
			if( d.getInt(s) == 1 )
				set.remove(s);
		return set.isEmpty();
	}
	
	public User getUser(){
		return user;
	}
	
	public ArrayList<User> getFriends(){
		Connection<User> f = fb.fetchConnection("me/friends", User.class, Parameter.with("fields","id,name,gender"));
		ArrayList<User> list = new ArrayList<User>();
		for(List<User> friends : f)
			list.addAll(friends);
		return list;
	}
	
	public User getUser(String id){
		return fb.fetchObject(id, User.class);
	}
	
	public String getProfilePicture(User u){
		JsonObject o = fb.fetchObject(u.getId()+"/picture", JsonObject.class, 
				Parameter.with("width","500"), Parameter.with("height","500"), Parameter.with("redirect", "false"));
		return o.getJsonObject("data").getString("url");
	}
    
}