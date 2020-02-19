package com.wangpanlong.applicant.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.entity.Category;
import com.wangpanlong.applicant.entity.Channel;
import com.wangpanlong.applicant.entity.Favorite;
import com.wangpanlong.applicant.entity.User;
import com.wangpanlong.applicant.service.ArticleService;
import com.wangpanlong.applicant.service.UserService;
import com.wangpanlong.cms.utils.FileUtils;
import com.wangpanlong.cms.utils.HtmlUtils;
import com.wangpanlong.cms.utils.StringUtils;

@Controller
@RequestMapping("user")
public class UserController {

	@Value("${upload.path}")
	String picRootPath;
	
	@Value("${pic.path}")
	String picUrl;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ArticleService articleService;
	
	@RequestMapping("home")
	public String home(){
		return "user/home";
	}
	
	@RequestMapping("logout")
	public String home(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().removeAttribute(CmsContant.USER_KEY);
		
		
		Cookie cookieUserName = new Cookie("username", "");
		cookieUserName.setPath("/");
		cookieUserName.setMaxAge(0);// 立即过期
		response.addCookie(cookieUserName);
		Cookie cookieUserPwd = new Cookie("userpwd", "");
		cookieUserPwd.setPath("/");
		cookieUserPwd.setMaxAge(0);// 立即过期
		response.addCookie(cookieUserPwd);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="register",method=RequestMethod.GET)
	public String  register (HttpServletRequest request){
		
		User user = new User();
		request.setAttribute("user", user);
		
		return "user/register";
	}
	
	@RequestMapping(value="register",method=RequestMethod.POST)
	public String register(HttpServletRequest request,
			@Valid @ModelAttribute("user") User user,
			BindingResult result){
		
		if(result.hasErrors()){
			return "user/register";
		}
		
		User exisUser=userService.getUserByUsername(user.getUsername());
		if(exisUser!=null){
			result.rejectValue("username", "", "用户名字已存在");
			return "user/register";
		}
		
		if(StringUtils.isNumber(user.getPassword())){
			result.rejectValue("password", "", "密码不能全是数字");
			return "user/register";
		}
		
		int reRegister = userService.register(user);
		
		if(reRegister<1){
			request.setAttribute("eror", "注册失败，请稍后再试！");
			
			return  "user/register";
		}
		
		return "redirect:login";
	}
	
	
	
	@RequestMapping(value="login",method=RequestMethod.GET)
	public String login(HttpServletRequest request){
		return "user/login";
	}
	
	@RequestMapping(value="login",method=RequestMethod.POST)
	public String  login(HttpServletRequest request,HttpServletResponse response,User user){
		String pwd =  new String(user.getPassword());
		User loginUser = userService.login(user);
		
		//登录失败
		if(loginUser==null) {
			request.setAttribute("error", "用户名密码错误");
			return "/user/login";	
		}
		
		// 登录成功，用户信息存放看到session当中
		request.getSession().setAttribute(CmsContant.USER_KEY, loginUser);
		
		//保存用户的用户名和密码
		Cookie cookieUserName = new Cookie("username", user.getUsername());
		cookieUserName.setPath("/");
		cookieUserName.setMaxAge(10*24*3600);// 10天
		response.addCookie(cookieUserName);
		Cookie cookieUserPwd = new Cookie("userpwd", pwd);
		cookieUserPwd.setPath("/");
		cookieUserPwd.setMaxAge(10*24*3600);// 10天
		response.addCookie(cookieUserPwd);
		
		// 进入管理界面
		if(loginUser.getRole()==CmsContant.USER_ROLE_ADMIN)
			 return "redirect:/admin/index";	
		
		// 进入个人中心
		return "redirect:/user/home";
	}
	
	@RequestMapping("checkname")
	@ResponseBody
	public boolean checkUserName(String username) {
		User existUser = userService.getUserByUsername(username);
		return existUser==null;
	}
	
	@RequestMapping("deletearticle")
	@ResponseBody
	public boolean deleteArticle(Integer id){
		
		int result = articleService.delete(id);
		
		return  result > 0;
	}
	
	@RequestMapping("deletefavorite")
	@ResponseBody
	public boolean deleteFavorite(Integer id){
		
		int result = articleService.deletes(id);
		
		return result>0;
		
	}
	
	@RequestMapping("articles")
	public String articles(HttpServletRequest request,@RequestParam(defaultValue="1") int page) {
		
		User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
		System.out.println(loginUser);
		PageInfo<Article> articlePage = articleService.listByUser(loginUser.getId(),page);
		System.out.println(articlePage.getList());
		request.setAttribute("articlePage", articlePage);
		
		return "user/article/list";
	}
	
	@RequestMapping("favorite")
	public String favorite(HttpServletRequest request){
		
		
		List<Favorite> favoriteList = userService.favoriteList();
		
		request.setAttribute("favoriteList", favoriteList);
		
		return "user/favorite/list";
		
	}
	
	@RequestMapping("comments")
	public String comments(){
		return "user/comment/list";
	}
	
	//1111
	@RequestMapping("postArticle")
	public String postArticle(HttpServletRequest request){
		
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
		
		return "user/article/post";
		
	}
	
	@RequestMapping(value = "postArticle",method=RequestMethod.POST)
	@ResponseBody
	public boolean postArticle(HttpServletRequest request, Article article, 
			MultipartFile file
			) {
		
		
		String picUrl;
		try {
			// 处理上传文件
			picUrl = processFile(file);
			article.setPicture(picUrl);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
		article.setUserId(loginUser.getId());
		
		
		return articleService.add(article)>0;
	}
	@RequestMapping(value="updateArticle",method=RequestMethod.GET)
	public String updateArticle(HttpServletRequest request,int id) {	
		
		//获取栏目
		List<Channel> channels= articleService.getChannels();
		request.setAttribute("channels", channels);
		
		//获取文章
		Article article = articleService.getById(id);
		User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
		if(loginUser.getId() != article.getUserId()) {
			// todo 准备做异常处理的！！
		}
		request.setAttribute("article", article);
		request.setAttribute("content1",  HtmlUtils.htmlspecialchars(article.getContent()));
		
		
		return "user/article/update";
	}
	
	@RequestMapping("getCategoris")
	@ResponseBody
	public List<Category> getCategoris(Integer cid){
		
		List<Category> categoris = articleService.getCategorisByCid(cid);
		
		return categoris;
	}
	
	@RequestMapping(value="postAricle",method=RequestMethod.POST)
	@ResponseBody
	public boolean postAricle(HttpServletRequest request,Article article,
			MultipartFile file){
		
		String picUrl;
		
		try {
			picUrl = processFile(file);
			article.setPicture(picUrl);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		User loginUser = (User) request.getSession().getAttribute(CmsContant.USER_KEY);
		article.setUserId(loginUser.getId());
		System.out.println(article);
		return articleService.add(article)>0;
	}
	
	@RequestMapping(value="updateArticle",method=RequestMethod.POST)
	@ResponseBody
	public  boolean  updateArticle(HttpServletRequest request,Article article,MultipartFile file) {
		
		System.out.println("aarticle is  "  + article);
		
		String picUrl;
		try {
			// 处理上传文件
			picUrl = processFile(file);
			article.setPicture(picUrl);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//当前用户是文章的作者
		User loginUser = (User)request.getSession().getAttribute(CmsContant.USER_KEY);
		int updateREsult  = articleService.update(article,loginUser.getId());
		
		
		return updateREsult>0;
		
	}
	
	private String processFile(MultipartFile file) throws IllegalStateException, IOException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String subPath = sdf.format(new Date());
		
		File path = new File(picRootPath+"/"+ subPath);
		
		if(!path.exists())
			path.mkdirs();
		
		String suffixName = FileUtils.getSuffixName(file.getOriginalFilename());
		
		String fileName = UUID.randomUUID().toString()+suffixName;
		
		file.transferTo(new File(picRootPath+"/" + subPath + "/" + fileName));
		
		return subPath + "/" + fileName;
	}
}
