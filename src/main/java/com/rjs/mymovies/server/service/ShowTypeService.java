package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.repos.ShowTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShowTypeService extends BaseService<ShowType, ShowTypeRepository> {
    @Autowired
    public ShowTypeService(ShowTypeRepository showTypeRepository) {
        super(showTypeRepository);
    }

    public List<ShowType> getAll() {
        return ((ShowTypeRepository) repository).findAllByOrderByName();
    }

    public Map<String, ShowType> getAllAsMap() {
        return getAll().stream().collect(Collectors.toMap(ShowType::getName, st -> st));
    }

    public ShowType get(String showTypeName) {
        return ((ShowTypeRepository) repository).findByName(showTypeName);
    }
}
