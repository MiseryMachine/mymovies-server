package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.model.dto.UserDto;
import com.rjs.mymovies.server.model.security.Role;
import com.rjs.mymovies.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserWebController {
    private UserService userService;

    @Autowired
    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showUserRegistration(Model model) {
        model.addAttribute("appRoles", Role.getRoleText());
        model.addAttribute("user", new UserDto());

        return "user-info-form";
    }

    @GetMapping("/update/{userId}")
    public String showUserUpdate(@PathVariable Long userId, Model model) {
        User user = userService.get(userId);

        if (user == null) {
            // todo
            return "home";
        }

        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(
            user.getRoles()
                .stream()
                .map(r -> r.text)
                .collect(Collectors.toSet())
        );

        model.addAttribute("appRoles", Role.getRoleText());
        model.addAttribute("user", userDto);
        return "user-info-form";
    }

    @PostMapping(value = "/postUser", params = {"action=create"})
    public ModelAndView createUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, WebRequest webRequest, Errors errors) {
        if (userService.userExists(userDto.getUsername())) {
//            bindingResult.rejectValue("username", "message.regError");
            bindingResult.rejectValue("username", "Username already used.");
        }

        if (!bindingResult.hasErrors()) {
            userService.createUser(userDto);
            return new ModelAndView("home", "user", userDto);
        }
        else {
            return new ModelAndView("user-info-form", "user", userDto);
        }
    }

    @PostMapping(value = "/postUser", params = {"action=update"})
    public ModelAndView updateUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, WebRequest webRequest, Errors errors) {
        if (!bindingResult.hasErrors()) {
            userService.updateUser(userDto);
            return new ModelAndView("home", "user", userDto);
        }
        else {
            return new ModelAndView("user-info-form", "user", userDto);
        }
    }

    @PostMapping(value = "/postUser", params = {"action=cancel"})
    public String cancelRegistration() {
        return "home";
    }
}
