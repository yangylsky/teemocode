package com.teemocode.commons.component.mail;

import java.io.StringWriter;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MockMailService implements MailService {
	private static final Log LOG = LogFactory.getLog(MockMailService.class);
	
	@Override
	public void send(String from, String to, String subject, String text) throws MessagingException {
		LOG.debug(this + " send mail");
		LOG.debug("mail.from=[" + from + "]");
		LOG.debug("mail.to=[" + to + "]");
		LOG.debug("mail.subject=[" + subject + "]");
		LOG.debug("mail.text=[" + text + "]");
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
