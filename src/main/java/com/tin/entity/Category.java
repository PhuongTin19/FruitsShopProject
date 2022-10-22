package com.tin.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Categories")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable{

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
	private Integer category_id;
	
	@Column(name = "name")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String name;
	
	@Column(name = "is_enable")
	private Boolean is_enable;
	
	public Category(String name, Boolean is_enable) {
		super();
		this.name = name;
		this.is_enable = is_enable;
	}
}
