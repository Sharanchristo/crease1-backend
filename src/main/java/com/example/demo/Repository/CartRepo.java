package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Cart;

public interface CartRepo extends JpaRepository<Cart, Long>{

	@Query("SELECT c From Cart c where c.user.id=:userId")
	public Cart findByUserId(@Param("userId")Long userId);
}
