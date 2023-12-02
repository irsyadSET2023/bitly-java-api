package com.jwtexample.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jwtexample.demo.entity.LinkInfo;
import com.jwtexample.demo.entity.UserInfo;
import com.jwtexample.demo.repository.UserInfoRepository;
import com.jwtexample.demo.service.JwtService;
import com.jwtexample.demo.service.LinkInfoService;


@RestController
@RequestMapping("/link")
@CrossOrigin(origins = "*")
public class LinkController {

    @Autowired
    private UserInfoRepository user;
    @Autowired
	private JwtService jwtService; 
    @Autowired
    private LinkInfoService linkService;

    @PostMapping
    public ResponseEntity<Map<String,Object>> createLink(@RequestHeader("Authorization") String authorizationHeader,@RequestBody LinkInfo linkInfo){
        Map<String,Object> userDetails=jwtService.extractUsernameAndUserId(authorizationHeader.substring(7));
        int userId=(int) userDetails.get("id");
        Optional<UserInfo> currentUser=user.findById(userId);
        UserInfo getUser=currentUser.get();
        LinkInfo createdLink=linkService.createLink(linkInfo, getUser);

        Map<String,Object> response=new HashMap<>();

        response.put("message","Link is created");
        response.put("data",createdLink);
        return ResponseEntity.status(HttpStatus.OK)
			.body(response); 
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> getLink(@RequestHeader("Authorization") String authorizationHeader){
        Map<String,Object> userDetails=jwtService.extractUsernameAndUserId(authorizationHeader.substring(7));
        int userId=(int) userDetails.get("id");
        Optional<UserInfo> currentUser=user.findById(userId);
        UserInfo getUser=currentUser.get();

        List<LinkInfo> allLinks=linkService.getLinksByUser(getUser);

        Map<String,Object> response=new HashMap<>();

        response.put("message","All links");
        response.put("data",allLinks);

        return ResponseEntity.status(HttpStatus.OK)
			.body(response); 

    }

    @PutMapping
    public ResponseEntity<Map<String,Object>> updateLink(@RequestHeader("Authorization") String authorizationHeader,@RequestBody LinkInfo linkInfo){
        String newLink=linkInfo.getLink();
        String slug=linkInfo.getSlug();
        LinkInfo updatedLink=linkService.updateLink(newLink, slug);
        Map<String,Object> response=new HashMap<>
        ();
        response.put("message","Link is updated" );
        response.put("data",updatedLink );
        return ResponseEntity.status(HttpStatus.OK)
			.body(response); 
    }

    @DeleteMapping
    public ResponseEntity<Map<String,Object>> deleteLink(@RequestHeader("Authorization") String authorizationHeader,@RequestBody LinkInfo linkInfo){
        String slug=linkInfo.getSlug();
        linkService.deleteLink(slug);
        Map<String,Object> response=new HashMap<>();

        response.put("message", "Link is deleted");
                return ResponseEntity.status(HttpStatus.OK)
			.body(response); 
    }

    @GetMapping("/redirect/{slug}")
    public ModelAndView redirectLink(@PathVariable String slug){
        String redirectLink=linkService.addVisitCount(slug);
        return new ModelAndView(new RedirectView(redirectLink));
    }

    
}
