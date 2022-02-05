package com.triquang.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.triquang.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;

	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Sam Sung");
		Category savedCategory = repo.save(category);

		assertThat(savedCategory.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(1);
		Category subCategory = new Category("RAM", parent);
		Category savedCategory = repo.save(subCategory);

		assertThat(savedCategory.getId()).isGreaterThan(0);

	}

	@Test
	public void testGetCategory() {
		Category category = repo.findById(1).get();
		System.out.println(category.getName());

		Set<Category> children = category.getChildren();

		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}

		assertThat(children.size()).isGreaterThan(0);
	}

	@Test
	public void testPrintCategories() {
		Iterable<Category> categories = repo.findAll();

		for (Category rootCategories : categories) {
			if (rootCategories.getParent() == null) {
				System.out.println(rootCategories.getName());

				Set<Category> children = rootCategories.getChildren();
				for (Category subCategory : children) {
					System.out.println(subCategory.getName());
					printChildrent(rootCategories, 1);
				}
			}
		}
	}

	private void printChildrent(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.print("----");
			}
			System.out.println(subCategory.getName());

			printChildrent(subCategory, newSubLevel);
		}
	}

	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repo.findRootCategories(Sort.by("name").ascending());
		rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}

	@Test
	public void testByFindName() {
		String name = "Sam Sung";
		Category category = repo.findByName(name);

		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}

	@Test
	public void testFindByAlias() {
		String alias = "Iphone 12 Pro Max";
		Category category = repo.findByAlias(alias);

		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);
	}

}
