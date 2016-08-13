package com.spring.german.controller;

import com.spring.german.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloWorldController {

    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public String showGallery(Model model) {
        return "gallery";
    }

        @RequestMapping(value = "/showForm", method = RequestMethod.GET)
    public String showForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "hello-world-form";
    }

    @RequestMapping(value = "/processForm", method = RequestMethod.POST)
    public String processForm(@ModelAttribute(value = "student") Student student, Model model) {
        String obtainedName = student.getName();
        int age = 15;
        String newName = obtainedName + " evolved";
        Student result = new Student(newName, age);
        model.addAttribute("student", result);
        return "hello-world";
    }

}
