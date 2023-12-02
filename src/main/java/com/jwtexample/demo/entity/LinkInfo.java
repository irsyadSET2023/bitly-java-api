package com.jwtexample.demo.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "slug")
    private String slug;
    @Column(name = "link",unique = true)
    private String link;
    @Column(name="visit_counter")
    private int visitCounts;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private UserInfo owner;
    @Column(name = "created_at") 
	private LocalDateTime createdAt;
	@Column(name = "updated_at") 
	private LocalDateTime updatedAt;
}
