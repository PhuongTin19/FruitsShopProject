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
import com.tin.entity.Discount;
import com.tin.entity.Product;
import com.tin.entity.Report;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
	@Query("SELECT p FROM Product p WHERE p.quantity > 0 and is_enable = 0")
	List<Product> findProductEnable();
	
	@Query("SELECT p FROM Product p WHERE p.category.category_id=?1")
	List<Product> findByCategoryId(Integer cid);

	// Top 4 sản phẩm mới nhất
	@Query(value = "select TOP 4 * from products order by createdDate desc", nativeQuery = true)
	List<Product> findByNewProduct();

	// truy xuất mặt hàng(bao gồm có khuyến mãi)
	@Query(value = "select * from Products where name like ? and quantity > 0 and is_enable = 0", nativeQuery = true)
	Page<Product> findAllByNameLike(String keywords, Pageable page);

	// Hiển thị ra top sản phẩm mới nhất theo loại - trang khuyến mãi
	@Query(value = "select TOP 3 * from Products where is_enable = 0 and category_id = ?1", nativeQuery = true)
	List<Product> findAllProduct(Integer number);

	// Lọc sản phẩm theo giá - trang khuyến mãi
	@Query(value = "select p from Product p where p.is_enable = 0 and (p.price - (p.price * p.discount.discount/100)) between ?1 and ?2")
	Page<Product> filterByPrice(Double min, Double max, Pageable pageable);

	// Lọc sản phẩm theo loại - trang khuyến mãi
	@Query(value = " select p from Product p where p.is_enable = 0 and p.category.category_id = ?1")
	Page<Product> filterByCate(Integer id, Pageable pageable);

	// Sản phẩm mới nhất - Trang chủ
	@Query(value = "select TOP 3 * from Products where is_enable = 0 order by createdDate desc", nativeQuery = true)
	List<Product> productHomepage();

	// top4 sản phẩm liên quan để hiển thị ở trang detail
	@Query(value = "select TOP 4 * from Products where category_id = ? and is_enable = 0", nativeQuery = true)
	List<Product> FindByIdTop4(Integer cid);

	// Tìm kiểm theo keyword
	@Query(value = "select * from products where name like ?1 and is_enable = 0", nativeQuery = true)
	List<Product> searchByKeyword(String name);

	// Top sản phẩm nổi bật - Trang chủ
	@Query(value = "select TOP 8 * from Products where quantity> 0 and is_enable = 0", nativeQuery = true)
	List<Product> findProductOutstanding();

	// Top loại hàng nổi bật - Trang chủ
	@Query(value = "select TOP 4 * from Products where quantity> 0 and is_enable = 0", nativeQuery = true)
	List<Product> slideCategory();

	// Lượt mua nhiều nhất
	@Query(value = "select TOP 3 p.name,p.price,p.image,p.product_id,p.category_id,d.discount,count(od.product_id) "
			+ "from Products p\r\n" + "inner join Oder_details od\r\n" + "on od.product_id = p.product_id\r\n"
			+ "inner join Discounts d\r\n" + "on d.discount_id = p.discount_id\r\n"
			+ " where p.is_enable = 0 group by p.name,p.price,p.image,p.product_id,p.category_id,d.discount\r\n"
			+ "order by count(od.product_id) desc", nativeQuery = true)
	List<Object[]> countMostBuys();

	// update số lượng
	@Modifying
	@Transactional
	@Query(value = "UPDATE products SET quantity = ? WHERE product_id = ?", nativeQuery = true)
	void updateQuantity(Integer newQuantity, Integer productId);

	/* ADMIN */

	@Query(value = "select p from Product p where p.product_id = ?1")
	Product findByProductId(Integer id);

	// Thống kê tồn kho
	@Query("SELECT new Report(o.category.name, sum(o.price), sum(o.quantity)) " + " FROM Product o " + " GROUP BY o.category.name"
			+ " ORDER BY sum(o.price) DESC")
	List<Report> getInventoryByCategory();
	
	@Query("SELECT new Report(o.name, sum(o.price), sum(o.quantity)) " + " FROM Product o WHERE o.category.name = ?1" + " GROUP BY o.name"
			+ " ORDER BY sum(o.price) DESC ")
	List<Report> getInventoryByCategoryDetail(String categoryName);
	
	@Query(value = "select p from Product p where p.name like %?1%")
	List<Product> findByKeyword(String keyword);
	
	@Modifying
	@Query(value="update Products set is_enable = 1 where product_id=?", nativeQuery=true)
	void deleteLogical(Integer id);
	
	@Modifying
	@Query(value="update Products set is_enable = 0 where product_id=?", nativeQuery=true)
	void updateLogical(Integer id);
}
