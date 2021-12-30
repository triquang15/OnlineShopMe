package com.triquang.admin.user;

import org.springframework.data.repository.CrudRepository;

import com.triquang.common.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
