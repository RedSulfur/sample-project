package com.spring.german.controller;

import com.spring.german.entity.Film;
import com.spring.german.entity.Student;
import com.spring.german.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class HelloWorldController {

    Logger log = LoggerFactory.getLogger(HelloWorldController.class);

    @Autowired
    FilmRepository filmRepository;

    //@ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public ModelAndView showGallery() {
        List<Film> films = filmRepository.findAll();
        log.info("*****************************************");
        log.info("FILMS FETCHED: {}", Arrays.toString(films.toArray()));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("gallery");
        mav.addObject("films", films);

        return mav;
    }

    @RequestMapping(value = "/showForm", method = RequestMethod.GET)
    public String showForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "hello-world-form";
    }

    @RequestMapping(value = "/processForm", method = RequestMethod.POST)
    public String processForm(@ModelAttribute(value = "student")Student student, Model model) {
        String obtainedName = student.getName();
        int age = 15;
        String newName = obtainedName + " evolved";
        Student result = new Student(newName, age);
        model.addAttribute("student", result);
        return "hello-world";
    }

}
