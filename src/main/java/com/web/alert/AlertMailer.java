package com.web.alert;

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

	private static FileInputStream mailerPropsFile;
	private static Properties mailerProps;

	private static FileInputStream mailIdFile;
	private static Properties mailIdList;

	private static String recipients = "";

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

	private String emailText(String timestamp,String cpuUsage,String ramUsage) {

		String text = "";
		if(!cpuUsage.equals("") && !ramUsage.equals("")) {
			text = "Hello,\nAn important notification.\nAt "+timestamp+" in your machine the Cpu Usage and RAM Usage has been exceeded the given limit.\nCpu Usage = "+cpuUsage+"\nRAM Usage = "+ramUsage+"\nKindly have a look on that.\nThanks..";
		} else if(!cpuUsage.equals("")) {
			text = "Hello,\nAn important notification.\nAt "+timestamp+" in your machine the Cpu Usage has been exceeded the given limit.\nCpu Usage = "+cpuUsage+"\nKindly have a look on that.\nThanks..";
		} else if(!ramUsage.equals("")) {
			text = "Hello,\nAn important notification.\nAt "+timestamp+" in your machine the RAM Usage has been exceeded the given limit.\nRAM Usage = "+ramUsage+"\nKindly have a look on that.\nThanks..";
		}
		return text;
	}

	public void sendMail(String timestamp,String cpuUsage,String ramUsage) {

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
			String text = emailText(timestamp,cpuUsage,ramUsage);
			message.setText(text);
			Transport.send(message);
			System.out.println("Mail sent to all recipients successfully");
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}