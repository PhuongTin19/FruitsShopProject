package com.tin.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tin.entity.Favorite;
import com.tin.repository.FavoriteRepo;
import com.tin.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService{

	@Autowired
	FavoriteRepo favoriteRepo;
	
	@Override
	public List<Object[]> findAllFavorite(Integer id) {
		return favoriteRepo.findAllFavorite(id);
	}

	
	@Override
	public List<Object[]> listFavorite() {
		return favoriteRepo.listFavorite();
	}


	@Override
	public void deleteFavorites(Integer id, Integer pid) {
		favoriteRepo.deleteFavorites(id, pid);
	}

	@Override
	public Integer countLike(String username) {
		return favoriteRepo.countLike(username);
	}


	@Override
	public void LikeProducts(Integer account_id, Integer product_id,Timestamp likedate) {
		favoriteRepo.LikeProducts(account_id, product_id,likedate);
	}


	
}
