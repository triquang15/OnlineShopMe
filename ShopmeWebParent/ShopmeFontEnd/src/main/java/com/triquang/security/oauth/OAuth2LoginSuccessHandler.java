package com.triquang.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.triquang.common.entity.AuthenticationType;
import com.triquang.common.entity.Customer;
import com.triquang.customer.CustomerService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Lazy
	@Autowired private CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User auth2User = (CustomerOAuth2User) authentication.getPrincipal();

		String name = auth2User.getName();
		String email = auth2User.getEmail();
		String countryCode = request.getLocale().getCountry();
		String clientName = auth2User.getClientName();
		
		System.out.println("OAuth2LoginSuccessHandler: " + name + " | " + email);
		System.out.println("Client Name: " + clientName);
		
		AuthenticationType authencationType = getAuthencationType(clientName);
		
		Customer customer = customerService.getCustomerByEmail(email);
		if (customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name, email, countryCode, authencationType);
		} else {
			customerService.updateAuthenticationType(customer, authencationType);
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	private AuthenticationType getAuthencationType(String clientName) {
		if(clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		} else if (clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		} else {
			return AuthenticationType.DATABASE;
		}
	}

}
