package com.rjs.mymovies.server.repos;

import com.rjs.mymovies.server.model.UserShowFilter;

import java.util.List;

public interface UserShowFilterRepository extends BaseRepository<UserShowFilter> {
    List<UserShowFilter> findByUser_Id(Long userId);
    List<UserShowFilter> findByUser_Username(String username);
}
