package com.tin.entity;

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
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Favorites")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avorite_id")
	private Integer favorite_id;

	
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Product product;
	
	@Column(name = "likedate")
	@CreationTimestamp
	private Timestamp likedate;
}
