package utils.notify;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;
import settings.Config;

/**
 * Created by manjeet on 28/09/15.
 */
public class MailSenderParam {

    public String email;
    public String subject;
    public String message;

    public String EMAIL_SIGNATURE = "Thanks,\n " +Config.CompanyFullName+"\n "+Config.supportEmail +"\n "+Config.supportPhone;

    public MailSenderParam(String email, String subject , String message ) {
        this.email = email;
        this.subject = subject;
        this.message = message + "\n\n\n" + EMAIL_SIGNATURE;
    }
}

