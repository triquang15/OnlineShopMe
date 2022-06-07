package com.triquang.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.triquang.common.entity.Customer;
import com.triquang.common.entity.Review;
import com.triquang.common.entity.order.OrderStatus;
import com.triquang.common.entity.product.Product;
import com.triquang.common.exception.ReviewNotFoundException;
import com.triquang.order.OrderDetailRepository;

@Service
public class ReviewService {
	public static final int REVIEWS_PER_PAGE = 5;

	@Autowired
	private ReviewRepository repo;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	public Page<Review> listByCustomerByPage(Customer customer, String keyword, int pageNum, String sortField,
			String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

		if (keyword != null) {
			return repo.findByCustomer(customer.getId(), keyword, pageable);
		}

		return repo.findByCustomer(customer.getId(), pageable);
	}

	public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException {
		Review review = repo.findByCustomerAndId(customer.getId(), reviewId);
		if (review == null)
			throw new ReviewNotFoundException("Customer doesn not have any reviews with ID " + reviewId);

		return review;
	}

	public Page<Review> list3MostRecentReviewsByProduct(Product product) {
		Sort sort = Sort.by("reviewTime").descending();
		Pageable pageable = PageRequest.of(0, 3, sort);

		return repo.findByProduct(product, pageable);
	}

	public Page<Review> listByProduct(Product product, int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

		return repo.findByProduct(product, pageable);
	}

	public boolean didCustomerReviewProduct(Customer customer, Integer productId) {
		Long count = repo.countByCustomerAndProduct(customer.getId(), productId);

		return count > 0;
	}

	public boolean canCustomerReviewAndProduct(Customer customer, Integer productId) {
		Long count = orderDetailRepository.countByProductAndCustomerAndOrderStatus(productId, customer.getId(),
				OrderStatus.DELIVERED);

		return count > 0;
	}
}
