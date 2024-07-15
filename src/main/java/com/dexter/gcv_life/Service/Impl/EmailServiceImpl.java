package com.dexter.gcv_life.Service.Impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.dexter.gcv_life.Entity.EmailData;
import com.dexter.gcv_life.Repository.EmailDataRepository;
import com.dexter.gcv_life.Service.EmailService;
import com.dexter.gcv_life.payload.MailResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private Configuration config;

	@Autowired
	private EmailDataRepository emailDataRepository;

	public static String incrementString(String input) {
		String numericPart = input.substring(3);

		int number = Integer.parseInt(numericPart);
		number++;

//        String incrementedString = String.format("GCV%04d", number);
		String incrementedString = String.format("Dexter Query Ticket-%06d", number);

		return incrementedString;
	}

	public void sendSimpleMessage(String email, String mobile, String mailMessage, String fileName)
			throws MessagingException {

		Map<String, Object> model = new HashMap<>();
		model.put("email", email);
		model.put("mobile", mobile);
		model.put("message", mailMessage);

		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			EmailData user = emailDataRepository.findUser();
			String lasteToken = user.getTokenNumber();

			String numericPart = lasteToken.substring(20);

			int number = Integer.parseInt(numericPart);
			number++;

//			String incrementedString = String.format("GCV%04d", number);
			String incrementedString = String.format("Dexter Query Ticket-%06d", number);

			model.put("ticket", incrementedString);

			EmailData data = new EmailData();
			data.setTokenNumber(incrementedString);
			emailDataRepository.save(data);

			if (!fileName.equals("")) {
				String currentPath = new java.io.File(".").getCanonicalPath();
				File convertFile = new File(currentPath + "/src/main/resources/static/document/" + fileName);
				helper.addAttachment(fileName, convertFile);
			}
			Template t = config.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			helper.setTo("agentportal4@gmail.com");
			helper.setText(html, true);
//			helper.setSubject("Contact us_" + mobile);
			helper.setSubject(incrementedString);
			helper.setFrom("agentportal4@gmail.com");
			sender.send(message);

			response.setMessage("Email send Sucessfully");
			response.setStatus(Boolean.TRUE);

//	        if (convertFile.delete()) {
//	            System.out.println("File deleted successfully");
//	        }
//	        else {
//	            System.out.println("Failed to delete the file");
//	        }

		} catch (MessagingException | IOException |

				TemplateException e) {
			response.setMessage("Mail Sending failure : " + e.getMessage());
			response.setStatus(Boolean.FALSE);
		}
	}

	@Override
	public void sendPdfMail(String email, String mailMessage, String fileName) throws MessagingException {

		Map<String, Object> model = new HashMap<>();
		model.put("email", email);
		model.put("message", mailMessage);

		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			if (!fileName.equals("")) {
				String currentPath = new java.io.File(".").getCanonicalPath();
				File convertFile = new File(currentPath + "/src/main/resources/static/document/" + fileName);
				helper.addAttachment(fileName, convertFile);
			}
			Template t = config.getTemplate("dashboard-email.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

//			helper.setTo("agentportal4@gmail.com");
			helper.setTo(email);
			helper.setText(html, true);
//			helper.setSubject("Contact us_" + mobile);
			helper.setSubject("Dashboard PDF Attachment");
			helper.setFrom("agentportal4@gmail.com");
			sender.send(message);

			response.setMessage("Email send Sucessfully");
			response.setStatus(Boolean.TRUE);

		} catch (MessagingException | IOException |

				TemplateException e) {
			response.setMessage("Mail Sending failure : " + e.getMessage());
			response.setStatus(Boolean.FALSE);
		}
	}
}
