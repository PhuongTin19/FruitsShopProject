package com.tin.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tin.entity.Favorite;

public interface FavoriteService {
	
	List<Object[]>findAllFavorite(Integer id);
	
	void deleteFavorites(Integer id,Integer pid);
	
	List<Object[]>listFavorite();
	
	Integer countLike(String username);
	
	void LikeProducts(Integer account_id, Integer product_id,Timestamp likedate);
	
	List<Favorite>favorites(Integer id);
		
	List<Favorite> CheckExistProducts(Integer account_id);
	
	List<Object[]> statsFavorite();
}
