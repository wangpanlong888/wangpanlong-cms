package com.wangpanlong.applicant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangpanlong.applicant.common.CmsContant;
import com.wangpanlong.applicant.dao.ArticleMapper;
import com.wangpanlong.applicant.dao.SlideMapper;
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.entity.Category;
import com.wangpanlong.applicant.entity.Channel;
import com.wangpanlong.applicant.entity.Comment;
import com.wangpanlong.applicant.entity.Complain;
import com.wangpanlong.applicant.entity.Slide;
import com.wangpanlong.applicant.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleMapper articleMapper;
	
	@Autowired
	SlideMapper slideMapper;

	@Override
	public int delete(int id) {
		return articleMapper.updateStatus(id,CmsContant.ARTICLE_STATUS_DEL);
	}

	@Override
	public PageInfo<Article> listByUser(Integer id, int page) {
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		
		PageInfo<Article> articlePage = new PageInfo<Article>(articleMapper.listByUser(id));
		return articlePage;
	}

	@Override
	public List<Channel> getChannels() {
		return articleMapper.getAllChannels();
	}

	@Override
	public Article getById(int id) {
		return articleMapper.findById(id);
	}

	@Override
	public List<Category> getCategorisByCid(int cid) {
		return articleMapper.getCateGorisByCid(cid);
	}

	@Override
	public int add(Article article) {
		return articleMapper.add(article);
	}

	@Override
	public int update(Article article, Integer userId) {
		
		Article articleSrc = this.getById(article.getId());
		if(articleSrc.getUserId() != userId){
			
		}
		return articleMapper.update(article);
	}

	@Override
	public PageInfo<Article> list(int status, int page) {
		PageHelper.startPage(page,CmsContant.PAGE_SIZE);
		return new PageInfo<Article>(articleMapper.list(status));
	}

	@Override
	public PageInfo<Article> hotList(int page) {
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<>(articleMapper.hostList());
	}

	@Override
	public List<Article> lastList() {
		return articleMapper.lastList(CmsContant.PAGE_SIZE);
	}

	@Override
	public List<Slide> getSlides() {
		return slideMapper.list();
	}

	@Override
	public PageInfo<Article> getArticles(int channleId, int catId, int page) {
		PageHelper.startPage(page,CmsContant.PAGE_SIZE);
		
		return new PageInfo<Article>(articleMapper.getArticles(channleId, catId));
	}

	@Override
	public List<Category> getCategoriesByChannelId(int channleId) {
		return articleMapper.getCategoriesByChannelId(channleId);
	}

	@Override
	public int addComment(Comment comment) {
		// TODO Auto-generated method stub
		int result =  articleMapper.addComment(comment);
		System.out.println(result);
		 //文章评论数目自增
		if(result>0)
			articleMapper.increaseCommentCnt(comment.getArticleId());
		
		return result;
	}

	@Override
	public PageInfo<Comment> getComments(int articleId, int page) {
		
		PageHelper.startPage(page,CmsContant.PAGE_SIZE);
		return new PageInfo<Comment>(articleMapper.getComments(articleId));
	}

	@Override
	public Article getInfoById(int id) {
		return articleMapper.getInfoById(id);
	}

	@Override
	public int setHot(int id, int status) {
		return articleMapper.setHot(id,status);
	}

	@Override
	public int setCheckStatus(int id, int status) {
		// TODO Auto-generated method stub
		return articleMapper.CheckStatus(id,status);
	}

	@Override
	public int addComplian(Complain complain) {
		int result = articleMapper.addCoplain(complain);
		
		if(result>0)
			articleMapper.increaseComplainCnt(complain.getArticleId());
		return 0;
	}

	@Override
	public PageInfo<Complain> getComplains(int articleId, int page) {
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Complain>(articleMapper.getComplains(articleId));
	}

	@Override
	public List<Complain> complainList() {
		
		return articleMapper.complainList();
	}

	@Override
	public PageInfo<Article> lists(int complainType, int page) {
		PageHelper.startPage(page,CmsContant.PAGE_SIZE);
		return new PageInfo<Article>(articleMapper.lists(complainType));
	}

}
