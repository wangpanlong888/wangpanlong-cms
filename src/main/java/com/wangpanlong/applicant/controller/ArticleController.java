package com.wangpanlong.applicant.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.entity.Comment;
import com.wangpanlong.applicant.entity.Complain;
import com.wangpanlong.applicant.entity.User;
import com.wangpanlong.applicant.service.ArticleService;
import com.wangpanlong.cms.utils.StringUtils;

@Controller
@RequestMapping("article")
public class ArticleController extends BaseController{

	@Autowired
	ArticleService articleService;
	
	
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
