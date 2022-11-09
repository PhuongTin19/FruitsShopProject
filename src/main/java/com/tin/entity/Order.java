package com.tin.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Orders")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
	private Integer order_id;
	
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = "payment_method_id", referencedColumnName = "payment_method_id")
	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
	private Payment_Methods payment_method;
	
	@Column(name = "orderdate")
	@CreationTimestamp
	private Timestamp orderdate;
	
	@Column(name = "deliverydate")
	@CreationTimestamp
	private Timestamp deliveryDate;
 
	 
//	@Column(name = "shippingfee")
//	private Double shippingFee;
	
	
	@Column(name = "orderstatus")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String orderStatus;
	
	@Column(name = "notes")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String notes;
	
	@Column(name = "phone")
	private String phone;
	 
	@Column(name = "address")
	@Type(type="org.hibernate.type.StringNVarcharType")
	private String address;
	
	@Column(name = "totalprice")
	private Double totalPrice;
	
	@Column(name = "verification_code")
    private String verificationCode;
	
	@JsonIgnore
	@OneToMany(mappedBy = "order")
	List<OrderDetail> orderDetails;
}
