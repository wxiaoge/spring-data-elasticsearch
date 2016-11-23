package com.es.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.es.repositories.NewsRepositories;
import com.es.service.NewsService;
import com.es.vo.News;

@Component
public class NewsServiceImpl implements NewsService {
	
	
	@Autowired
	private NewsRepositories newsRepositories;

	@Override
	public void save(News news) {
		
		newsRepositories.save(news);

	}

}
