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

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Oder_details")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
	private Integer order_detail_id;
	
//	@ManyToOne
//	@JoinColumn(name = "order_id", referencedColumnName = "order_id")
//	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
//	private Order order;
	
	
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	Order order;
	
	@Column(name = "totalprice")
	private Double totalPrice;
	
	@Column(name = "totalquantity")
	private Integer totalQuantity;
}
