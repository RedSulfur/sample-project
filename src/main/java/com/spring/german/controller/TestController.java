package com.spring.german.controller;

import com.spring.german.entity.VerificationToken;
import com.spring.german.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    VerificationTokenRepository repository;

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public String getToken (Model model) {
        log.info("in controller getToken: {}");
        VerificationToken token = repository.findByToken("halo");
        log.info("Token fetched: {}", token.getToken());
        model.addAttribute("token", token.getExpiryDate());

        return "test";
    }
}
