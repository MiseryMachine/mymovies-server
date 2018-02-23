package com.rjs.mymovies.server.controllers.web;

//@Controller
//@RequestMapping("/admin")
public class AdminWebController {
    public AdminWebController() {
    }

//    @GetMapping("/adminUsers")
    public String userAdmin() {
        return "adminUsers";
    }
}
