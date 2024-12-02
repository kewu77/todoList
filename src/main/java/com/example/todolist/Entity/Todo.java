package com.example.todolist.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    private int id;

    private String text;

    private boolean done;

    public Todo() {
    }

    public Todo(String text, boolean done) {
        this.text = text;
        this.done = done;
    }
}
