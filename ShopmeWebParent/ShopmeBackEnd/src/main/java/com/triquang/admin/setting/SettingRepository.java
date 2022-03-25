package com.triquang.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.triquang.common.entity.Setting;
import com.triquang.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	public List<Setting> findByCategory(SettingCategory category);
}
