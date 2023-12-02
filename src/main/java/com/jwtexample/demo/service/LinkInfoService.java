package com.jwtexample.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwtexample.demo.entity.LinkInfo;
import com.jwtexample.demo.entity.UserInfo;
import com.jwtexample.demo.repository.LinksInfoRepository;
@Service
public class LinkInfoService {
    @Autowired
    LinksInfoRepository repository;

    public LinkInfo createLink(LinkInfo linkInfo,UserInfo userInfo){
        linkInfo.setLink(linkInfo.getLink());
        linkInfo.setSlug(generateRandomString(8));
        linkInfo.setVisitCounts(0);
        linkInfo.setCreatedAt(LocalDateTime.now());
        linkInfo.setUpdatedAt(LocalDateTime.now());
        linkInfo.setOwner(userInfo);
        repository.save(linkInfo);
        return linkInfo;
    }

    public List<LinkInfo> getLinksByUser(UserInfo userInfo) {
        return repository.findByOwner(userInfo);
    }

    public LinkInfo updateLink(String newLink,String slug){
        LinkInfo selectedLinkInfo=repository.findBySlug(slug);
        selectedLinkInfo.setLink(newLink);
        repository.save(selectedLinkInfo);
        return selectedLinkInfo;
    
    }

    public void deleteLink(String slug){
        LinkInfo selectedLinkInfo=repository.findBySlug(slug);
        repository.delete(selectedLinkInfo);
    }

    public String addVisitCount(String slug){
        LinkInfo selectedLinkInfo=repository.findBySlug(slug);
        System.out.println(selectedLinkInfo.getOwner());
        int visitCounts=selectedLinkInfo.getVisitCounts();
        visitCounts+=1;
        selectedLinkInfo.setVisitCounts(visitCounts);
        repository.save(selectedLinkInfo);
        return selectedLinkInfo.getLink();
    }

    public static String generateRandomString(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
