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

@Table(name = "Products")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable{

	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
	private Integer product_id;
	
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "category_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Brand brand;
	
	@ManyToOne
	@JoinColumn(name = "discount_id", referencedColumnName = "discount_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Discount discount;
	
	@Column(name = "name")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String name;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "createddate")
	@CreationTimestamp
	private Timestamp createdate;
	
	@Column(name = "is_enable")
	private Boolean is_enable;
	
	@Column(name = "quantity")
	private Integer quantity;
	

	@Column(name = "image")
	private String image;

	
	@Column(name = "description")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String description;
	
	@Column(name = "weight")
	private Double weight;

}
