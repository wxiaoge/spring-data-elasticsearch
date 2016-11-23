package com.es.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.completion.Completion;

@Document(indexName="es",type="people")
public class People {

	
	@Id
	private Long id;
	
	private Integer age;
	
	private String name;
	
	@CompletionField(analyzer="ik")
	private Completion suggest;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Completion getSuggest() {
		return suggest;
	}

	public void setSuggest(Completion suggest) {
		this.suggest = suggest;
	}

	@Override
	public String toString() {
		return "People [id=" + id + ", age=" + age + ", name=" + name + ", suggest=" + suggest + "]";
	}


	
	
	
	
}
