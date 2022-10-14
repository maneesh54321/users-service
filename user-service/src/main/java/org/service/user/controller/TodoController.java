package org.service.user.controller;

import org.service.user.service.todo.TodoService;
import org.service.user.vo.Response;
import org.service.user.vo.TodoForm;
import org.service.user.vo.TodoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todo/create")
    public Response addTodo(@RequestBody String description) {
        TodoVO addedTodo = todoService.createTodo(description);
        Response response = new Response("Todo created!!!", HttpStatus.OK);
        response.addData("todo", addedTodo);
        return response;
    }

    @PostMapping("/todo/edit")
    public Response editTodo(@RequestBody TodoForm todoForm) {
        TodoVO addedTodo = todoService.updateTodo(todoForm);
        Response response = new Response("Todo updated!!!", HttpStatus.OK);
        response.addData("todo", addedTodo);
        return response;
    }

    @DeleteMapping("/todo/delete/{id}")
    public Response deleteTodo(@PathVariable("id") Integer todoId) {
        todoService.deleteTodo(todoId);
        return new Response("Todo deleted!!!", HttpStatus.OK);
    }

    @GetMapping("/todo/{id}")
    public Response getTodo(@PathVariable("id") Integer todoId) {
        TodoVO todo = todoService.getTodo(todoId);
        Response response = new Response("Here is the todo!!", HttpStatus.OK);
        response.addData("todo", todo);
        return response;
    }

    @GetMapping("/todo/all")
    public Response getAllTodos() {
        List<TodoVO> todoVOs = todoService.getAllTodos();
        Response response = new Response("Here are the todos!!", HttpStatus.OK);
        response.addData("todos", todoVOs);
        return response;
    }
}
