package com.example.demo.controllers;

import com.example.demo.dto.UrlDto;
import com.example.demo.dto.UrlErrorResponseDto;
import com.example.demo.dto.UrlResponseDto;
import com.example.demo.model.Url;
import com.example.demo.model.User;
import com.example.demo.services.CurrentUserService;
import com.example.demo.services.UrlService;
import com.example.demo.services.UserService;
import com.example.demo.userDetails.CustomUserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class AppController {

    private final UserService userService;
    private final UrlService urlService;
    private final CurrentUserService currentUserService;


    public AppController(UserService userService, UrlService urlService, CustomUserDetailService customUserDetailService, CurrentUserService currentUserService) {
        this.userService = userService;
        this.urlService = urlService;
        this.currentUserService = currentUserService;
    }



    @PostMapping("process_register")
    public String processRegister(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userService.save(user);
        return "register_success";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("listUsers", userService.findAll());
        return "users";
    }

    @GetMapping("/urls")
    public String urls(Model model){
        String email = SecurityContextHolder.getContext()
                        .getAuthentication().getName();
        User user =userService.findByEmail(email);
        currentUserService.setUser(user);
        model.addAttribute("listUrls",urlService.getUrl(user));
        return "urls";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


//Url Controller


 @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto){

        Url urlToRet = urlService.generateShortLink(urlDto, currentUserService.getUser());
        if (urlToRet!=null)
        {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
            urlResponseDto.setShortUrl(urlToRet.getShortLink());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("There was an error processing your request. Please try again.");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
    }


    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink,
                                                   HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(shortLink)){
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Invalid Url");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        Url urlToRet = urlService.getEncodedUrl(shortLink);

        if(urlToRet == null)
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url does not exist or it might have expired! Please try again.");
            urlErrorResponseDto.setStatus("400");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now()))
        {
            urlService.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setError("Url Expired. Please try generating a fresh one.");
            urlErrorResponseDto.setStatus("200");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }

    @PostMapping("/delete/{shortLink}")
    public String deleteShortLink(@PathVariable("shortLink") String shortLink){
        Url url = urlService.getEncodedUrl(shortLink);

        try {
            urlService.deleteShortLink(url);
        }catch (Exception e){
            System.out.println(e);
        }
        return "urls";
    }

}
