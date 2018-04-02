package com.rjs.mymovies.server.controllers.web;

import com.rjs.mymovies.server.controllers.UserController;
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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserWebController extends UserController {
    @Autowired
    public UserWebController(UserService userService) {
        super(userService);
    }

    @GetMapping("/login-signup")
    public String getLoginSignUp(Model model) {
        model.addAttribute("newUser", new UserDto());
        model.addAttribute("newUserMessage", "");

        return "user/login-signup";
    }

    @PostMapping(value = "/login-signup", params = "action=signup")
    public ModelAndView registerUser(@ModelAttribute("newUser") @Valid UserDto userDto, BindingResult bindingResult, WebRequest webRequest, Errors errors) {
        boolean hasErrors = bindingResult.hasErrors();

        if (!hasErrors) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "password.confirm", "Passwords do not match.");
                hasErrors = true;
            }
        }

        if (!hasErrors) {
            if (userService.userExists(userDto.getUsername())) {
                bindingResult.rejectValue("username", "username.exists", "Username already used.");
                hasErrors = true;
            }
        }

        Map<String, Object> model = new HashMap<>();

        if (!hasErrors) {
            User user = userService.createUser(userDto);
            model.put("newUserMessage", "User " + user.getUsername() + " created.");
            model.put("newUser", new UserDto());
        }
        else {
            userDto.setPassword("");
            userDto.setConfirmPassword("");
            model.put("newUser", userDto);
        }

        return new ModelAndView("user/login-signup", model);
    }

    @GetMapping("/registration")
    public String showUserRegistration(Model model) {
        model.addAttribute("appRoles", Role.getRoleText());
        model.addAttribute("newUser", new UserDto());

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
