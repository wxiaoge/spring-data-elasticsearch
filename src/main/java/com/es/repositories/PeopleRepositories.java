package com.es.repositories;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.es.vo.People;

public interface PeopleRepositories extends ElasticsearchRepository<People, Long>{

	List<People> findByAge(Integer age);
	
	List<People> findByName(String name);
}
