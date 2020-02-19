package com.wangpanlong.applicant.service;

import java.util.List;

import javax.validation.Valid;

import com.github.pagehelper.PageInfo;
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.entity.Category;
import com.wangpanlong.applicant.entity.Channel;
import com.wangpanlong.applicant.entity.Comment;
import com.wangpanlong.applicant.entity.Complain;
import com.wangpanlong.applicant.entity.Slide;

public interface ArticleService {

	int delete(int id);

	PageInfo<Article> listByUser(Integer id, int page);

	List<Channel> getChannels();

	Article getById(int id);

	List<Category> getCategorisByCid(int cid);

	int add(Article article);

	int update(Article article, Integer id);

	PageInfo<Article> list(int status, int page);

	PageInfo<Article> hotList(int page);

	List<Article> lastList();

	List<Slide> getSlides();

	PageInfo<Article> getArticles(int channleId, int catId, int page);

	List<Category> getCategoriesByChannelId(int channelId);

	int addComment(Comment comment);

	PageInfo<Comment> getComments(int articleId, int page);

	Article getInfoById(int id);

	int setHot(int id, int status);

	int setCheckStatus(int id, int status);

	int addComplian( Complain complain);

	PageInfo<Complain> getComplains(int articleId, int page);

	List<Complain> complainList();

	PageInfo<Article> lists(int complainType, int page);

	int updaHits(Article article);

	int adds(Article article);

	int deletes(int id);

}
