package com.example.todolist.controller;

import com.example.todolist.Entity.Todo;
import com.example.todolist.Exception.TodoNotFoundException;
import com.example.todolist.Repository.TodoRepository;
import jakarta.annotation.Resource;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TodoControllerTest {
    @Resource
    private MockMvc client;

    @Autowired
    private TodoRepository todoRepository;

    @Resource
    private JacksonTester<List<Todo>> todosJacksonTester;


    @BeforeEach
    public void givenDataToJpa() {
        todoRepository.deleteAll();
        todoRepository.save(new Todo("test1", false));
        todoRepository.save(new Todo("test2", false));
        todoRepository.save(new Todo("test3", false));
        todoRepository.save(new Todo("test4", false));
        todoRepository.save(new Todo("test5", false));
    }

    @Test
    void should_return_todos_when_get_all_given_todo_exist() throws Exception {
        //given
        final List<Todo> givenEmployees = todoRepository.findAll();

        //when
        //then
        final String jsonResponse = client.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        final List<Todo> employeesResult = todosJacksonTester.parseObject(jsonResponse);
        assertThat(employeesResult)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(givenEmployees);
    }

    @Test
    void should_return_todo_when_get_by_id() throws Exception {
        // Given
        final Todo givenTodo = todoRepository.findAll().get(0);

        // When
        // Then
        client.perform(MockMvcRequestBuilders.get("/todos/" + givenTodo.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(givenTodo.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenTodo.getText()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(givenTodo.isDone()));
    }

    @Test
    void should_create_todo_success() throws Exception {
        // Given
        todoRepository.deleteAll();
        String givenText = "Test text";
        boolean givenDone = false;

        String givenEmployee = String.format(
                "{\"text\": \"%s\", \"done\": \"%b\"}",
                givenText,
                givenDone
        );
        // When
        // Then
        client.perform(MockMvcRequestBuilders.post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenEmployee)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(givenDone));
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(1);
        AssertionsForClassTypes.assertThat(todos.get(0).getId()).isNotNull();
        AssertionsForClassTypes.assertThat(todos.get(0).getText()).isEqualTo(givenText);
        AssertionsForClassTypes.assertThat(todos.get(0).isDone()).isFalse();
    }

    @Test
    void should_remove_todo_success() throws Exception {
        // Given
        final List<Todo> givenTodo = todoRepository.findAll();
        int givenId = givenTodo.get(0).getId();

        // When
        // Then
        client.perform(MockMvcRequestBuilders.delete("/todos/" + givenId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(4);
        AssertionsForClassTypes.assertThat(todos.get(0).getId()).isEqualTo(givenTodo.get(1).getId());
        AssertionsForClassTypes.assertThat(todos.get(1).getId()).isEqualTo(givenTodo.get(2).getId());
        AssertionsForClassTypes.assertThat(todos.get(2).getId()).isEqualTo(givenTodo.get(3).getId());
        AssertionsForClassTypes.assertThat(todos.get(3).getId()).isEqualTo(givenTodo.get(4).getId());
    }

    @Test
    void should_update_todo_success() throws Exception {
        // Given
        Integer givenId = 1;
        String givenText = "New Text";
        boolean givenDone = true;

        String givenTodo = String.format(
                "{\"id\": %s, \"text\": \"%s\", \"done\": \"%b\"}",
                givenId,
                givenText,
                givenDone
        );

        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(givenDone));
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(5);
        AssertionsForClassTypes.assertThat(todos.get(0).getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(todos.get(0).getText()).isEqualTo(givenText);
        AssertionsForClassTypes.assertThat(todos.get(0).isDone()).isTrue();

    }

    @Test
    void should_return_TodoNotFoundException_when_get_by_error_id() throws Exception {
        // Given
        final Integer errorId = -1;

        // When
        // Then
        String contentAsString = client.perform(MockMvcRequestBuilders.get("/todos/" + errorId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AssertionsForClassTypes.assertThat(contentAsString).isEqualTo(TodoNotFoundException.THIS_TODO_IS_NOT_EXIST);
    }

    @Test
    void should_remove_todo_fail() throws Exception {
        // Given
        final List<Todo> givenTodo = todoRepository.findAll();
        int givenId = -1;

        // When
        // Then
        String contentAsString = client.perform(MockMvcRequestBuilders.delete("/todos/" + givenId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(givenTodo.size());
        AssertionsForClassTypes.assertThat(contentAsString).isEqualTo(TodoNotFoundException.THIS_TODO_IS_NOT_EXIST);
    }

    @Test
    void should_update_todo_fail() throws Exception {
        // Given
        Integer givenId = -1;
        String givenText = "New Text";
        boolean givenDone = true;

        String givenTodo = String.format(
                "{\"id\": %s, \"text\": \"%s\", \"done\": \"%b\"}",
                givenId,
                givenText,
                givenDone
        );

        // When
        // Then
        String contentAsString = client.perform(MockMvcRequestBuilders.put("/todos/" + givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AssertionsForClassTypes.assertThat(contentAsString).isEqualTo(TodoNotFoundException.THIS_TODO_IS_NOT_EXIST);

    }
}
