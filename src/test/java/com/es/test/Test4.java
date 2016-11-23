package com.es.test;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:com/es/config/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
//defaultRollback=true不会改变数据库，false会改变数据库
public class Test4 {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	//@Test
	public void test1(){
		//聚合
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices("es").withTypes("people")
				.addAggregation(AggregationBuilders.avg("avg").field("age"))
				.addAggregation(AggregationBuilders.max("max").field("age"))
				.addAggregation(AggregationBuilders.min("min").field("age"))
				.addAggregation(AggregationBuilders.sum("sum").field("age"))
				.build();
		
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
		
		//System.out.println(aggregations.get("agg").getMeta);
		Avg avg = aggregations.get("avg");
		System.out.println(avg.getValue());
		
		Max max = aggregations.get("max");
		System.out.println(max.getValue());
		
		Min min = aggregations.get("min");
		System.out.println(min.getValue());
		
		Sum sum = aggregations.get("sum");
		System.out.println(sum.getValue());
	}
	
	
	//@Test
	public void test2(){
		//聚合
				SearchQuery searchQuery = new NativeSearchQueryBuilder()
						.withIndices("es").withTypes("people")
						.addAggregation(AggregationBuilders.stats("agg").field("age"))
						.build();
				
				Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
		            @Override
		            public Aggregations extract(SearchResponse response) {
		                return response.getAggregations();
		            }
		        });
				
				Stats stats = aggregations.get("agg");
				System.out.println(stats.getAvg());
				System.out.println(stats.getCount());
				System.out.println(stats.getMax());
				System.out.println(stats.getMin());
				System.out.println(stats.getSum());
				
	}
	
	
	//@Test
	public void test3(){
		//百分比
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices("es").withTypes("people")
				.addAggregation(AggregationBuilders.percentiles("agg").field("age"))
				.build();
		
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
		
		Percentiles percentiles = aggregations.get("agg");
		for (Percentile entry : percentiles){
			double percent = entry.getPercent();    // Percent
		    double value = entry.getValue();
		    
		    System.out.println(percent+":"+value);
		}
	}
	
	//----------------下面是bucket-----------
	
	//@Test
	public void test4(){
		//全局
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices("es").withTypes("people")
				.addAggregation(AggregationBuilders.global("agg").subAggregation(AggregationBuilders.terms("ages").field("age")))
				.build();
		
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
		
		Global global = aggregations.get("agg");
		System.out.println(global.getDocCount());
		System.out.println(global.getAggregations());
	}
	
	
	@Test
	public void test5(){
		//过滤
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices("es").withTypes("people")
				//.addAggregation(AggregationBuilders.stats("agg").field("age"))
				.addAggregation(AggregationBuilders.filter("filter").filter(QueryBuilders.termQuery("name", "王频"))
						.subAggregation(AggregationBuilders.stats("agg").field("age")))
				.build();
		
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
		
		
		Filter filter = aggregations.get("filter");
		System.out.println(filter.getDocCount());
		
		aggregations = filter.getAggregations();
		Stats stats = aggregations.get("agg");
		System.out.println(stats.getAvg());
		System.out.println(stats.getCount());
		System.out.println(stats.getMax());
		System.out.println(stats.getMin());
		System.out.println(stats.getSum());
		
	}
}
