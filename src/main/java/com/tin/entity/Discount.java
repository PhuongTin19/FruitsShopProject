package com.tin.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;

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

@Table(name = "Discounts")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discount implements Serializable{


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
	private Integer discount_id;
	
	@Column(name = "name")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String name;
	
	@Column(name = "start_time")
	private Date start_time;

	@Column(name = "end_time")
	private Date end_time;

	@Column(name = "is_enable")
	private Boolean is_enable;
	
	@Column(name = "discount")
	private Double discount;
}
