package ru.gleb.SpringSecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.gleb.SpringSecurity.entity.Movie;
import ru.gleb.SpringSecurity.model.Roles;
import ru.gleb.SpringSecurity.repository.ActorRepository;
import ru.gleb.SpringSecurity.repository.MovieRepository;
import ru.gleb.SpringSecurity.service.UserService;

import java.util.Optional;

@Slf4j
@Controller
public class MovieController {

    @Autowired
    private UserService userService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @GetMapping("/movies/showList")
    public ModelAndView showList(Authentication authentication) {
        var user = userService.findUserByEmail(authentication.getName());

        var movies = user.hasRole(Roles.ROLE_USER)
                ? movieRepository.findAllByCreatedBy(user)
                : movieRepository.findAll();

        var mav = new ModelAndView("movies/list");
        mav.addObject("movies", movies);

        return mav;
    }

    @GetMapping("/movies/showAnalytics")
    public ModelAndView showBoxOffice() {
        var movies = movieRepository.count();
        var total = movieRepository.calculateBox();

        var mav = new ModelAndView("movies/analytics");
        mav.addObject("movies", movies);
        mav.addObject("total", total);

        return mav;
    }

    @GetMapping("/movies/showAddForm")
    public ModelAndView showAddForm() {
        var movie = new Movie();
        var actors = actorRepository.findAll();

        var mav = new ModelAndView("movies/add");
        mav.addObject("movie", movie);
        mav.addObject("actors", actors);

        return mav;
    }

    @GetMapping("/movies/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long id) {
        var movie = movieRepository.findById(id);
        var actors = actorRepository.findAll();

        var mav = new ModelAndView("movies/add");
        mav.addObject("movie", movie);
        mav.addObject("actors", actors);

        return mav;
    }

    @PostMapping("/movies/save")
    public ModelAndView save(@ModelAttribute Movie movie, Authentication authentication) {
        Optional<Movie> oldMovie = (movie.getId() != null) ? movieRepository.findById(movie.getId()) : Optional.empty();
        var createdBy = oldMovie.isEmpty()
                ? userService.findUserByEmail(authentication.getName())
                : oldMovie.get().getCreatedBy();

        movie.setCreatedBy(createdBy);
        movieRepository.save(movie);

        return new ModelAndView("redirect:/movies/showList");
    }

    @GetMapping("/movies/delete")
    public ModelAndView delete(@RequestParam Long id) {
        movieRepository.deleteById(id);

        return new ModelAndView("redirect:/movies/showList");
    }
}
