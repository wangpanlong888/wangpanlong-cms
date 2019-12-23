package com.wangpanlong.applicant.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.wangpanlong.applicant.entity.Slide;

public interface SlideMapper {

	@Select("SELECT id,title,picture,url FROM cms_slide ORDER BY id ")
	List<Slide> list();

}
