package com.example.Response;

public class AuthResponse {
	
	private String jwt;
	
	private String Message;
	
	public AuthResponse() {
		// TODO Auto-generated constructor stub
	}

	public AuthResponse(String jwt, String Message) {
		super();
		this.jwt = jwt;
		this.Message = Message;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	
	

}
