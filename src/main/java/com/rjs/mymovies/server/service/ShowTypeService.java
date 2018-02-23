package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.model.ShowType;
import com.rjs.mymovies.server.repos.ShowTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowTypeService extends BaseService<ShowType, ShowTypeRepository> {
    @Autowired
    public ShowTypeService(ShowTypeRepository showTypeRepository) {
        super(showTypeRepository);
    }

    public List<ShowType> getAll() {
        return ((ShowTypeRepository) repository).findAllByOrderByName();
    }

    public ShowType get(String showTypeName) {
        return ((ShowTypeRepository) repository).findByName(showTypeName);
    }
}
