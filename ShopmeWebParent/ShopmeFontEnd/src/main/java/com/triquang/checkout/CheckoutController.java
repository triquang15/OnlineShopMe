package com.triquang.checkout;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.triquang.Utility;
import com.triquang.address.AddressService;
import com.triquang.common.entity.Address;
import com.triquang.common.entity.CartItem;
import com.triquang.common.entity.Customer;
import com.triquang.common.entity.ShippingRate;
import com.triquang.customer.CustomerService;
import com.triquang.shipping.ShippingRateService;
import com.triquang.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {
	@Autowired private CheckoutService checkoutService;
	@Autowired private CustomerService customerService;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService shipService;
	@Autowired private ShoppingCartService cartService;
	
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
	Customer customer = getAuthenticatedCustomer(request);
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		
		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
			shippingRate = shipService.getShippingRateForAddress(defaultAddress);
		} else {
			model.addAttribute("shippingAddress", customer.toString());
			shippingRate = shipService.getShippingRateForCustomer(customer);
		}
		
		if (shippingRate == null) {
			return "redirect:/cart";
		}
		
		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);
		
		return "checkout/checkout";
}
	

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}	
	
}
