package wynd.mhskc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class Fetcher {
	private String URL;
	private StringBuffer post = new StringBuffer();
	private StringBuffer cookie = new StringBuffer();
	private String html;
	private boolean followRedir = true;
	private List<String> cookies;
	
	public Fetcher(String u){
		setURL(u);
	}
	
	public void addPost(String k, String v){
		post.append((post.length()==0?"":"&") + k+"="+enc(v));
	}
	
	public void addCookie(String k, String v){
		addCookie(k+"="+v);
	}
	public void addCookie(String s){
		cookie.append((cookie.length()==0?"":";") + s);
	}
	private static final int TRIES = 1;
	private static final int TIMEOUT = 7*1000;
	
	public String fetch(){
		for(int abc = 0; abc<TRIES; abc++)
		{
			HttpURLConnection con = null;
			try{
				con = (HttpURLConnection)new URL(URL).openConnection();
		        con.setReadTimeout(TIMEOUT);
		        con.setUseCaches(false);
		        con.setInstanceFollowRedirects(isFollowRedir());
		        boolean POST = post.length()!=0;
			    if(POST){
			        con.setDoOutput(true);
			        con.setRequestProperty("Content-Length", ""+post.length());
			        con.setRequestMethod("POST");
		        }
			    if( cookie.length()!= 0)
			        con.setRequestProperty("Cookie", cookie.toString());
			    con.connect();
			    if( POST){
			    	OutputStream out = con.getOutputStream();
			    	out.write(post.toString().getBytes());
			    	out.flush();
			    	//System.out.println(post);
			    }
			    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			    String line;
			    StringBuffer data = new StringBuffer();
			    while ((line = in.readLine()) != null)
			    	data.append(line+"\n");
			    setHtml(data.toString());
			    //System.out.println(con.getHeaderFields());
			    List<String> cookies = null;
			    if((cookies=con.getHeaderFields().get("set-cookie")) == null)
			    	cookies = con.getHeaderFields().get("Set-Cookie");
			    setCookies(cookies);
			    return getHtml();
			}catch(Exception e){
				e.printStackTrace();
				/*con != null){
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				    String line;
				    try {
						while ((line = in.readLine()) != null)
							System.out.println(line);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}*/
			}
		}
		return null;
	}
	
	public static String enc(String s){
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getHtml() {
		return html;
	}

	private void setHtml(String html) {
		this.html = html;
	}

	public List<String> getCookies() {
		return cookies;
	}

	private void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}
	
	public static void main(String[] args){
	}

	public boolean isFollowRedir() {
		return followRedir;
	}

	public void setFollowRedir(boolean followRedir) {
		this.followRedir = followRedir;
	}
	
	
}
