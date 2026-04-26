package com.example.demo.service;

import com.example.demo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TodoService {
    List<Todo> findAll();
    Optional<Todo> findById(Long id);
    void save(Todo todo);
    void deleteById(Long id);
}
