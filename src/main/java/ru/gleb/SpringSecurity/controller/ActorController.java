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
import ru.gleb.SpringSecurity.entity.Actor;
import ru.gleb.SpringSecurity.model.Roles;
import ru.gleb.SpringSecurity.repository.ActorRepository;
import ru.gleb.SpringSecurity.service.UserService;

import java.util.Optional;

@Slf4j
@Controller
public class ActorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActorRepository actorRepository;

    @GetMapping("/actors/showList")
    public ModelAndView showList(Authentication authentication) {
        var user = userService.findUserByEmail(authentication.getName());

        var actors = user.hasRole(Roles.ROLE_USER)
                ? actorRepository.findAllByCreatedBy(user)
                : actorRepository.findAll();

        var mav = new ModelAndView("actors/list");
        mav.addObject("actors", actors);

        return mav;
    }

    @GetMapping("/actors/showAddForm")
    public ModelAndView showAddForm() {
        var actor = new Actor();

        var mav = new ModelAndView("actors/add");
        mav.addObject("actor", actor);

        return mav;
    }

    @GetMapping("/actors/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long id) {
        var mav = new ModelAndView("actors/add");
        mav.addObject("actor", actorRepository.findById(id));

        return mav;
    }

    @PostMapping("/actors/save")
    public ModelAndView save(@ModelAttribute Actor actor, Authentication authentication) {
        Optional<Actor> oldActor = (actor.getId() != null) ? actorRepository.findById(actor.getId()) : Optional.empty();
        var createdBy = oldActor.isEmpty()
                ? userService.findUserByEmail(authentication.getName())
                : oldActor.get().getCreatedBy();

        actor.setCreatedBy(createdBy);
        actorRepository.save(actor);

        return new ModelAndView("redirect:/actors/showList");
    }

    @GetMapping("/actors/delete")
    public ModelAndView delete(@RequestParam Long id) {
        actorRepository.deleteById(id);

        return new ModelAndView("redirect:/actors/showList");
    }
}
