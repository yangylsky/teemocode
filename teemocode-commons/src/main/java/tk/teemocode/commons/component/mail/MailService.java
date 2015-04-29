package tk.teemocode.commons.component.mail;

import java.util.Map;

import javax.mail.MessagingException;

public interface MailService {
	public void send(String from, String to, String subject, String text) throws MessagingException;	
	public void send(Map<String, Object> mailinfo, String templateName) throws MessagingException;
}
