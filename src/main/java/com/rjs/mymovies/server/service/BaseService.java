package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.model.AbstractElement;
import com.rjs.mymovies.server.repos.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class BaseService<E extends AbstractElement, R extends BaseRepository<E>> {
    protected BaseRepository<E> repository;

    protected BaseService(BaseRepository<E> repository) {
        this.repository = repository;
    }

    public List<E> getAll() {
        return repository.findAll();
    }

    public List<E> getAll(Sort sort) {
        return repository.findAll(sort);
    }

    public Page<E> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<E> getMatching(E example) {
        Example<E> ex = Example.of(example);

        return repository.findAll(ex);
    }

    public List<E> getMatching(E example, Sort sort) {
        // TODO: make sure this is correct
        Example<E> ex = Example.of(example);

        return repository.findAll(ex, sort);
    }

    public Page<E> getMatching(E example, Pageable pageable) {
        // TODO: make sure this is correct
        return repository.findAll(Example.of(example), pageable);
    }

    public E get(Long id) {
        return repository.findOne(id);
    }

    public List<E> get(Iterable<Long> ids) {
        return repository.findAll(ids);
    }

    public E save(E element) {
        return repository.save(element);
    }

    public List<E> save(Iterable<E> elements) {
        return repository.save(elements);
    }

    public void delete(E element) {
        delete(element.getId());
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public void delete(Iterable<E> elements) {
        repository.deleteInBatch(elements);
    }

    public void deleteAll() {
        repository.deleteAllInBatch();
    }
}
