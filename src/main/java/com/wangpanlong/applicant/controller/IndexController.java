package com.wangpanlong.applicant.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.entity.Category;
import com.wangpanlong.applicant.entity.Channel;
import com.wangpanlong.applicant.entity.Slide;
import com.wangpanlong.applicant.service.ArticleService;

@Controller
public class IndexController {

	@Autowired
	ArticleService articleService;
	
	
	@RequestMapping(value={"index","/"})
	public String index(HttpServletRequest request,@RequestParam(defaultValue="1")int page) throws InterruptedException{
		
		Thread t1 = new Thread(){
			public void run() {
				
				List<Channel> channels = articleService.getChannels();
				request.setAttribute("channels", channels);
					};
		};
		
		Thread t2 = new Thread(){
			public void run() {
				
				PageInfo<Article> articlePage = articleService.hotList(page);
				request.setAttribute("articlePage", articlePage);
			};
		};
		
		Thread t3 = new Thread(){
			public void run() {
				List<Article> lastArticles = articleService.lastList();
				request.setAttribute("lastArticles", lastArticles);
			};
		};
		
		Thread t4 = new Thread(){
			public void run() {
				
				List<Slide> slides = articleService.getSlides();
				
				request.setAttribute("slides", slides);
			};
		};
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		
		return "index";
	}
	
	@RequestMapping("channel")
	public String channel(HttpServletRequest request,
			int channelId,
			@RequestParam(defaultValue="0")int catId,
			@RequestParam(defaultValue="1") int page) throws InterruptedException{
		
		Thread t1 = new Thread(){
			public void run(){
				List<Channel> channels = articleService.getChannels();
				request.setAttribute("channels", channels);
			};
		};
		
		Thread t2 = new Thread(){
			public void run() {
				PageInfo<Article> articlePage = articleService.getArticles(channelId,catId, page);
				request.setAttribute("articlePage", articlePage);
			};
		};
		
		Thread t3 = new Thread(){
			public void run() {
				List<Article> lastArticles = articleService.lastList();
				request.setAttribute("lastArticles", lastArticles);
			};
		};
		
		Thread t4 = new Thread(){
			public void run() {
				List<Slide> slides = articleService.getSlides();
				request.setAttribute("slides", slides);
			};
		};
		
		Thread t5 = new Thread(){
			public void run() {
				List<Category> categoris = articleService.getCategoriesByChannelId(channelId);
				request.setAttribute("categoris", categoris);
				System.err.println("categoris is " + categoris);
			};
		};
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		
		request.setAttribute("catId", catId);
		request.setAttribute("channelId", channelId);
		
		return "channel";
	}
	
}
