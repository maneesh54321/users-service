package org.service.user.service.todo;

import org.service.user.vo.TodoForm;
import org.service.user.vo.TodoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoVO createTodo(String todoDescription) {
        Todo todo = new Todo(
                todoDescription,
                LocalDate.now(),
                false
        );
        Todo addedTodo = todoRepository.save(todo);
        return new TodoVO(
                addedTodo.getId(),
                addedTodo.getDescription(),
                addedTodo.getCreationTime(),
                addedTodo.getCompleted()
        );
    }

    public TodoVO updateTodo(TodoForm updatedTodo) {
        Todo todo = new Todo(
                updatedTodo.getId(),
                updatedTodo.getDescription(),
                LocalDate.now(),
                updatedTodo.getCompleted()
        );
        Todo updated = todoRepository.save(todo);
        return new TodoVO(
                updated.getId(),
                updated.getDescription(),
                updated.getCreationTime(),
                updated.getCompleted()
        );
    }

    public void deleteTodo(Integer todoId) {
        todoRepository.deleteById(todoId);
    }

    public TodoVO getTodo(Integer todoId) {
        Todo todo = todoRepository.getReferenceById(todoId);

        return new TodoVO(
                todo.getId(),
                todo.getDescription(),
                todo.getCreationTime(),
                todo.getCompleted()
        );
    }

    public List<TodoVO> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(
                        todo -> new TodoVO(
                                todo.getId(),
                                todo.getDescription(),
                                todo.getCreationTime(),
                                todo.getCompleted()
                        )
                ).collect(Collectors.toList());
    }
}
