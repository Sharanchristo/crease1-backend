package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.ProductException;
import com.example.demo.Repository.CartRepo;
import com.example.demo.Request.AddItemRequest;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.model.Product;
import com.example.demo.model.CartItem;



@Service
public class CartService {
  @Autowired
	private CartRepo cr;
	public CartService(CartRepo cr, CartItemService cs, ProductService ps,CartItemService cis) {
	this.cr = cr;
	this.cs = cs;
	this.ps = ps;
	this.cis=cis;
}

	private CartItemService cs;
	private ProductService ps;
	private CartItemService cis;
	
	
public Cart createCart(User user) {
	Cart cart = new Cart();
	cart.setUser(user);
	Cart createdCart=cr.save(cart);
	return createdCart;
}
	
	public String addCartItem(Long userId,AddItemRequest req) throws ProductException{
		Cart cart=cr.findByUserId(userId);
		Product product=ps.findProductById(req.getProductId());
		
		CartItem isPresent=cis.isCartItemExist(cart, product, req.getSize(),userId);
		
		if(isPresent == null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			
			
			int price=req.getQuantity()*product.getDiscountedPrice();
			cartItem.setPrice(price);
			cartItem.setSize(req.getSize());
			
			CartItem createdCartItem=cis.createCartItem(cartItem);
			cart.getCartItems().add(createdCartItem);
		}
		
		
		return "Item Add To Cart";
	}
	
	
	public Cart findUserCart(Long userId) {
		Cart cart =	cr.findByUserId(userId);
		int totalPrice=0;
		int totalDiscountedPrice=0;
		int totalItem=0;
		for(CartItem cartsItem : cart.getCartItems()) {
			totalPrice+=cartsItem.getPrice();
			totalDiscountedPrice+=cartsItem.getDiscountedPrice();
			totalItem+=cartsItem.getQuantity();
		}
		
		cart.setTotalPrice(totalPrice);
		cart.setTotalItem(cart.getCartItems().size());
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setDiscounte(totalPrice-totalDiscountedPrice);
		cart.setTotalItem(totalItem);
		
		return cr.save(cart); 
		}


	
}
