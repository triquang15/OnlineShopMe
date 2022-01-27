package com.triquang.admin.category;


import org.springframework.data.repository.PagingAndSortingRepository;

import com.triquang.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

	
}
