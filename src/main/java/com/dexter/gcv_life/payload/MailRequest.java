package com.dexter.gcv_life.payload;

import lombok.Data;

@Data
public class MailRequest {
	private String name;
	private String to;
	private String from;
	private String subject;
	private String userId;
}

