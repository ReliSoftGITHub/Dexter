package com.dexter.gcv_life.Service;

//import javax.mail.MessagingException;

import jakarta.mail.MessagingException;

public interface EmailService {
    
	public void sendSimpleMessage(String email, String mobile, String message, String fileName)throws MessagingException ;

	public void sendPdfMail(String email, String message, String fileName)throws MessagingException;

}
