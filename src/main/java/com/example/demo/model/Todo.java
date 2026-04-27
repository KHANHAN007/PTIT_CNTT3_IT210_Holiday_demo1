package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "todos")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{todo.content.notBlank}")
    @Column(nullable = false)
    private String content;

    @NotNull(message = "{todo.dueDate.notNull}")
    @FutureOrPresent(message = "{todo.dueDate.futureOrPresent}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull(message = "{todo.status.notNull}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    @NotNull(message = "{todo.priority.notNull}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoPriority priority;
}
