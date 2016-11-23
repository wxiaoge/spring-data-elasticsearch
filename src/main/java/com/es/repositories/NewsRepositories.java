package com.es.repositories;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import com.es.vo.News;

/**
 * 
 * @Description: TODO
 * @ClassName: UserRepositories 
 * @author xiaoge
 * @date: 2016年11月4日 下午1:44:00
 *
 */
public interface NewsRepositories extends ElasticsearchRepository<News, Long>{
	
	

	List<News> findByTitle(String title); 
	
	List<News> findByContent(String content);
}
