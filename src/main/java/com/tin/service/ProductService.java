package com.tin.service;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tin.entity.Category;
import com.tin.entity.Product;

public interface ProductService {
	
	List<Product> findProductEnable();
	
	List<Product>findByNewProduct();
	
	Page<Product>findAllByNameLike(String keywords,Pageable page);
	
	List<Product>findAllProduct(Integer number);
	
	Page<Product>filterByPrice(Double min,Double max,Pageable page);
	
	Page<Product>filterByCate(Integer id,Pageable pageable);
	
	List<Product> findByCategoryId(Integer cid);
	
	List<Product> FindByIdTop4(Integer cid);
	
	Product findById(Integer id);
	
	List<Product>productHomepage();
	
	List<Product>searchByKeyword(String name);
	
	List<Product>findProductOutstanding();
	
	List<Product>slideCategory();

	List<Product> findAll();
	
	List<Object[]> countMostBuys();
	
	void updateQuantity(Integer newQuantity, Integer productId);
	

	/*ADMIN*/
	void save(Product product);
	
	Product findByProductId(Integer id);
	
	Product create(Product product) ;

	Product update(Product product) ;

	void delete(Integer id) ;
	
	void deleteLogical(Integer id);

	void updateLogical(Integer id);
	
	List<Product> findByKeyword(String keyword);
}
