package com.web.service;

import java.util.*;
import java.io.*;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

public class AlertMailer {

	private FileInputStream mailerPropsFile;
	private Properties mailerProps;

	private FileInputStream mailIdFile;
	private Properties mailIdList;

	private String recipients = "";

	public AlertMailer() {
		try {
			mailerPropsFile = new FileInputStream("/home/harikumar_g/Documents/projects/SystemPerformance-Backend/src/main/resources/Mailer.properties");
			mailerProps = new Properties();
			mailerProps.load(mailerPropsFile);

			mailIdFile = new FileInputStream("/home/harikumar_g/Documents/projects/SystemPerformance-Backend/src/main/resources/AlertMailId.properties");
			mailIdList = new Properties();
			mailIdList.load(mailIdFile);

			for(String key : mailIdList.stringPropertyNames()) {
				if(recipients.equals(""))
					recipients = recipients + mailIdList.getProperty(key);
				else
					recipients = recipients +","+mailIdList.getProperty(key);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String emailText(String systemName,String timestamp,String maxCpu,String cpuUsage,String maxRam,String ramUsage) {
		String text = "";
		if(!cpuUsage.equals("") && !ramUsage.equals("")) {
			text = "Hello,\nAn important notification.\nAt "+timestamp+" in "+systemName+" machine the Cpu Usage and RAM Usage has been exceeded the given limit.\nCpu Usage = "+cpuUsage+" - MaxCpu Usage = "+maxCpu+" \nRAM Usage = "+ramUsage+" - MaxRAM Usage = "+maxRam+"\nKindly have a look on that.\nThanks..";
		} else if(!cpuUsage.equals("")) {
			text = "Hello,\nAn important notification.\nAt "+timestamp+" in "+systemName+" machine the Cpu Usage has been exceeded the given limit.\nCpu Usage = "+cpuUsage+" - MaxCpu Usage = "+maxCpu+"\nKindly have a look on that.\nThanks..";
		} else if(!ramUsage.equals("")) {
			text = "Hello,\nAn important notification.\nAt "+timestamp+" in "+systemName+" machine the RAM Usage has been exceeded the given limit.\nRAM Usage = "+ramUsage+" - MaxRAM Usage = "+maxRam+"\nKindly have a look on that.\nThanks..";
		}
		return text;
	}

	public void sendMail(String systemName,String timestamp,String maxCpu,String cpuUsage,String maxRam,String ramUsage) {

		Properties props = new Properties();
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host",mailerProps.getProperty("host"));
		props.put("mail.smtp.port",mailerProps.getProperty("port"));
		Session session = Session.getInstance(props,new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailerProps.getProperty("fromuser").toString(),mailerProps.getProperty("password").toString());
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailerProps.getProperty("mailid").toString()));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));
			message.setSubject("Limit Exceeded Alert");
			String text = emailText(systemName,timestamp,maxCpu,cpuUsage,maxRam,ramUsage);
			message.setText(text);
			Transport.send(message);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}