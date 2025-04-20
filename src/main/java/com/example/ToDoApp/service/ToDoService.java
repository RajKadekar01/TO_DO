package com.example.ToDoApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ToDoApp.model.ToDo;
import com.example.ToDoApp.repo.IToDoRepo;

@Service
public class ToDoService {
	
	@Autowired
	IToDoRepo repo;

	public List<ToDo> getAllToDoItems() {
		List<ToDo> todoList = new ArrayList<>();
		repo.findAll().forEach(todoList::add);
		return todoList;
	}
	
	public ToDo getToDoItemById(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	public boolean updateStatus(Long id) {
		ToDo todo = getToDoItemById(id);
		if (todo != null) {
			todo.setStatus("Completed");
			return saveOrUpdateToDoItem(todo);
		}
		return false;
	}
	
	public boolean saveOrUpdateToDoItem(ToDo todo) {
		ToDo saved = repo.save(todo);
		return saved != null && getToDoItemById(saved.getId()) != null;
	}
	
	public boolean deleteToDoItem(Long id) {
		repo.deleteById(id);
		return repo.findById(id).isEmpty();
	}

	public boolean updateStatusOnly(Long id, String status) {
	    ToDo todo = getToDoItemById(id);
	    if (todo != null) {
	        todo.setStatus(status);
	        return saveOrUpdateToDoItem(todo);
	    }
	    return false;
	}

	}

