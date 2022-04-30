package com.triquang.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.common.entity.CartItem;
import com.triquang.common.entity.Customer;
import com.triquang.common.entity.Product;

import antlr.collections.List;

@Service
public class ShoppingCartService {
	@Autowired
	private CartItemRepository cartRepo;

	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);

		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);

		if (cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			if(updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add more " + quantity + " items");
			}

		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updatedQuantity);
		cartRepo.save(cartItem);

		return updatedQuantity;
	}
	
	public java.util.List<CartItem> listCartItems(Customer customer) {
		return cartRepo.findByCustomer(customer);
	}
}
