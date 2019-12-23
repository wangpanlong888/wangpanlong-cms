package com.wangpanlong.applicant.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangpanlong.applicant.common.CmsUtils;
import com.wangpanlong.applicant.dao.UserMapper;
import com.wangpanlong.applicant.entity.User;
import com.wangpanlong.applicant.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserMapper userMapper;

	@Override
	public User getUserByUsername(String username) {
		return userMapper.findUserByName(username);
	}

	@Override
	public int register(@Valid User user) {
		
		String encryPwd = CmsUtils.encry(user.getPassword(), user.getUsername());
		
		user.setPassword(encryPwd);
		return userMapper.add(user);
	}

	@Override
	public User login(User user) {
		user.setPassword(CmsUtils.encry(user.getPassword(), user.getUsername()));
		User loginUser = userMapper.findByPwd(user);
		return loginUser;
	}
	
}
