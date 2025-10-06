package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.UserService;

@RestController
@RequestMapping("/java")
public class ReviewController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @GetMapping("/latest")
    public String getLatestUser() {
        try {
            String latestEmployeeName = userService.getLatestUser().getName();
            logger.info(latestEmployeeName);
            return latestEmployeeName;
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return "Userが見つかりませんでした。";
        }

    }
}