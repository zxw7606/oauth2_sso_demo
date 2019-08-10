package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class OauthSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthSsoServerApplication.class, args);
    }

    @RequestMapping("/oauth/me")
    public String me(HttpServletRequest request) {
        System.out.println("invoke" + request.getRequestURI());
        return "hello";
    }
}
