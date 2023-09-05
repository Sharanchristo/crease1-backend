package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.ProductException;
import com.example.demo.Repository.ProductRepo;
import com.example.demo.Repository.ReviewRepo;
import com.example.demo.Request.ReviewRequest;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.model.User;



@Service
public class ReviewService {
	
	public ReviewService(ReviewRepo rr, ProductService ps, ProductRepo pr) {
		this.rr = rr;
		this.ps = ps;
		this.pr = pr;
	}
	@Autowired
	private ReviewRepo rr;
	private ProductService ps;
	@Autowired
	private ProductRepo pr;
	
	
	public Review createReview(ReviewRequest req,User user) throws ProductException {
		// TODO Auto-generated method stub
		Product product=ps.findProductById(req.getProductId());
		Review review=new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
//		product.getReviews().add(review);
		pr.save(product);
		return rr.save(review);
	}
public List<Review> getAllReview(Long productId) {
		
		return rr.getAllProductsReview(productId);
	}

}
