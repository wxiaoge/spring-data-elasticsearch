package com.es.test;

import java.util.Iterator;
import java.util.List;

import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.es.repositories.NewsRepositories;
import com.es.util.IkAnalzyerUtil;
import com.es.vo.News;


@ContextConfiguration(locations = {"classpath:com/es/config/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
//defaultRollback=true不会改变数据库，false会改变数据库
public class Test2 {

	@Autowired
	private NewsRepositories newsRepositories;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	//@Test
	public void test1(){
		News news = new News();
		news.setId(1l);
		news.setTitle("java php");
		news.setContent("小哥对java很熟悉，对php一窍不通");
		List<String> list = IkAnalzyerUtil.segmentPhraseByIk(news.getContent());
		String[] input = list.toArray(new String[list.size()]);
		System.out.println(input);
		news.setSuggest(new Completion(input));
		newsRepositories.save(news);
		
//		News news = new News();
//		news.setId(2l);
//		news.setTitle("cf lol");
//		news.setContent("小哥只会玩cf");
//		List<String> list = IkAnalzyerUtil.segmentPhraseByIk(news.getContent());
//		String[] input = list.toArray(new String[list.size()]);
//		System.out.println(input);
//		news.setSuggest(new Completion(input));
//		newsRepositories.save(news);
	}
	
	
	//@Test
	public void test2(){
		List<News> list = newsRepositories.findByTitle("php");
		for (News news : list) {
			System.out.println(news);
		}
	}
	
	//@Test
	public void test3(){
		List<News> list = newsRepositories.findByContent("cf");
		for (News news : list) {
			System.out.println(news);
		}
	}
	
	//@Test
	public void test4(){
		QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "lol");
		Iterable<News> iterable = newsRepositories.search(queryBuilder);
		Iterator<News> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
	
	//@Test
	public void test5(){
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("cf", "title","content");
		Iterable<News> iterable = newsRepositories.search(queryBuilder);
		Iterator<News> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
	
	@Test
	public void test6(){
		SuggestionBuilder<CompletionSuggestionBuilder> suggestionBuilder = new CompletionSuggestionBuilder("complete").text("一").field("suggest");
		SuggestResponse response = elasticsearchTemplate.suggest(suggestionBuilder, "es");
		if (response.getSuggest()!=null && response.getSuggest().getSuggestion("complete")!=null) {
			List<? extends Suggest.Suggestion.Entry<Suggest.Suggestion.Entry.Option>> list = (List<? extends Suggest.Suggestion.Entry<Suggest.Suggestion.Entry.Option>>) response.getSuggest().getSuggestion("complete").getEntries();
			for (Suggest.Suggestion.Entry<Suggest.Suggestion.Entry.Option> e : list) {
	            for (Suggest.Suggestion.Entry.Option option : e) {
	            	System.out.println(option.getText().toString());
	            }
	        }
		}
		
		
	}
	
}
