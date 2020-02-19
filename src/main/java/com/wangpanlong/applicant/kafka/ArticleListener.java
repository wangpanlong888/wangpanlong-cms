package com.wangpanlong.applicant.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;

import com.alibaba.fastjson.JSON;
import com.wangpanlong.applicant.entity.Article;
import com.wangpanlong.applicant.service.ArticleService;

public class ArticleListener implements MessageListener<String, String>{

	@Autowired
	ArticleService articleService;
	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
//		接收消息
		String jsonString = data.value();
		System.out.println("收到了消息");
		Article article = JSON.parseObject(jsonString,Article.class);
		System.out.println(article);
		//保存到mysql的数据库中
		articleService.adds(article);
	}

}
