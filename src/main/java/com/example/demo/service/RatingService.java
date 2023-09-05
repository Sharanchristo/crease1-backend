package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.ProductException;
import com.example.demo.Repository.ProductRepo;
import com.example.demo.Repository.RatingRepo;
import com.example.demo.Request.RatingRequest;
import com.example.demo.model.Rating;
import com.example.demo.model.User;
import com.example.demo.model.Product;



@Service
public class RatingService {
	@Autowired
	private RatingRepo rr;
	@Autowired
	private ProductRepo pr;
	public RatingService(RatingRepo rr, ProductService ps,ProductRepo pr) {
		this.rr = rr;
		this.ps = ps;
		this.pr=pr;
	}
@Autowired
	private ProductService ps;
	
	public Rating createRating(RatingRequest req,User user) throws ProductException{
      Product product=ps.findProductById(req.getProductId());
	
		Rating rating=new Rating();
		rating.setUser(user);
		rating.setProduct(product);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		pr.save(product);
		return rr.save(rating);
		
	}
	
	public List<Rating> getProductsRating(Long productId){
		return rr.getAllProductsRating(productId);
	}
}
