package com.triquang.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.common.entity.Category;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	public List<Category> listNoChildrentCategories() {
		List<Category> listNoChildrentCategories = new ArrayList<>();

		List<Category> listEnabledCategories = repo.findAllEnabled();

		listEnabledCategories.forEach(category -> {
			Set<Category> children = category.getChildren();
			if (children == null || children.size() == 0) {
				listNoChildrentCategories.add(category);
			}
		});

		return listNoChildrentCategories;
	}

	public Category getCategory(String alias) {
		return repo.findByAliasEnabled(alias);
	}

	public List<Category> getCategoryParents(Category child) {

		List<Category> listParents = new ArrayList<>();
		Category parent = child.getParent();

		while (parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}

		listParents.add(child);

		return listParents;
	}
}
