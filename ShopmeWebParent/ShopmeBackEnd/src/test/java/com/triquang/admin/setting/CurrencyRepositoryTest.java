package com.triquang.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.triquang.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {

	@Autowired
	CurrencyRepository repo;

	@Test
	public void testCreateCurrencies() {
		List<Currency> listCurrencies = java.util.Arrays.asList(

				new Currency("United States Dollar", "$", "USD"), new Currency("Vietnamese", "đ", "VND"),
				new Currency("United States Dollar", "$", "USD")

		);

		repo.saveAll(listCurrencies);
		Iterable<Currency> iterable = repo.findAll();

		assertThat(iterable).size().isEqualTo(3);
	}

	@Test
	public void testListAllByOrderByNameAsc() {

		List<Currency> currencies = repo.findAllByOrderByNameAsc();

		currencies.forEach(System.out::println);

		assertThat(currencies.size()).isGreaterThan(0);
	}

}
