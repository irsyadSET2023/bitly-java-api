package com.jwtexample.demo.repository;

import com.jwtexample.demo.entity.LinkInfo;
import com.jwtexample.demo.entity.UserInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository; 

@Repository
public interface LinksInfoRepository extends JpaRepository <LinkInfo,Integer> {
        List<LinkInfo> findByOwner(UserInfo userInfo);

        LinkInfo findBySlug(String slug);

}
