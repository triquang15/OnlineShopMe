package com.triquang.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.triquang.common.entity.order.OrderStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderDetailRepositoryTest {
	
	@Autowired
	private OrderDetailRepository repository;
	
	@Test
	public void testCountByProductAndCustomerAndStatus() {
		Integer productId = 99;
		Integer customerId = 1;
		
		Long count = repository.countByProductAndCustomerAndOrderStatus(productId, customerId, OrderStatus.DELIVERED);
		assertThat(count).isGreaterThan(0);
	}
	

}
