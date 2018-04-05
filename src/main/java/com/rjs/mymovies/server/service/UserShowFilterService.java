package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.model.UserShowFilter;
import com.rjs.mymovies.server.repos.UserShowFilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserShowFilterService extends BaseService<UserShowFilter, UserShowFilterRepository> {
    private UserShowFilterRepository userShowFilterRepository;

    @Autowired
    public UserShowFilterService(UserShowFilterRepository userShowFilterRepository) {
        super(userShowFilterRepository);

        this.userShowFilterRepository = userShowFilterRepository;
    }

    public List<UserShowFilter> findByUserId(Long userId) {
        return userShowFilterRepository.findByUser_Id(userId);
    }

    public List<UserShowFilter> findByUsername(String username) {
        return userShowFilterRepository.findByUser_Username(username);
    }
}
