package com.example.todolist.Service;

import com.example.todolist.Entity.DTO.TodoRequestDTO;
import com.example.todolist.Entity.Todo;
import com.example.todolist.Repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAll(){
        return todoRepository.findAll();
    }

    public Todo create(TodoRequestDTO todoRequestDTO){
        return todoRepository.save(new Todo(todoRequestDTO.getText(),todoRequestDTO.getDone()));
    }

    public Todo update(Todo todo){
        return todoRepository.save(todo);
    }

    public void delete(Integer id){
        todoRepository.deleteById(id);
    }
}
