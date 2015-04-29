package tk.teemocode.commons.component.mail;

import java.io.StringWriter;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class JavaMailService extends JavaMailSenderImpl implements MailService {
	private final Log LOG = LogFactory.getLog(JavaMailService.class);
	
	@Override
	public void send(String from, String to, String subject, String text) throws MessagingException {
		MimeMessage mailmsg = createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mailmsg, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, text);
		send(mailmsg);
	}
	
	@Override
	public void send(Map<String, Object> mailinfo, String templateName) throws MessagingException {
		String from = (String) mailinfo.get("mail.from");
		String to = (String) mailinfo.get("mail.to");
		String subject = (String) mailinfo.get("mail.subject");
		StringWriter swText = new StringWriter();
		try {
//			WebworkUtil.processFreemarker(mailinfo, swText, templateName);
		} catch(Exception e) {
			LOG.error("WebworkUtil.processFreemarker error!", e);
		}
		send(from, to, subject, swText.toString());
	}
}
