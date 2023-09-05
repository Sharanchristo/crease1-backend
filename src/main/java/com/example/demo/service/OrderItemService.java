package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.OrderItemRepo;
import com.example.demo.model.OrderItem;

@Service
public class OrderItemService {
	@Autowired
		private OrderItemRepo oir;
		public OrderItemService(OrderItemRepo oir) {
			this.oir=oir;
		}
		public OrderItem createOrderItem(OrderItem orderItem) {
			
			return oir.save(orderItem);
		}
		
	}

