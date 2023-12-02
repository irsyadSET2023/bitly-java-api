package com.jwtexample.demo.controller;

import com.jwtexample.demo.entity.AuthRequest;
import com.jwtexample.demo.entity.UserInfo;
import com.jwtexample.demo.repository.UserInfoRepository;
import com.jwtexample.demo.service.JwtService; 
import com.jwtexample.demo.service.UserInfoService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth") 
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
public class UserController { 
    @Autowired
    private UserInfoRepository user;

	@Autowired
	private UserInfoService service; 

	@Autowired
	private JwtService jwtService; 

	@Autowired
	private AuthenticationManager authenticationManager; 

	@GetMapping("/welcome") 
	public String welcome() { 
		return "Welcome this endpoint is not secure"; 
	} 

	@PostMapping("/addNewUser") 
	public String addNewUser(@RequestBody UserInfo userInfo) { 
		return service.addUser(userInfo); 
	} 

	@PostMapping("/register") 
	public ResponseEntity<Map<String,Object>> register(@RequestBody UserInfo userInfo) { 
		UserInfo registeredUser=service.registerUser(userInfo);
		Map<String,Object> response=new HashMap<String,Object>();
		response.put("message", "Login Successful");
		response.put("data", registeredUser);
	 	return ResponseEntity.status(HttpStatus.OK)
		.body(response);
	}

	@PostMapping("/login") 
	public ResponseEntity<Map<String,Object>> login(@RequestBody AuthRequest authRequest) { 
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())); 
		UserInfo loginUser=user.findByName(authRequest.getUsername()).get();
        if (authentication.isAuthenticated()) { 
			String jwtToken= jwtService.generateToken(authRequest.getUsername(), loginUser.getId());
			Map<String,Object> response=new HashMap<String,Object>();
			response.put("message", "Login Successful");
			response.put("data", loginUser);
			response.put("jwt", jwtToken);
			return ResponseEntity.status(HttpStatus.OK)
			.body(response); 
		} else { 
			throw new UsernameNotFoundException("invalid user request !"); 
		} 

	}

	@CrossOrigin("*")
	@GetMapping("/protected") 
	public ResponseEntity<Map<String,Object>> getProtected(@RequestHeader("Authorization") String authorizationHeader) { 
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "http://localhost:5173");
		Map<String,Object> userDetails=jwtService.extractUsernameAndUserId(authorizationHeader.substring(7));
		Map<String,Object> response=new HashMap<String,Object>();
		Object userId=userDetails.get("id");
		response.put("user", userId);
		System.out.println(userDetails);
		return ResponseEntity.status(HttpStatus.OK)
			.body(response); 
	}


	@GetMapping("/user/userProfile")
	public String userProfile(@RequestHeader("Authorization") String authorizationHeader) { 
		Map<String,Object> userDetails=jwtService.extractUsernameAndUserId(authorizationHeader.substring(7));
		System.out.println(userDetails);
		return "Welcome to User Profile"; 
	} 


	@GetMapping("/admin/adminProfile")
	public String adminProfile(@RequestHeader("Authorization") String authorizationHeader) { 
		return "Welcome to Admin Profile"; 
	} 

	@PostMapping("/generateToken") 
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) { 
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())); 
    
		UserInfo selectedUser=user.findByName(authRequest.getUsername()).get();
        if (authentication.isAuthenticated()) { 
			return jwtService.generateToken(authRequest.getUsername(), selectedUser.getId()); 
		} else { 
			throw new UsernameNotFoundException("invalid user request !"); 
		} 
	} 

} 

