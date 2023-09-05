package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.UserException;
import com.example.demo.Repository.UserRepo;
import com.example.demo.config.JwtTokenProvider;
import com.example.demo.model.User;


@Service
public class UserService {
	@Autowired
	private UserRepo ur;
	public UserService(UserRepo ur, JwtTokenProvider jw) {
		super();
		this.ur = ur;
		this.jw = jw;
	}

	private JwtTokenProvider jw;

	
	
	public User findUserById(Long userId) throws UserException {
        Optional<User> userOptional = ur.findById(userId);
        
        if (userOptional.isPresent()) { 
            return userOptional.get(); 
        } else {
            throw new UserException("User not found with id " + userId);
        }
    }
	
	public User findUserProfileByJwt(String jwt) throws UserException {
		System.out.println("user service");
		String email=jw.getEmailFromJwtToken(jwt);
		
		System.out.println("email"+email);
		
		User user=ur.findByEmail(email);
		
		
		
		if(user==null) {
			throw new UserException("user not exist with email "+email);
		}
		System.out.println("email user"+user.getEmail());
		return user;
	}
}
