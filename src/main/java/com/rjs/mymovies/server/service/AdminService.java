package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.repos.PageSortBuilder;
import com.rjs.mymovies.server.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    private ExampleMatcher defaultStringMatcher = ExampleMatcher.matching()
        .withMatcher("username", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
        .withMatcher("username", ExampleMatcher.GenericPropertyMatcher::contains);
    private PageSortBuilder pageSortBuilder = new PageSortBuilder("username");

    public AdminService() {
    }

    public User login(String username, String password) throws Exception {
        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user == null) {
            throw new Exception("Invalid username or password.");
        }

        return user;
    }

    public Page<User> getUsersPageed(User exampleUser) {
        return getUsersPaged(exampleUser, 0, 10);
    }

    public Page<User> getUsersPaged(User exampleUser, int pageNumber, int pageSize) {
        PageRequest pageRequest = pageSortBuilder.pageNumber(pageNumber).pageSize(pageSize).getPageRequest();

        if (exampleUser == null) {
            return userRepository.findAll(pageRequest);
        }

        return userRepository.findAll(Example.of(exampleUser, defaultStringMatcher), pageRequest);
    }

    public List<User> getUserList(User exampleUser) {
        pageSortBuilder.sortDirection(Sort.Direction.DESC);
        if (exampleUser == null) {
            return userRepository.findAll(pageSortBuilder.getSort());
        }

        return userRepository.findAll(Example.of(exampleUser, defaultStringMatcher), pageSortBuilder.getSort());
    }
}
