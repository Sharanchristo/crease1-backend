package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.OrderException;
import com.example.demo.Repository.AddressRepo;
import com.example.demo.Repository.OrderItemRepo;
import com.example.demo.Repository.OrderRepo;
import com.example.demo.Repository.UserRepo;
import com.example.demo.model.Address;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.User;
import com.example.user.domain.OrderStatus;
import com.example.user.domain.PaymentStatus;



@Service
public class OrderService {
	@Autowired
	private UserRepo ur;
	public OrderService(UserRepo ur, CartService cs, OrderRepo or, AddressRepo ar, OrderItemRepo oir,OrderItemService ois) {
		this.ur = ur;
		this.cs = cs;
		this.or = or;
		this.ar = ar;
		this.oir = oir;
		this.ois=ois;
	}
@Autowired
	private CartService cs;
	@Autowired
	private OrderRepo or;
	@Autowired
	private AddressRepo ar;
	@Autowired
	private OrderItemRepo oir;
	private OrderItemService ois;
	

	
	
public Order createOrder(User user, Address shippAddress) {

	shippAddress.setUser(user);
	Address address= ar.save(shippAddress);
	user.getAddresses().add(address);
	ur.save(user);
	
	Cart cart=cs.findUserCart(user.getId());
	List<OrderItem> orderItems=new ArrayList<>();
	
	for(CartItem item: cart.getCartItems()) {
		OrderItem orderItem=new OrderItem();
		
		orderItem.setPrice(item.getPrice());
		orderItem.setProduct(item.getProduct());
		orderItem.setQuantity(item.getQuantity());
		orderItem.setSize(item.getSize());
		orderItem.setUserId(item.getUserId());
		orderItem.setDiscountedPrice(item.getDiscountedPrice());
		
		
		OrderItem createdOrderItem=oir.save(orderItem);
		
		orderItems.add(createdOrderItem);
	}
	
	
	Order createdOrder=new Order();
	createdOrder.setUser(user);
	createdOrder.setOrderItems(orderItems);
	createdOrder.setTotalPrice(cart.getTotalPrice());
	createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
	createdOrder.setDiscounte(cart.getDiscounte());
	createdOrder.setTotalItem(cart.getTotalItem());
	
	createdOrder.setShippingAddress(address);
	createdOrder.setOrderDate(LocalDateTime.now());
	createdOrder.setOrderStatus(OrderStatus.PENDING);
	createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
	createdOrder.setCreatedAt(LocalDateTime.now());
	
	Order savedOrder=or.save(createdOrder);
	
	for(OrderItem item:orderItems) {
		item.setOrder(savedOrder);
		oir.save(item);
	}
	
	return savedOrder;
			}
	
	public Order findOrderById(Long orderId) throws OrderException{
Optional<Order> opt=or.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("order not exist with id "+orderId);
	}
	
	public List<Order> usersOrderHistory(Long userId){
		List<Order> orders=or.getUsersOrders(userId);
		return orders;
	}
	
	public Order placedOrder(Long orderId) throws OrderException{
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.PLACED);
		order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
		return order;
	}
	
	public Order confirmedOrder(Long orderId)throws OrderException{
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CONFIRMED);
		
		
		return or.save(order);
	}
	
	public Order shippedOrder(Long orderId) throws OrderException{
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.SHIPPED);
		return or.save(order);
	}
	
	public Order deliveredOrder(Long orderId) throws OrderException{
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.DELIVERED);
		return or.save(order);
	}
	
	public Order cancledOrder(Long orderId) throws OrderException{
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CANCELLED);
		return or.save(order);
	}
	
	public List<Order>getAllOrders(){
		return or.findAll();
	}
	
	public void deleteOrder(Long orderId) throws OrderException{
	
		Order order =findOrderById(orderId);
		
		or.deleteById(orderId);
	}
	

}
