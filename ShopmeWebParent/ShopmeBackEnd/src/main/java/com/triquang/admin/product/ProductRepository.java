package com.triquang.admin.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.triquang.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

}
