package com.tin.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tin.entity.Category;
import com.tin.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
	@Query("SELECT p FROM Product p WHERE p.category.category_id=?1")
	List<Product> findByCategoryId(Integer cid);

	//Top 4 sản phẩm mới nhất
	@Query(value= "select TOP 4 * from products order by createdDate desc", nativeQuery = true)
	List<Product>findByNewProduct();
	
	// truy xuất mặt hàng(bao gồm có khuyến mãi)
	Page<Product>findAllByNameLike(String keywords,Pageable page);
	
	//Hiển thị ra top sản phẩm mới nhất theo loại - trang khuyến mãi
	@Query(value= "select TOP 3 * from Products where is_enable = 1 and category_id = ?1", nativeQuery = true)
	List<Product>findAllProduct(Integer number);
	
	//Lọc sản phẩm theo giá - trang khuyến mãi
	@Query(value= "select p from Product p where p.discount.discount_id = 1 and p.is_enable = 1 and (p.price - (p.price * p.discount.discount/100)) between ?1 and ?2")
	Page<Product>filterByPrice(Double min,Double max,Pageable pageable);
	
	//Lọc sản phẩm theo loại - trang khuyến mãi
	@Query(value=" select p from Product p where p.discount.discount_id = 1 and p.is_enable = 1 and p.category.category_id = ?1")
	Page<Product>filterByCate(Integer id,Pageable pageable);
	
	//Sản phẩm mới nhất - Trang chủ
	@Query(value= "select TOP 3 * from Products order by createdDate desc", nativeQuery = true)
	List<Product>productHomepage();
	
	// top4 sản phẩm liên quan để hiển thị ở trang detail
	@Query(value= "select TOP 4 * from Products where category_id = ?",nativeQuery = true)
	List<Product>FindByIdTop4(Integer cid); 
	
	//Tìm kiểm theo keyword
	@Query(value="select * from products where name like ?1",nativeQuery = true)
	List<Product>searchByKeyword(String name);
	
	//Top sản phẩm nổi bật - Trang chủ
	@Query(value= "select TOP 8 * from Products",nativeQuery = true)
	List<Product>findProductOutstanding();
	//Top loại hàng nổi bật - Trang chủ
	@Query(value= "select TOP 4 * from Products",nativeQuery = true)
	List<Product>slideCategory();
	//Lượt mua nhiều nhất 
		@Query(value = "select TOP 3 p.name,p.price,p.image,p.product_id,p.category_id,d.discount,count(od.product_id) "
				+ "from Products p\r\n"
				+ "inner join Oder_details od\r\n"
				+ "on od.product_id = p.product_id\r\n"
				+ "inner join Discounts d\r\n"
				+ "on d.discount_id = p.discount_id\r\n"
				+ "group by p.name,p.price,p.image,p.product_id,p.category_id,d.discount\r\n"
				+ "order by count(od.product_id) desc",nativeQuery=true)
		List<Object[]> countMostBuys();
	//update số lượng
	@Modifying
	@Transactional
	@Query(value = "UPDATE products SET quantity = ? WHERE product_id = ?", nativeQuery = true)
	void updateQuantity(Integer newQuantity, Integer productId);
	
	/*ADMIN*/

	@Query(value = "select p from Product p where p.product_id = ?1")
	Product findByProductId(Integer id);
}
