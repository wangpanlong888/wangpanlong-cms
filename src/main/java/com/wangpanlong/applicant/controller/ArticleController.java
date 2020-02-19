package com.wangpanlong.applicant.controller;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.wangpanlong.applicant.common.CmsContant;
import com.wangpanlong.applicant.common.CmsError;
import com.wangpanlong.applicant.common.CmsMessage;
import com.wangpanlong.applicant.dao.ArticleRep;
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.entity.Channel;
import com.wangpanlong.applicant.entity.Comment;
import com.wangpanlong.applicant.entity.Complain;
import com.wangpanlong.applicant.entity.Favorite;
import com.wangpanlong.applicant.entity.User;
import com.wangpanlong.applicant.service.ArticleService;
import com.wangpanlong.applicant.utils.HLUtils;
import com.wangpanlong.cms.utils.StringUtils;

@Controller
@RequestMapping("article")
public class ArticleController extends BaseController{

	@Autowired
	ArticleService articleService;

	@Autowired
    RedisTemplate redisTemplate;
	
	@Autowired
	ThreadPoolTaskExecutor executor;
	
	@Autowired
	ArticleRep articleRep;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
//	es搜索的方法
	@RequestMapping("search")
	public String search(String key,Article article,Model model,HttpServletRequest request,@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "2") Integer pageSize){
		
		Thread t1 = new Thread(){
			public void run() {
				
				List<Channel> channels = articleService.getChannels();
				request.setAttribute("channels", channels);
					};
		};
		
//		List<Article> list = articleRep.findByTitle(key);
//		PageInfo<Article> pageInfo = new PageInfo<>(list);
//		model.addAttribute("articlePage",pageInfo);
		
//		利用es实现高量
		long start = System.currentTimeMillis();
		PageInfo<Article> pageInfo = (PageInfo<Article>) HLUtils.findByHighLight(elasticsearchTemplate, Article.class, page, pageSize, new String[]{"title"}, "id", key);
		long end = System.currentTimeMillis();
		System.err.println("本次高量搜索耗时:"+(end-start)+"毫秒");
		model.addAttribute("articlePage",pageInfo);
		model.addAttribute("key",key);
		return "index";
	}
	
	@RequestMapping("getDetail")
	@ResponseBody
	public CmsMessage getDetail(Integer id){
		
		if(id<0){
			
		}
		
		Article article = articleService.getById(id);
		
		if(article==null)
			return new CmsMessage(CmsError.NOT_EXIST,"文章不存在",null);
		
		return new CmsMessage(CmsError.SUCCESS,"",article);
	}
	
	@RequestMapping("detail")
	public String detail(HttpServletRequest request,Integer id){
		Article article = articleService.getById(id);
		request.setAttribute("article", article);
		
		/**
		 * 现在请你利用Redis提高性能，当用户浏览文章时，
		 * 将“Hits_${文章ID}_${用户IP地址}”为key，查询Redis里有没有该key，如果有key，则不做任何操作。
		 * 如果没有，则使用Spring线程池异步执行数据库加1操作，
		 * 并往Redis保存key为Hits_${文章ID}_${用户IP地址}，value为空值的记录，而且有效时长为5分钟。
		 */
		//获取用户ip地址  的方法
		String user_ip = request.getRemoteAddr();
		//准备redis'的key
		String key = "Hits"+id+user_ip;
		//查询redis中的该key
		String redisKey = (String) redisTemplate.opsForValue().get(key);
		if(redisKey==null) {
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					//在这里就可以写具体的逻辑了
					//数据库+1操作(根据id从mysql中查询文章对象)
					//设置浏览量+1
					article.setHits(article.getHits()+1);
					//更新到数据库
					articleService.updaHits(article);
					//并往Redis保存key为Hits_${文章ID}_${用户IP地址}，value为空值的记录，而且有效时长为5分钟。
					redisTemplate.opsForValue().set(key, "",5, TimeUnit.MINUTES);
				}
			});
		}
		
		return "detail";
	}
	
	@RequestMapping("postcomment")
	@ResponseBody
	public CmsMessage postcomment(HttpServletRequest request,int articleId,String content){
		
		User loginUser = (User) request.getSession().getAttribute(CmsContant.USER_KEY);
		
		if(loginUser == null){
			return new CmsMessage(CmsError.NOT_LOGIN,"您尚未登录！",null);
		}
		
		Comment comment = new Comment();
		comment.setUserId(loginUser.getId());
		comment.setContent(content);
		comment.setArticleId(articleId);
		int result = articleService.addComment(comment);
		if(result > 0)
			return new CmsMessage(CmsError.SUCCESS, "成功", null);
		
		return new CmsMessage(CmsError.FAILED_UPDATE_DB, "异常原因失败，请与管理员联系", null);
	}
	
	
	@RequestMapping("comments")
	public String comments(HttpServletRequest request,Integer id,int page) {
		PageInfo<Comment> commentPage =  articleService.getComments(id,page);
		request.setAttribute("commentPage", commentPage);
		return "comments";
	}
	
	/**
	 * 跳转到投诉的页面
	 * @param request
	 * @param articleId
	 * @return
	 */
	@RequestMapping(value="complain",method=RequestMethod.GET)
	public String complain(HttpServletRequest request,int articleId) {
		Article article= articleService.getById(articleId);
		request.setAttribute("article", article);
		request.setAttribute("complain", new Complain());
		return "article/complain";
				
	}
	
	/**
	 * 跳转到收藏的页面
	 * @param request
	 * @param articleId
	 * @return
	 */
	@RequestMapping(value="favorite",method=RequestMethod.GET)
	public String favorite(HttpServletRequest request,int articleId) {
		Article article= articleService.getById(articleId);
		request.setAttribute("article", article);
		request.setAttribute("favorite", new Favorite());
		return "article/favorite";
				
	}
	
	@RequestMapping(value="complain",method=RequestMethod.POST)
	public String complain(HttpServletRequest request,
			@ModelAttribute("complain") @Valid Complain complain,
			MultipartFile file,
			BindingResult result) throws IllegalStateException, IOException {
		
		if(!StringUtils.isUrl(complain.getSrcUrl())) {
			result.rejectValue("srcUrl", "", "不是合法的url地址");
		}
		if(result.hasErrors()) {
			return "article/complain";
		}
		
		User loginUser  =  (User)request.getSession().getAttribute(CmsContant.USER_KEY);
		
		String picUrl = this.processFile(file);
		complain.setPicture(picUrl);
		
		
		//加上投诉人
		if(loginUser!=null)
			complain.setUserId(loginUser.getId());
		else
			complain.setUserId(0);
		
		articleService.addComplian(complain);
		
		return "redirect:/article/detail?id="+complain.getArticleId();
				
	}
	
	@RequestMapping("complains")
	public String 	complains(HttpServletRequest request,int articleId,
			@RequestParam(defaultValue="1") int page) {
		PageInfo<Complain> complianPage=   articleService.getComplains(articleId, page);
		request.setAttribute("complianPage", complianPage);
		return "article/complainslist";
	}
	
}
