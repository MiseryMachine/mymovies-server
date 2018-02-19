package com.rjs.mymovies.server.controllers;

import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users/list")
    public List<User> getAllUsers() {
        return adminService.getUserList(null);
    }

    @GetMapping("/users/paged")
    public Page<User> getAllUsersPaged(@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNumber) {
        return adminService.getUsersPaged(null, pageNumber, 5);
    }
}
