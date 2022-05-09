package com.triquang.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.triquang.common.entity.setting.Setting;
import com.triquang.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
	
	@Autowired
	private SettingRepository repo;
	
	@Test
	public void testCreateGeneralSettings() {
		
		//Setting siteName =  new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
		Setting siteName =  new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
		Setting siteLogo =  new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2022 Shopme Ltd", SettingCategory.GENERAL);
		
		repo.saveAll(List.of(siteLogo, copyright));
		
		Iterable<Setting> iterable = repo.findAll();	
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testListSettingsByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);
		
		settings.forEach(System.out :: println);
	}

}
