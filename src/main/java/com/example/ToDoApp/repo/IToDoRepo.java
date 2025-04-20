package com.example.ToDoApp.repo;

import org.springframework.data.repository.CrudRepository;
import com.example.ToDoApp.model.ToDo;

public interface IToDoRepo extends CrudRepository<ToDo, Long> {
}
