package com.triquang.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.triquang.common.entity.Brand;
import com.triquang.common.entity.Category;
import com.triquang.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositotyTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 38);
		Category category = entityManager.find(Category.class, 6);

		Product product = new Product();
		product.setName("Dell");
		product.setAlias("Dell");
		product.setShortDescription("A good smartphone from Apple");
		product.setFullDescription("This is a very good smartphone full description");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(999);
		product.setCost(500);
		product.setEnabled(true);
		product.setInStock(true);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProducts = productRepository.save(product);
		assertThat(savedProducts).isNotNull();
		assertThat(savedProducts.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProdutcs = productRepository.findAll();

		iterableProdutcs.forEach(System.out::println);
	}

	@Test
	public void testGetProduct() {
		Integer id = 2;
		Product product = productRepository.findById(id).get();
		System.out.println(product);

		assertThat(product).isNotNull();
	}

	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = productRepository.findById(id).get();
		product.setPrice(199);

		productRepository.save(product);

		Product updatedProduct = entityManager.find(Product.class, id);
		assertThat(updatedProduct.getPrice()).isEqualTo(199);
	}

	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		productRepository.deleteById(id);

		Optional<Product> result = productRepository.findById(id);

		assertThat(!result.isPresent());
	}

	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = productRepository.findById(productId).get();

		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image 1.png");
		product.addExtraImage("extra_image_2.png");
		product.addExtraImage("extra-image3.png");

		Product savedProduct = productRepository.save(product);
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}

}
