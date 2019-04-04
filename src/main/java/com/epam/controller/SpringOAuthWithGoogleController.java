package com.epam.controller;

import org.apache.taglibs.standard.lang.jstl.test.beans.Factory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringOAuthWithGoogleController {
	
	private GoogleConnectionFactory factory = new GoogleConnectionFactory("clientId", "Secret");
	
	String google_scope="https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

	
	@RequestMapping("/")
	public ModelAndView firstPage() {
		return new ModelAndView("welcome");
	}
	
	@GetMapping(value = "/useApplication")
	public String producer() {
		OAuth2Operations operation = factory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri("http://localhost:8080/forwardLogin");
		params.setScope(google_scope);
		String url = operation.buildAuthenticateUrl(params);
		System.out.println("The URL is :"+url);
		return "redirect:"+url;
	}
	
	@GetMapping(value = "/forwardLogin")
	public ModelAndView producer(@RequestParam("code") String authorizationCode){
		OAuth2Operations operations = factory.getOAuthOperations();
		System.out.println("h2"+" With Authorization code : "+authorizationCode);
		AccessGrant accessToken = operations.exchangeForAccess(authorizationCode, "http://localhost:8080/forwardLogin", null);
		System.out.println("Token : "+accessToken);
		Connection<Google> connection = factory.createConnection(accessToken);
		UserProfile user = connection.fetchUserProfile();
		ModelAndView model = new ModelAndView("details");
		System.out.println("User data : "+user.toString());
		model.addObject("user",user);
		return model;
 	}
	
	
	
	
	
	
}

































