package ru.gleb.SpringSecurity.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.gleb.SpringSecurity.dto.UserDto;
import ru.gleb.SpringSecurity.entity.User;
import ru.gleb.SpringSecurity.model.UserData;
import ru.gleb.SpringSecurity.repository.RoleRepository;
import ru.gleb.SpringSecurity.repository.UserRepository;
import ru.gleb.SpringSecurity.service.UserService;

import java.util.List;

@Controller
public class SecurityController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register/save")
    public String registration(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model
    ) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "На этот адрес электронной почты уже зарегистрирована учетная запись.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);

            return "register";
        }

        userService.saveUser(userDto);

        return "redirect:/register?success";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/showUpdateRolesForm")
    public ModelAndView showUpdateRolesForm(@RequestParam Long id) {
        var optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return new ModelAndView("redirect:/error");
        }

        var user = optionalUser.get();
        var roles = roleRepository.findAll();

        var mav = new ModelAndView("update_roles");
        mav.addObject("user", UserData.setByUser(user));
        mav.addObject("roles", roles);

        return mav;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/saveRoles")
    public ModelAndView save(@ModelAttribute UserData data) {
        var optionalUser = userRepository.findById(data.getId());
        if (optionalUser.isEmpty()) {
            return new ModelAndView("redirect:/error");
        }

        var user = optionalUser.get();
        user.setRoles(data.getRoles());
        userRepository.save(user);

        return new ModelAndView("redirect:/users");
    }
}
