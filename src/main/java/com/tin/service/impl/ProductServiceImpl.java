package com.tin.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tin.entity.Category;
import com.tin.entity.OrderDetail;
import com.tin.entity.Product;
import com.tin.repository.ProductRepo;
import com.tin.service.OrderDetailsService;
import com.tin.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private OrderDetailsService orderDetailsService;

	@Override
	public List<Product> findByNewProduct() {
		return productRepo.findByNewProduct();
	}

	@Override
	public List<Product> findAllProduct(Integer number) {
		return productRepo.findAllProduct(number);
	}

	@Override
	public Page<Product> filterByPrice(Double min, Double max, Pageable page) {
		return productRepo.filterByPrice(min, max, page);
	}

	@Override
	public Page<Product> filterByCate(Integer id, Pageable pageable) {
		return productRepo.filterByCate(id, pageable);
	}



	// truy vấn sản phẩm theo category
	@Override
	public List<Product> findByCategoryId(Integer cid) {
		return productRepo.findByCategoryId(cid);
	}

	// truy vấn sản phẩm theo id
	@Override
	public Product findById(Integer id) {
		return productRepo.findById(id).get();
	}

	@Override
	public List<Product> productHomepage() {
		return productRepo.productHomepage();
	}

	@Override
	public List<Product> searchByKeyword(String name) {
		return productRepo.searchByKeyword(name);
	}


	//
	@Override
	public Page<Product> findAllByNameLike(String keywords, Pageable page) {
		return productRepo.findAllByNameLike(keywords, page);
	}

	@Override
	public List<Product> FindByIdTop4(Integer cid) {
		return productRepo.FindByIdTop4(cid);
	}

	@Override
	public List<Product> findProductOutstanding() {
		return productRepo.findProductOutstanding();
	}

	@Override
	public List<Product> slideCategory() {
		return productRepo.slideCategory();
	}

	@Override
	public List<Product> findAll() {
		return productRepo.findAll();
	}

	@Override
	public List<Object[]> countMostBuys() {
		return productRepo.countMostBuys();
	}

	@Override
	@Transactional
	public void updateQuantity(Integer newQuantity, Integer productId) {
		productRepo.updateQuantity(newQuantity, productId);
	}
	
	/*ADMIN*/
	@Override
	public void save(Product product) {
		productRepo.save(product);
	}
	
	@Override
	public Product findByProductId(Integer id) {
		return productRepo.findByProductId(id);
	}

	@Override
	public Product create(Product product) {
		return productRepo.save(product);
	}

	@Override
	public Product update(Product product) {
//		Product product2 = productRepo.findByProductId(product.getProduct_id());
//		product.setQuantity(product.getQuantity() + product2.getQuantity());
		return productRepo.save(product);
	}

	@Override
	public void delete(Integer id) {
		productRepo.deleteById(id);
	}
	
	@Override
	public List<Product> findByKeyword(String keyword) {
		return productRepo.findByKeyword(keyword);
	}

	@Override
	@Transactional
	public void deleteLogical(Integer id) {
		List<OrderDetail>list = orderDetailsService.findAll();
		for (int i = 0; i < list.size(); i++) {
			if(id == list.get(i).getProduct().getProduct_id()) {
				System.out.println("Người dùng đang sử dụng dịch vụ");
				throw new RuntimeException();
			}else {
				productRepo.deleteLogical(id);
			}
		}
		
	}

	@Override
	@Transactional
	public void updateLogical(Integer id) {
		productRepo.updateLogical(id);
	}

	@Override
	public List<Product> findProductEnable() {
		return productRepo.findProductEnable();
	}
}
