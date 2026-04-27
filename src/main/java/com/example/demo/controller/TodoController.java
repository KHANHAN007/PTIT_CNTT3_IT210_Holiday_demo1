package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.model.TodoPriority;
import com.example.demo.model.TodoStatus;
import com.example.demo.service.TodoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
public class TodoController {
    private final TodoService todoService;
    private final MessageSource messageSource;

    public TodoController(TodoService todoService, MessageSource messageSource) {
        this.todoService = todoService;
        this.messageSource = messageSource;
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
    public String listTodos(Model model, HttpSession session) {
        String ownerName = (String) session.getAttribute("ownerName");
        if (!StringUtils.hasText(ownerName)) {
            return "redirect:/owner";
        }
        model.addAttribute("ownerName", ownerName);
        model.addAttribute("todos", todoService.findAll());
        return "todo/list";
    }

    @GetMapping("/owner")
    public String showOwnerForm(HttpSession session, Model model) {
        String ownerName = (String) session.getAttribute("ownerName");
        model.addAttribute("ownerName", ownerName);
        return "todo/owner";
    }

    @PostMapping("/owner")
    public String saveOwner(@RequestParam("ownerName") String ownerName, HttpSession session, RedirectAttributes redirectAttributes, Locale locale) {
        if (!StringUtils.hasText(ownerName)) {
            redirectAttributes.addFlashAttribute("ownerError", messageSource.getMessage("owner.required", null, locale));
            return "redirect:/owner";
        }
        session.setAttribute("ownerName", ownerName.trim());
        return "redirect:/";
    }

    @GetMapping("/new")
    public String showCreateForm(HttpSession session) {
        if (!StringUtils.hasText((String) session.getAttribute("ownerName"))) {
            return "redirect:/owner";
        }
        return "todo/form";
    }

    @PostMapping("/save")
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo, BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, HttpSession session) {
        if (!StringUtils.hasText((String) session.getAttribute("ownerName"))) {
            return "redirect:/owner";
        }
        if (bindingResult.hasErrors()) {
            return "todo/form";
        }
        todoService.save(todo);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                messageSource.getMessage("todo.save.success", null, locale)
        );
        return "redirect:/";
    }
}

