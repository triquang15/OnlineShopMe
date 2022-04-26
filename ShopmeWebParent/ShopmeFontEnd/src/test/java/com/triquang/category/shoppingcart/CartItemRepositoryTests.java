package com.triquang.category.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.triquang.common.entity.CartItem;
import com.triquang.common.entity.Customer;
import com.triquang.common.entity.Product;
import com.triquang.shoppingcart.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {

	@Autowired
	private CartItemRepository repository;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testSaveItem() {
		Integer customerId = 1;
		Integer productId = 1;

		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);

		CartItem newItem = new CartItem();
		newItem.setCustomer(customer);
		newItem.setProduct(product);
		newItem.setQuantity(1);

		CartItem savedItem = repository.save(newItem);

		assertThat(savedItem.getId()).isGreaterThan(0);
	}

	@Test
	public void testSaveItem2() {
		Integer customerId = 4;
		Integer productId = 10;

		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);

		CartItem newItem1 = new CartItem();
		newItem1.setCustomer(customer);
		newItem1.setProduct(product);
		newItem1.setQuantity(3);

		CartItem newItem2 = new CartItem();
		newItem2.setCustomer(customer);
		newItem2.setProduct(product);
		newItem2.setQuantity(2);

		Iterable<CartItem> iterable = repository.saveAll(List.of(newItem1, newItem2));

		assertThat(iterable).size().isGreaterThan(0);
	}

	@Test
	public void testFindByCustomer() {
		Integer customerId = 4;
		List<CartItem> listItems = repository.findByCustomer(new Customer(customerId));

		listItems.forEach(System.out::println);

		assertThat(listItems.size()).isEqualTo(2);
	}

	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId = 1;
		Integer productId = 1;

		CartItem item = repository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

		assertThat(item).isNotNull();

		System.out.println(item);
	}

	@Test
	public void testUpdateQuantity() {
		Integer customerId = 1;
		Integer productId = 1;
		Integer quantity = 4;

		repository.updateQuantity(quantity, customerId, productId);

		CartItem item = repository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

		assertThat(item.getQuantity()).isGreaterThan(1);
	}

	@Test
	public void testDeleteByCustomerAndProduct() {
		Integer customerId = 4;
		Integer productId = 10;

		repository.deleteCustomerAndProduct(customerId, productId);

		CartItem item = repository.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

		assertThat(item).isNull();

	}

}
