package com.example.todolist.Exception;

public class TodoNotFoundException extends RuntimeException{
    public static final String THIS_TODO_IS_NOT_EXIST = "This Todo is not exist";

    public TodoNotFoundException() {
        super(THIS_TODO_IS_NOT_EXIST);
    }
}
