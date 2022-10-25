package com.tin.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Blogs")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable{



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
	private Integer blog_id;
	
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Account account;
	
	@Column(name = "title")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String title;
	
	@Column(name = "description", nullable = false, columnDefinition = "varchar(max)" )
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String description;
	
	@Column(name = "createdate")
	@CreationTimestamp
	private Timestamp createdate;
	

	@Column(name = "image")
	private String image;

	
}
