package com.rjs.mymovies.server.controllers;

import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.model.dto.UserDto;
import com.rjs.mymovies.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserController {
    protected UserService userService;
    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected UserController(UserService userService) {
        this.userService = userService;
    }

    protected UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
