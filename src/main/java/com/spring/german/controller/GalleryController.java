package com.spring.german.controller;

import com.spring.german.entity.Film;
import com.spring.german.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class GalleryController {

    Logger log = LoggerFactory.getLogger(GalleryController.class);

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
}
