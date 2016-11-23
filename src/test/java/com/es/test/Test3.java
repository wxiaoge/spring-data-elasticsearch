package com.es.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wltea.analyzer.lucene.IKAnalyzer5x;

import com.es.repositories.PeopleRepositories;
import com.es.util.IkAnalzyerUtil;
import com.es.vo.People;

@ContextConfiguration(locations = {"classpath:com/es/config/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
//defaultRollback=true不会改变数据库，false会改变数据库
public class Test3 {

	@Autowired
	private PeopleRepositories peopleRepositories;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Test
	public void test1(){
//		People people = new People();
//		people.setId(1l);
//		people.setAge(23);
//		people.setName("小哥");
//		String[] input = new String[]{"很帅","喜欢学习","爱睡觉","贪玩"};
//		Completion completion = new Completion(input);
//		people.setSuggest(completion);
//		peopleRepositories.save(people);
		
		People people = new People();
		people.setId(4l);
		people.setAge(29);
		people.setName("王频");
		String[] input = new String[]{"爱护花草","喜欢吃饭","不喝酒","爱写代码","爱看书"};
		Completion completion = new Completion(input);
		people.setSuggest(completion);
		peopleRepositories.save(people);
	}
	
	//@Test
	public void test2(){
		SuggestionBuilder<CompletionSuggestionBuilder> suggestionBuilder = new CompletionSuggestionBuilder("complete").text("爱").field("suggest").size(10);
		SuggestResponse response = elasticsearchTemplate.suggest(suggestionBuilder, "es");
		if (response.getSuggest()!=null && response.getSuggest().getSuggestion("complete")!=null) {
			@SuppressWarnings("unchecked")
			List<? extends Suggest.Suggestion.Entry<Suggest.Suggestion.Entry.Option>> list = (List<? extends Suggest.Suggestion.Entry<Suggest.Suggestion.Entry.Option>>) response.getSuggest().getSuggestion("complete").getEntries();
			for (Suggest.Suggestion.Entry<Suggest.Suggestion.Entry.Option> e : list) {
	            for (Suggest.Suggestion.Entry.Option option : e) {
	            	System.out.println(option.getText().toString());
	            }
	        }
		}
	}
	
	//@Test
	public void test3(){
		String str =  "剩下的是completion 、context suggester。 这两个的使用与上面个使用方法完全不一样，上面都在查询的时候根据制定字段内容来做提示，而这两种是需要在mapping 里面定制suggester字段。使用时完全匹配时提示；";
		//String str = "中华人民共和国";
		List<String> list = IkAnalzyerUtil.segmentPhraseByIk(str);
		for (String string : list) {
			System.out.print(string+"|");
		}
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Analyzer analyzer = new IKAnalyzer5x(true);
        TokenStream ts = analyzer.tokenStream("field",
                new StringReader(
                    "剩下的是completion 、context suggester。 这两个的使用与上面个使用方法完全不一样，上面都在查询的时候根据制定字段内容来做提示，而这两种是需要在mapping 里面定制suggester字段。使用时完全匹配时提示；"));

        OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
        try {
            ts.reset();
            while (ts.incrementToken()) {
                System.out.print(offsetAtt.toString()+"|");
            }
            ts.end();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            try {
				ts.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
