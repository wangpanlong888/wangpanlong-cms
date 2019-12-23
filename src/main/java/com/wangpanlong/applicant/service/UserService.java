package com.wangpanlong.applicant.service;

import javax.validation.Valid;

import com.wangpanlong.applicant.entity.User;

public interface UserService {

	User getUserByUsername(String username);

	int register(@Valid User user);

	User login(User user);

}
