package com.es.test;

import java.util.Iterator;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.es.repositories.NewsRepositories;
import com.es.repositories.PeopleRepositories;
import com.es.service.NewsService;
import com.es.vo.News;
import com.es.vo.People;


@ContextConfiguration(locations = {"classpath:com/es/config/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
//defaultRollback=true不会改变数据库，false会改变数据库
public class Test1 {
	
	//@Autowired
	//private EsService esService;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private NewsRepositories newsRepositories;
	
	@Autowired
	private PeopleRepositories peopleRepositories;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	//@Test
	public void test1(){
		News news = new News();
		news.setId(2l);
		news.setTitle("hello world");
		news.setContent("小哥的第N个es代码");
		//esService.addNews(news);
		System.out.println("成功");
	}
	
	//@Test
	public void test2(){
		//esService.deleteNews("2", News.class);
		System.out.println("成功");
	}
	
	
	//@Test
	public void test3(){
		News news = new News();
		news.setId(3l);
		news.setTitle("hello world");
		news.setContent("小哥的第1个es代码");
		newsService.save(news);
	}
	
	
	//@Test
	public void test4(){
		
		for(int i=0;i<5;i++){
			People people = new People();
			people.setAge(i);
			people.setName("xoapge");
			//peopleRepositories.save(people);
		}
	}
	
	
	//@Test
	public void test5(){
		List<News> list = newsRepositories.findByTitle("hello world");
		for (News news : list) {
			System.out.println(news);
		}
	}
	
	
	//@Test
	public void test6(){
		//peopleRepositories.search(query)
		QueryBuilder builder = QueryBuilders.matchQuery("age", 3);
		Iterable<People> iterable = peopleRepositories.search(builder);
		Iterator<People> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			People people = iterator.next();
			System.out.println(people);
		}
	}
	
	
	//@Test
	public void test7(){
		List<People> list = peopleRepositories.findByAge(3);
		for (People people : list) {
			System.out.println(people);
		}
		System.out.println("**********************");
		peopleRepositories.deleteAll();
		List<People> list2 = peopleRepositories.findByName("xoapge");
		for (People people : list2) {
			System.out.println(people);
		}
		
	}
	
	@Test
	public void test8(){
		elasticsearchTemplate.deleteIndex("es");
		//elasticsearchTemplate.suggest(suggestion, clazz)
	}
}
