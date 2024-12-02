package com.example.todolist.Controller;


import com.example.todolist.Entity.DTO.TodoRequestDTO;
import com.example.todolist.Entity.Todo;
import com.example.todolist.Service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAll(){
        return todoService.getAll();
    }

    @PostMapping
    public Todo create(@RequestBody TodoRequestDTO todoRequestDTO){
        return todoService.create(todoRequestDTO);
    }

    @PutMapping
    public Todo update(@RequestBody Todo todo){
        return todoService.update(todo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        todoService.delete(id);
    }
}
