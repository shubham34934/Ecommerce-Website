package utils.notify;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;
import settings.Config;

/**
 * Created by manjeet on 28/09/15.
 */
public class MailSender {

    public static final String EMAIL_USER = "mail.smtp.user";
    public static final String EMAIL_PASSWORD = "mail.smtp.pass";
    public static final String EMAIL_HOST = "mail.smtp.host";
    public static final String EMAIL_PORT = "mail.smtp.port";

    public static boolean SendMail(MailSenderParam param) {
        return SendSimpleMail(param);
    }

    //http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
    public static void SendJavaMail() {

    }

    //https://www.playframework.com/documentation/1.3.x/emails
    //http://commons.apache.org/proper/commons-email/userguide.html//
    public static boolean SendSimpleMail(MailSenderParam param) {
        try {
            String serverEmail = play.Play.application().configuration().getString(EMAIL_USER);
            String serverEmailPass = play.Play.application().configuration().getString(EMAIL_PASSWORD);
            String emailHost = play.Play.application().configuration().getString(EMAIL_HOST);
            int emailPort = play.Play.application().configuration().getInt(EMAIL_PORT);
            Logger.info("Trying to send mail");
            Logger.info("SimpleEmail:"+serverEmail+serverEmailPass+emailHost+emailPort);

            SimpleEmail email = new SimpleEmail();
            email.setHostName(emailHost);
            email.setSmtpPort(emailPort);
            email.setTLS(true);
            //email.setStartTLSEnabled(true);
            //email.setSSLOnConnect(true);

            if(Config.Debug) {
                //email.setDebug(true);
            }


            //email.setAuthenticator(new DefaultAuthenticator("shubham34934@gmail.com", "times@123"));
            //email.setFrom("shubham34934@gmail.com");
            //email.setAuthenticator(new DefaultAuthenticator("zoopiniit@gmail.com", "zoop@123"));
            //email.setFrom("zoopiniit@gmail.com");

            email.setAuthenticator(new DefaultAuthenticator(serverEmail, serverEmailPass));
            email.setFrom(Config.CompanyName+"-noreply@gmail.com" , Config.CompanyFullName);

            email.setSubject(param.subject);
            email.setMsg(param.message);
            email.addTo(param.email);
            //email.addTo("zoopiniit@gmail.com");
            //email.addTo(serverEmail);
            email.send();

            Logger.info("Mail send successfully: " + param.email);
            return true;
        } catch (Exception e) {
            Logger.error("Mail could not be send: some error occoured: "+param.email+", error="+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
