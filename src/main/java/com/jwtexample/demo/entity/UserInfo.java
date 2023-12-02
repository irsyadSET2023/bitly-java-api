package com.jwtexample.demo.entity;



import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id;
import lombok.AllArgsConstructor; 
import lombok.Data; 
import lombok.NoArgsConstructor; 

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	@Column(name = "username",unique = true) 
	private String name; 
	@Column(name = "email", unique = true) 
	private String email; 
	@Column(name = "password",unique = false) 
	private String password; 
	@Column(name = "roles") 
	private String roles; 
	@Column(name = "created_at") 
	private LocalDateTime createdAt;
	@Column(name = "updated_at") 
	private LocalDateTime updatedAt;

} 

