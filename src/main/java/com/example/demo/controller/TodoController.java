package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.model.TodoPriority;
import com.example.demo.model.TodoStatus;
import com.example.demo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @ModelAttribute("todo")
    public Todo todo() {
        Todo todo = new Todo();
        todo.setStatus(TodoStatus.TODO);
        todo.setPriority(TodoPriority.MEDIUM);
        return todo;
    }

    @ModelAttribute("statuses")
    public TodoStatus[] statuses() {
        return TodoStatus.values();
    }

    @ModelAttribute("priorities")
    public TodoPriority[] priorities() {
        return TodoPriority.values();
    }

    @GetMapping({"/", ""})
    public String listTodos(Model model) {
        model.addAttribute("todos", todoService.findAll());
        return "todo/list";
    }

    @GetMapping("/new")
    public String showCreateForm() {
        return "todo/form";
    }

    @PostMapping("/save")
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "todo/form";
        }
        todoService.save(todo);
        redirectAttributes.addFlashAttribute("successMessage", "Todo saved successfully.");
        return "redirect:/";
    }
}

