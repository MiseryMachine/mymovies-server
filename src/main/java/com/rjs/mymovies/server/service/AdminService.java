package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.repos.PageSortBuilder;
import com.rjs.mymovies.server.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminService extends BaseService<User, UserRepository> {
    private ExampleMatcher defaultStringMatcher = ExampleMatcher.matching()
        .withMatcher("username", ExampleMatcher.GenericPropertyMatcher::ignoreCase)
        .withMatcher("username", ExampleMatcher.GenericPropertyMatcher::contains);
    private PageSortBuilder pageSortBuilder = new PageSortBuilder("username");

    @Autowired
    public AdminService(UserRepository userRepository) {
        super(userRepository);
    }

    public User login(String username, String password) throws Exception {
        User user = ((UserRepository) repository).findByUsernameAndPassword(username, password);

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
            return repository.findAll(pageRequest);
        }

        return repository.findAll(Example.of(exampleUser, defaultStringMatcher), pageRequest);
    }

    public List<User> getUserList(User exampleUser) {
        pageSortBuilder.sortDirection(Sort.Direction.DESC);
        if (exampleUser == null) {
            return repository.findAll(pageSortBuilder.getSort());
        }

        return repository.findAll(Example.of(exampleUser, defaultStringMatcher), pageSortBuilder.getSort());
    }

    public boolean hasRoles(HttpServletRequest request, String... roleNames) {
        for (String roleName : roleNames) {
            if (request.isUserInRole(roleName)) {
                return true;
            }
        }

        return false;
    }
}
