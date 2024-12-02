package com.example.todolist.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String text;

    private boolean done;

    public Todo() {
    }

    public Todo(int id, String text, boolean done) {
        this.id = id;
        this.text = text;
        this.done = done;
    }

    public Todo(String text, boolean done) {
        this.text = text;
        this.done = done;
    }

}
