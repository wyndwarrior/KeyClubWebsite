package wynd.mhskc;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.jdo.PersistenceManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONValue;

@SuppressWarnings("serial")
public class MHSKeyClubServlet extends HttpServlet {
	public static final String C_USER= "USER";
	public static final String FB_CON = "fbconnect";
	public static final String UP_INFO = "updateInfo";
	public static final String P_CONNECT = "connect";
	public static final String LIST_USERS = "listUsers";
	public static final String GET_USER = "getUser";
	public static final String VERIFY = "verify";
	public static final String ADMIN = "admin";
	public static final String MEVENT = "modifyEvent";
	public static final String LEVENT = "listEvents";
	public static final String GEVENT = "getEvent";
	public static final String MYEVENT = "myEvents";
	public static final String JEVENT = "joinEvent";
	public static final String QEVENT = "quitEvent";
	public static final String GHOURS = "giveHours";
	public static final String CHOURS = "clearHours";
	public static final String SFEATURED = "setFeatured";
	public static final String GFEATURED = "getFeatured";
	public static final String LHOURS = "listHours";
	public static final String SHOURS = "setHours";
	
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//set servlet url
		Facebook.setRedir(req.getRequestURL().toString());

		//ServletOutputStream out = resp.getOutputStream();

		Account u = new Account(req);
		
		String action = req.getParameter("action");

		if( FB_CON.equals(action)){
			String code = req.getParameter("code");

			if( code == null){
				resp.sendRedirect(Facebook.getLoginRedirectURL());
			}else{
				String s = new Fetcher(Facebook.getAuthURL(code)).fetch();
				try{
					String auth = s.split("&")[0].split("=")[1];
					addCookie(req,resp, C_USER, auth);
				}catch(Exception e){
					e.printStackTrace();
				}
				resp.sendRedirect("/?view="+P_CONNECT);
			}
			return;
		}
		
		if( GFEATURED.equals(action)){
			Featured f = Featured.getObject(AppEngine.getPM());
			KEvent ev = f.getFeatured();
			if( ev == null)
				apiReturnObject(resp, "false");
			else apiReturnObject(resp, ev.toMap());
			return;
		}
		
		if( LEVENT.equals(action)){
			KEventList list = KEventList.getObject(AppEngine.getPM());
			apiReturnObject(resp, convertEventList(list.list));
			return;
		}
		
		if( MYEVENT.equals(action)){
			if( !u.loggedIn() ){
				apiReturn(resp, "false");
				return;
			}
			KUser kuser = KUser.getUser(u.getId());
			if( kuser != null && kuser.getEventList()!=null){
				apiReturnObject(resp, convertEventList(kuser.getEventList()));
			}else{
				apiReturn(resp, "false");
			}
			return;
		}
		
		if( GEVENT.equals(action)){
			String id = req.getParameter("id");
			if( id == null)
				apiError(resp, "Missing id");
			KEvent ev = KEvent.getObject(AppEngine.getPM(), id);
			if( ev.getId() == null)
				apiError(resp, "Event not found");
			apiReturnObject(resp, ev.toMap());
			return;
		}
		
		if( !ensureOK(resp, u) )
			return;
		
		if( UP_INFO.equals(action) ){
			String phone = req.getParameter("phone"),
					phone2 = req.getParameter("phonealt"),
					grade = req.getParameter("grade"),
					agree = req.getParameter("agree"),
					email = req.getParameter("email");
			if( phone == null || phone2 == null || grade == null || agree == null 
				|| phone.equals("") || grade.equals("Select a grade") || email == null){
				error(resp, "Missing fields", P_CONNECT);
				return;
			}
			if( !agree.equals("on")){
				error(resp, "Please agree to the terms", P_CONNECT);
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			KUser user = KUser.getObject(pm, u.getId());
			
			user.setId(u.getId());
			user.setName(u.getName());
			user.setEmail(email);
			user.setGrade(grade);
			user.setPhone(phone);
			user.setPhone2(phone2);
			
			KUserList list = KUserList.getObject(pm);
			list.add(user.getSmall());
			
			AppEngine.save(pm,user);
			AppEngine.save(pm,list);
			AppEngine.close(pm);
			success(resp, "Your information has been updated", P_CONNECT);
			return;
		}
		
		if( JEVENT.equals(action) || QEVENT.equals(action) ){
			String id = req.getParameter("id");
			if( id == null){
				apiError(resp, "No id");
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			KUser user = KUser.getObject(pm, u.getId());
			
			if( user.getId() == null){			
				apiError(resp, "Not registered");
				return;
			}
			
			if( !user.isVerified()){	
				apiError(resp, "Cannot join events unless your account is verified.");
				return;
			}
			
			KEvent ev = KEvent.getObject(pm, id);
			if( ev.getId() == null){
				apiError(resp, "Invalid event");
				return;
			}
			
			KEventSmall small = ev.getSmall();
			KUserSmall usmall = user.getSmall();
			if( JEVENT.equals(action)){
				if( !user.getEventList().contains(small)) 
					user.getEventList().add(small);
				if( !ev.getUserList().contains(usmall))
					ev.getUserList().add(usmall);
			}else{
				user.getEventList().remove(small);
				ev.getUserList().remove(usmall);
			}
			
			AppEngine.save(pm,user);
			AppEngine.save(pm,ev);
			AppEngine.close(pm);
			apiReturnObject(resp, "true");
			return;
		}
		
		KUser kuser = KUser.getUser(u.getId());
		
		if( kuser == null || !kuser.isAdmin()){
			apiError(resp, "Not admin");
			return;
		}
		
		if( LIST_USERS.equals(action)){
			KUserList list = KUserList.getObject(AppEngine.getPM());
			ArrayList<HashMap<String, String> > ulist = new ArrayList<HashMap<String, String> >();
			for(KUserSmall ks : list.list){
				ulist.add(KUser.getUser(ks.getId()).toMap());
			}
			apiReturnObject(resp, ulist);
			return;
		}
		
		if( GET_USER.equals(action)){
			String id = req.getParameter("id");
			KUser user = null;
			if( id == null || (user=KUser.getUser(id)) == null){
				apiError(resp, "No user found");
				return;
			}
			apiReturnObject(resp, user.toMap());
			return;
		}
		
		if( VERIFY.equals(action)){
			String id = req.getParameter("id"),
					val = req.getParameter("value");
			KUser user = null;
			PersistenceManager pm = AppEngine.getPM();
			if( val == null || id == null || (user=KUser.getObject(pm, id)) == null || user.getId() == null){
				apiError(resp, "No user found");
				return;
			}
			user.setVerified("true".equals(val));
			AppEngine.save(pm, user);
			AppEngine.close(pm);
			apiReturn(resp, "true");
			return;
		}
		
		if( ADMIN.equals(action)){
			String id = req.getParameter("id"),
					val = req.getParameter("value");
			KUser user = null;
			PersistenceManager pm = AppEngine.getPM();
			if( val == null || id == null || (user=KUser.getObject(pm, id)) == null || user.getId() == null){
				apiError(resp, "No user found");
				return;
			}
			user.setAdmin("true".equals(val));
			AppEngine.save(pm, user);
			AppEngine.close(pm);
			apiReturn(resp, "true");
			return;
		}
		
		if( MEVENT.equals(action)){
			String id = req.getParameter("id"),
					name = req.getParameter("name"),
					desc = req.getParameter("desc"),
					loc = req.getParameter("loc"),
					pic = req.getParameter("pic"),
					sd = req.getParameter("sd"),
					st = req.getParameter("st"),
					ed = req.getParameter("ed"),
					et = req.getParameter("et"),
					hours = req.getParameter("hours"),
					quota = req.getParameter("quota");
			if(bad(id) || bad(name) || bad(desc) || 
					bad(loc) || pic==null || bad(sd) || 
					bad(st) || bad(ed) || bad(et) || bad(hours)){
				apiError(resp, "Field(s) missing");
				return;
			}
			double h = -1;
			int q = -1;
			Date sdate = null, edate = null;
			try{
				h = Double.parseDouble(hours.trim());
				sdate = KEvent.parseDate(sd + " " + st);
				edate = KEvent.parseDate(ed + " " + et);
				q = Integer.parseInt(quota);
			}catch(Exception e){
				apiError(resp, "Field(s) invalid");
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			if( id.equals("new") )
				id = KEvent.getUID();
			KEvent ev = KEvent.getObject(pm, id);
			ev.setDescription(desc);
			ev.setEdate(edate);
			ev.setHours(h);
			ev.setId(id);
			ev.setLocation(loc);
			ev.setName(name);
			ev.setPicture(pic.isEmpty() ? null : pic);
			ev.setSdate(sdate);
			ev.setQuota(q);
			AppEngine.save(pm, ev);
			KEventList evList = KEventList.getObject(pm);
			ev.updateList(evList.list);
			AppEngine.save(pm, evList);
			for(KUserSmall us : ev.getUserList()){
				KUser uu = KUser.getObject(pm, us.getId());
				ev.updateList(uu.getEventList());
				AppEngine.save(pm, uu);
			}
			AppEngine.close(pm);
			apiReturn(resp, id);
			return;
		}
		
		if( GHOURS.equals(action)){
			String id = req.getParameter("id");
			if(bad(id)){
				apiError(resp, "Field(s) missing");
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			KEvent ev = KEvent.getObject(pm, id);
			if( !id.equals(ev.getId())){
				apiError(resp, "Event not found");
				return;
			}
			KEventSmall tmp = ev.getSmall();
			for(KUserSmall us : ev.getUserList()){
				KUser user = KUser.getObject(pm, us.getId());
				ArrayList<KEventSmall> elist = user.getEventList();
				int ind = elist.indexOf(tmp);
				if( ind != -1)
					elist.get(ind).setHoursEarned(ev.getHours());
				user.calcHours();
				AppEngine.save(pm, user);
			}
			AppEngine.close(pm);
			apiReturn(resp, id);
			return;
		}
		
		if( CHOURS.equals(action)){
			String sdate = req.getParameter("date");
			if(bad(sdate)){
				apiError(resp, "Field(s) missing");
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			Date date = null;
			try {
				date = KEvent.parseDate2(sdate);
			} catch (ParseException e) {
				apiError(resp, "Bad date");
				e.printStackTrace();
				return;
			}
			KUserList list = KUserList.getObject(pm);
			for(KUserSmall ks : list.list){
				KUser user = KUser.getObject(pm, ks.getId());
				for(KEventSmall ev : user.getEventList()){
					if( ev.getEdate().compareTo(date) < 0)
						ev.setHoursEarned(0);
				}
				user.calcHours();
				AppEngine.save(pm, user);
			}
			AppEngine.close(pm);
			apiReturn(resp, "true");
			return;
		}
		
		if( LHOURS.equals(action)){
			String id = req.getParameter("id");
			if(bad(id)){
				apiError(resp, "Field(s) missing");
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			KEvent ev = KEvent.getObject(pm, id);
			if( !id.equals(ev.getId())){
				apiError(resp, "Event not found");
				return;
			}
			KEventSmall tmp = ev.getSmall();
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			for(KUserSmall us : ev.getUserList()){
				KUser user = KUser.getObject(pm, us.getId());
				ArrayList<KEventSmall> elist = user.getEventList();
				int ind = elist.indexOf(tmp);
				if( ind != -1){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", user.getId());
					map.put("hours", String.format("%.2f", elist.get(ind).getHoursEarned()));
					map.put("name", user.getName());
					list.add(map);
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", ev.getId());
			map.put("hours", String.format("%.2f", ev.getHours()));
			map.put("users", list);
			apiReturnObject(resp, map);
			return;
		}
		
		if( SHOURS.equals(action)){
			String id = req.getParameter("id"), uid = req.getParameter("uid"), hours = req.getParameter("hours");
			double x = -1;
			try{
				x = Double.parseDouble(hours);
			}catch(Exception e){
				apiError(resp, "Hours not a number");
				return;
			}
			if(bad(id) || bad(uid) || bad(hours)){
				apiError(resp, "Field(s) missing");
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			KUser user = KUser.getObject(pm, uid);
			ArrayList<KEventSmall> elist = user.getEventList();
			for(KEventSmall ke : elist)
				if( ke.getId().equals(id)){
					ke.setHoursEarned(x);
				}
			user.calcHours();
			AppEngine.save(pm, user);
			AppEngine.close(pm);
			apiReturn(resp, "true");
			return;
		}
		
		if( SFEATURED.equals(action)){
			String id = req.getParameter("id");
			if(bad(id)){
				apiError(resp, "Field(s) missing");
				return;
			}
			PersistenceManager pm = AppEngine.getPM();
			Featured f = Featured.getObject(pm);
			f.setFeatured(id);
			AppEngine.save(pm, f);
			AppEngine.close(pm);
			apiReturn(resp, id);
			return;
		}
		
	}
	
	public static HashMap<String,ArrayList<HashMap<String, String>>> convertEventList(ArrayList<KEventSmall> list){
		HashMap<String,ArrayList<HashMap<String, String>>> m = new HashMap<String,ArrayList<HashMap<String, String>>>();
		ArrayList<HashMap<String, String>> past = new ArrayList<HashMap<String, String>>(),
				up = new ArrayList<HashMap<String, String>>();
		m.put("past", past);
		m.put("upcoming", up);
		Date today = new Date();
		for(KEventSmall ev : list){
			HashMap<String, String> map = ev.toMap();
			if( today.compareTo(ev.getEdate()) < 0 )
				up.add(map);
			else past.add(map);
		}
		Collections.reverse(past);
		Collections.reverse(up);
		return m;
	}
	
	public static boolean bad(String s){
		return s == null || s.trim().isEmpty();
	}
	
	public static HashMap<String, String> map(String[][] sr){
		HashMap<String, String> m = new HashMap<String, String>();
		for(int i = 0; i<sr.length; i++)
			m.put(sr[i][0], sr[i][1]);
		return m;
	}
	
	public static String ret(String[][] sr){
		return JSONValue.toJSONString(map(sr));
	}
	
	public static String encode(String s){
		try{
			return URLEncoder.encode(s, "UTF8");
		}catch(Exception e){
			return "";
		}
	}
	
	public static void apiError(HttpServletResponse resp, String msg) throws IOException{
		resp.getOutputStream().println(ret(new String[][]{new String[]{"error", msg}}));
	}
	
	public static void apiReturn(HttpServletResponse resp, String val) throws IOException{
		resp.getOutputStream().println(ret(new String[][]{new String[]{"ret", val}}));
	}
	
	public static void apiReturnObject(HttpServletResponse resp, Object o) throws IOException{
		resp.getOutputStream().println(JSONValue.toJSONString(o));
	}
	
	public static void error(HttpServletResponse resp, String msg, String page) throws IOException{
		resp.sendRedirect("/?view="+page+"&err="+encode(msg));
	}
	
	public static void success(HttpServletResponse resp, String msg, String page) throws IOException{
		resp.sendRedirect("/?view="+page+"&succ="+encode(msg));
	}
	
	public static boolean ensureOK(HttpServletResponse resp, Account u) throws IOException{
		if( !u.loggedIn() || !u.ensurePermissions() ){
			error(resp, "Not logged in or permissions not granted", P_CONNECT);
			return false;
		}
		return true;	
	}
	
	public static String getCookie(HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) 
				if(cookies[i].getName().equals(name))
					return cookies[i].getValue();
		return null;
	}

	public static final int EXPIRE = 1000*60*60*24*7;

	public static void addCookie(HttpServletRequest req, HttpServletResponse resp, String name, String val) {
		Cookie c = new Cookie(name, val);
		c.setPath("/");
		c.setMaxAge(EXPIRE);
		resp.addCookie(c);
	}
}
