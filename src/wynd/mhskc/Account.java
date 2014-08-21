package wynd.mhskc;

import javax.servlet.http.HttpServletRequest;

import com.restfb.types.User;

public class Account {
	private Facebook fb;
	private User u;
	public Account(HttpServletRequest req){
		fb = new Facebook(MHSKeyClubServlet.getCookie(req, MHSKeyClubServlet.C_USER));
		u = fb.getUser();
	}
	public boolean loggedIn(){
		return u != null;
	}
	public boolean ensurePermissions(){
		return fb.ensurePermissions();
	}
	public String getName(){
		return u.getName();
	}
	public String getEmail(){
		return u.getEmail();
	}
	public String getId(){
		return u.getId();
	}
}
