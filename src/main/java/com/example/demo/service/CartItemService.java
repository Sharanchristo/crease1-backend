package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.CartItemException;
import com.example.demo.Exception.UserException;
import com.example.demo.Repository.CartItemRepo;
import com.example.demo.Repository.CartRepo;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;

@Service
public class CartItemService {
	@Autowired
	private CartItemRepo cir;
	private UserService us;
	private CartRepo cr;
	public CartItemService(CartItemRepo cir, UserService us, CartRepo cr) {
		super();
		this.cir = cir;
		this.us = us;
		this.cr = cr;
	}

	
	
public CartItem createCartItem(CartItem cartItem) {
	cartItem.setQuantity(1);
	cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
	cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());
	
	CartItem createdCartItem=cir.save(cartItem);
	
	return createdCartItem;
	}
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException{
		CartItem item=findCartItemById(id);
		User user=us.findUserById(item.getUserId());
		
		
		if(user.getId().equals(userId)) {
			
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity()*item.getProduct().getPrice());
			item.setDiscountedPrice(item.getQuantity()*item.getProduct().getDiscountedPrice());
			
			return cir.save(item);
				
			
		}
		else {
			throw new CartItemException("You can't update  another users cart_item");
		}
			}
	
	public CartItem isCartItemExist(Cart cart,Product product,String size, Long userId) {
CartItem cartItem=cir.isCartItemExist(cart, product, size, userId);
		
		return cartItem;
	}
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException{
System.out.println("userId- "+userId+" cartItemId "+cartItemId);
		
		CartItem cartItem=findCartItemById(cartItemId);
		
		User user=us.findUserById(cartItem.getUserId());
		User reqUser=us.findUserById(userId);
		
		if(user.getId().equals(reqUser.getId())) {
			cir.deleteById(cartItem.getId());
		}
		else {
			throw new UserException("you can't remove anothor users item");
		}
	}
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException{
			 Optional<CartItem> opt=cir.findById(cartItemId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new CartItemException("cartItem not found with id : "+cartItemId);
	}

	}

