package com.wangpanlong.applicant.dao;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.wangpanlong.applicant.entity.Favorite;
import com.wangpanlong.applicant.entity.User;

public interface UserMapper {

	@Select(" SELECT id,username,password FROM cms_user "
			+ " WHERE username = #{value} limit 1")
	User findUserByName(String username);

	@Insert("INSERT INTO cms_user(username,password,locked,create_time,score,role)"
			+ " VALUES(#{username},#{password},0,now(),0,0)")
	int add(@Valid User user);

	@Select("SELECT id,username,password,nickname,birthday,"
			+ "gender,locked,create_time createTime,update_time updateTime,url,"
			+ "role FROM cms_user WHERE username=#{username}  AND password = #{password} "
			+ " LIMIT 1")
	User findByPwd(User user);

	@Select("select * from cms_favorite")
	List<Favorite> favoriteList();

}
