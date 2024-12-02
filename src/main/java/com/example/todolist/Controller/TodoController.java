package com.example.todolist.Controller;

import com.example.todolist.Entity.DTO.TodoRequestDTO;
import com.example.todolist.Entity.Todo;
import com.example.todolist.Service.TodoService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public Todo getById(@PathVariable Integer id){
        return todoService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@RequestBody TodoRequestDTO todoRequestDTO){
        return todoService.create(todoRequestDTO);
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable Integer id, @RequestBody TodoRequestDTO todoRequestDTO){
        return todoService.update(id,todoRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        todoService.delete(id);
    }
}
