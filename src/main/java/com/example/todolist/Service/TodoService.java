package com.example.todolist.Service;

import com.example.todolist.Entity.DTO.TodoRequestDTO;
import com.example.todolist.Entity.Todo;
import com.example.todolist.Exception.TodoNotFoundException;
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

    public Todo update(Integer id, TodoRequestDTO todoRequestDTO){
        Todo todo = todoRepository.findById(id).orElse(null);
        if(todo == null){
            throw new TodoNotFoundException();
        }
        todo.setText(todoRequestDTO.getText());
        todo.setDone(todoRequestDTO.getDone());
        return todoRepository.save(todo);
    }

    public void delete(Integer id){
        Todo todo = todoRepository.findById(id).orElse(null);
        if(todo == null){
            throw new TodoNotFoundException();
        }
        todoRepository.deleteById(id);
    }

    public Todo getById(Integer id) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if(todo == null){
            throw new TodoNotFoundException();
        }
        return todo;
    }
}
