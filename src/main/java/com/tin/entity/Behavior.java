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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "behavior")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Behavior {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "behaviorid")
	private Integer behaviorId;
	
//	@ManyToOne
//	@JoinColumn(name = "accountId", referencedColumnName = "account_id")
//	@JsonIgnoreProperties(value = {"application", "hibernateLazyInitializer"})
//	private Account account;
	
	@Column(name = "createdate")
	@CreationTimestamp
	private Timestamp createDate = null;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "accountid")
	Account account;
	
	
	
}
