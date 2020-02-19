package com.wangpanlong.applicant.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wangpanlong.applicant.dao.ArticleMapper;
import com.wangpanlong.applicant.dao.ArticleRep;
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.service.ArticleService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-beans.xml")
public class ImportMysql2ES {

	@Autowired
	ArticleMapper articleMapper;
	@Autowired
	ArticleRep articleRep;
	
	@Test
	public void importMysql2es(){
		
		List<Article> findAllArticlesWithStatus = articleMapper.findAllArticlesWithStatus(1);
		articleRep.saveAll(findAllArticlesWithStatus);
		
	}
	
}
