package com.dexter.gcv_life.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//import javax.mail.MessagingException;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dexter.gcv_life.Service.EmailService;

@CrossOrigin
@RestController
public class ContactUsMail {

	@Autowired
	EmailService service;
	
	@PostMapping("/sendingEmail")
	public ResponseEntity<?> sendEmail(@RequestParam(required = false, value = "email")String email,
			@RequestParam(required = false, value = "mobile")String mobile,
			@RequestParam(required = false, value = "message")String message,
			@RequestParam(required = false, value = "file") MultipartFile file) throws JSONException, MessagingException, IOException {
		
		System.out.println("sendingEmail API called");
		String fileName = "";
		if(!file.isEmpty()) {
			String currentPath = new java.io.File(".").getCanonicalPath();
			File convertFile = new File(currentPath + "/src/main/resources/static/document/");
			File myFile = new File(convertFile + "/" + file.getOriginalFilename());
			myFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(myFile);
			fos.write(file.getBytes());
			fos.close();
			
			fileName = file.getOriginalFilename();
		}
		service.sendSimpleMessage(email, mobile, message, fileName);
		
		return new ResponseEntity<>("Email send successfully...!",HttpStatus.OK);
	}
	
	@PostMapping("/sendPdfMail")
	public ResponseEntity<?> sendPdfMail(@RequestParam(required = false, value = "email")String email,
			@RequestParam(required = false, value = "message")String message,
			@RequestParam(required = false, value = "file") MultipartFile file) throws JSONException, MessagingException, IOException {
		
		System.out.println("sendPdfMail API called");
		String fileName = "";
		if(!file.isEmpty()) {
			String currentPath = new java.io.File(".").getCanonicalPath();
			File convertFile = new File(currentPath + "/src/main/resources/static/document/");
			File myFile = new File(convertFile + "/" + file.getOriginalFilename());
			myFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(myFile);
			fos.write(file.getBytes());
			fos.close();
			
			fileName = file.getOriginalFilename();
		}
		service.sendPdfMail(email, message, fileName);
		
		return new ResponseEntity<>("Email send successfully...!",HttpStatus.OK);
	}
}
