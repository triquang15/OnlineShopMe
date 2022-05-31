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
import com.triquang.security.CustomerUserDetails;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Lazy
	@Autowired private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
		Customer customer = userDetails.getCustomer();
		
		customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	
}
