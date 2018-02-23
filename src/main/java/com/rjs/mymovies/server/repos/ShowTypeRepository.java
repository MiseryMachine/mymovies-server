package com.rjs.mymovies.server.repos;

import com.rjs.mymovies.server.model.ShowType;

import java.util.List;

public interface ShowTypeRepository extends BaseRepository<ShowType> {
    List<ShowType> findAllByOrderByName();
    ShowType findByName(String name);
}
