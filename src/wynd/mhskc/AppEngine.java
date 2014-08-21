package wynd.mhskc;

import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AppEngine {
	private static final PersistenceManagerFactory pmf =
	        JDOHelper.getPersistenceManagerFactory("transactions-optional");
	public static PersistenceManager getPM(){
		PersistenceManager pm = pmf.getPersistenceManager();
		pm.getFetchPlan().addGroup("otherfield");
		return pm;
	}
	
	public static void save(PersistenceManager pm, Object o){
		pm.makePersistent(o);
	}
	
	public static void close(PersistenceManager pm){
		pm.close();
	}
	
	public static void email(String sub, String body){
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("iwyndwarrior@gmail.com", "Project V"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("iwyndwarrior@gmail.com", "iwyndwarrior@gmail.com"));
            msg.setSubject(sub);
            msg.setText(body);
            Transport.send(msg);

        } catch (Exception e){
        	
        }
	}
}
