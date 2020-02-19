package com.wangpanlong.applicant.service;

import java.util.List;

import javax.validation.Valid;

import com.wangpanlong.applicant.entity.Favorite;
import com.wangpanlong.applicant.entity.User;

public interface UserService {

	User getUserByUsername(String username);

	int register(@Valid User user);

	User login(User user);

	List<Favorite> favoriteList();

}
