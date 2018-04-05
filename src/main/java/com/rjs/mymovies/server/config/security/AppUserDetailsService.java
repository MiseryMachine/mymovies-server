package com.rjs.mymovies.server.config.security;

import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.repos.UserRepository;
import com.rjs.mymovies.server.service.UserShowFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    private UserShowFilterService userShowFilterService;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " does not exist.");
        }

        user.setUserShowFilters(userShowFilterService.findByUserId(user.getId()));
        return new UserPrincipal(user);
    }
}
