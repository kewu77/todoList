package com.example.todolist.Entity.DTO;

import lombok.Data;

@Data
public class TodoRequestDTO {

    private String text;

    private Boolean done;
}
