package com.triquang.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.triquang.Utility;
import com.triquang.common.entity.Address;
import com.triquang.common.entity.Customer;
import com.triquang.customer.CustomerService;

@Controller
public class AddressController {

	@Autowired
	private AddressService addressService;
	@Autowired
	private CustomerService customerService;

	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listAddressBook(customer);

		boolean usePrimaryAddressDefault = true;
		for (Address address : listAddresses) {
			if (address.isDefaultForShipping()) {
				usePrimaryAddressDefault = false;
				break;
			}
		}

		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressDefault", usePrimaryAddressDefault);

		return "address_book/addresses";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);

		return customerService.getCustomerByEmail(email);
	}
}
