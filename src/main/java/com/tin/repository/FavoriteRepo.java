package com.tin.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.tin.entity.Account;
import com.tin.entity.Favorite;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorite,Integer> {
	
	//Những sản phẩm đc thích bởi account nào đó - Trang Yêu Thích
	@Query("Select distinct f.product.name,f.product.price,f.product.image,f.product.product_id,f.product.category.category_id, "
			+ "f.product.discount.discount,f.product.discount.is_enable "
			+ "from Favorite f "
			+ "where f.account.account_id = ?1")
	List<Object[]>findAllFavorite(Integer id);
	 
	//Sản phẩm được thích nhất
	@Query(value="select TOP 3 p.name,p.price,p.image,p.product_id,Categories.name as 'cates',Categories.category_id,p.discount_id "
			+ "from Favorites f\r\n"
			+ "inner join Products p\r\n"
			+ "on f.product_id = p.product_id\r\n"
			+ "inner join Categories \r\n"
			+ "on p.category_id = Categories.category_id \r\n"
			+ "where p.is_enable = 0\r\n"
			+ "group by p.name,p.price,p.image,p.product_id,Categories.name,Categories.category_id,p.discount_id \r\n"
			+ "order by count(p.product_id) desc",nativeQuery = true)
	List<Object[]>listFavorite();
	
	//Đếm số lượt thích
	@Query(value="select count(distinct f.product_id) from Favorites f \r\n"
			+ "inner join accounts a\r\n"
			+ "on f.account_id = a.account_id\r\n"
			+ "where a.username = ?1",nativeQuery = true)
	Integer countLike(String username);
	
	//Unlike
	@Modifying
	@Query(value="delete from favorites where account_id = ?1 and product_id = ?2",nativeQuery = true)
	void deleteFavorites(Integer id,Integer pid);
	
	//Like
	@Modifying
	@Query(value = "insert into favorites(account_id,product_id,likedate) values(?1,?2,?3)",nativeQuery = true)
	void LikeProducts(Integer account_id, Integer product_id,Timestamp likedate);
	//Check list favorite của account có những sản phẩm nào
	@Query(value = "select * from favorites where account_id = ?1",nativeQuery = true)
	List<Favorite> CheckExistProducts(Integer account_id);
	
	//Những sản phẩm được thích bởi account
	@Query(value = "select * from Favorites where account_id = ?1",nativeQuery = true)
	List<Favorite>favorites(Integer id);
	
	//Thống kê các sản phẩm được yêu thích
	@Query(value = "{CALL sp_statsFavorite()}", nativeQuery = true)
	List<Object[]> statsFavorite();

}
