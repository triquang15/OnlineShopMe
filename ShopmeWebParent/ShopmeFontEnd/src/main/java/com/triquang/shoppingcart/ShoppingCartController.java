package com.triquang.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.triquang.Utility;
import com.triquang.common.entity.CartItem;
import com.triquang.common.entity.Customer;
import com.triquang.customer.CustomerService;

@Controller
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService cartService;
	@Autowired
	private CustomerService customerService;

	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = cartService.listCartItems(customer);
		
		float estimatedTotal = 0.0F;
		
		for(CartItem item : cartItems) {
			estimatedTotal += item.getSubtotal();
		}
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("estimatedTotal", estimatedTotal);

		return "cart/shopping_cart";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);

		return customerService.getCustomerByEmail(email);
	}
}
