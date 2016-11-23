package com.es.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.completion.Completion;


@Document(indexName="es",type="news")
public class News {

	@Id
	//@Field(index = FieldIndex.not_analyzed,store = true)
	private Long id;
	
	
	@Field(type = FieldType.String,analyzer="ik")
	private String title;
	
	@Field(type = FieldType.String,analyzer="ik")
	private String content;
	
	@CompletionField(maxInputLength=100,analyzer="ik")
	private Completion suggest;


	
	
	

	public Completion getSuggest() {
		return suggest;
	}

	public void setSuggest(Completion suggest) {
		this.suggest = suggest;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", content=" + content + "]";
	}
	
	
	
}
