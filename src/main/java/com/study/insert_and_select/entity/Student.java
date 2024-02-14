package com.study.insert_and_select.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Student {
	private int StudentId;
	private String name;
	private int age;
}
