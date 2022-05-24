package com.triquang.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.triquang.Utility;
import com.triquang.common.entity.Customer;
import com.triquang.common.exception.OrderNotFoundException;
import com.triquang.customer.CustomerNotFoundException;
import com.triquang.customer.CustomerService;

@RestController
public class OrderRestController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private CustomerService customerService;

	@PostMapping("/orders/return")
	public ResponseEntity<?> handlerOrderReturnRequest(@RequestBody OrderReturnRequest request,
			HttpServletRequest servletRequest) {

		Customer customer = null;

		try {
			customer = getAuthenticatedCustomer(servletRequest);
		} catch (CustomerNotFoundException e) {

			return new ResponseEntity<>("Authentication required", HttpStatus.BAD_REQUEST);
		}

		try {
			orderService.setOrderReturnRequested(request, customer);
		} catch (OrderNotFoundException e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(new OrderReturnResponse(request.getOrderId()), HttpStatus.OK);
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}

		return customerService.getCustomerByEmail(email);
	}

}
