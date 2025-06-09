package com.example.webapp.controller;

import com.example.webapp.dto.TaskDto;
import com.example.webapp.entity.Task;
import com.example.webapp.entity.User;
import com.example.webapp.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public String listTasks(@AuthenticationPrincipal User user,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "search", required = false) String search,
                           Model model) {
        
        List<Task> tasks;
        
        if (search != null && !search.trim().isEmpty()) {
            tasks = taskService.searchTasksByTitle(user, search.trim());
            model.addAttribute("searchTerm", search);
        } else if (status != null && !status.isEmpty()) {
            try {
                Task.Status taskStatus = Task.Status.valueOf(status.toUpperCase());
                tasks = taskService.findTasksByStatus(user, taskStatus);
                model.addAttribute("selectedStatus", status);
            } catch (IllegalArgumentException e) {
                tasks = taskService.findAllTasksByUser(user);
            }
        } else {
            tasks = taskService.findAllTasksByUser(user);
        }
        
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskStatuses", Task.Status.values());
        model.addAttribute("taskPriorities", Task.Priority.values());
        
        // Estatísticas
        TaskService.TaskStatistics stats = taskService.getTaskStatistics(user);
        model.addAttribute("stats", stats);
        
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newTaskForm(Model model) {
        model.addAttribute("taskDto", new TaskDto());
        model.addAttribute("taskStatuses", Task.Status.values());
        model.addAttribute("taskPriorities", Task.Priority.values());
        return "tasks/form";
    }

    @PostMapping("/new")
    public String createTask(@Valid @ModelAttribute("taskDto") TaskDto taskDto,
                            BindingResult result,
                            @AuthenticationPrincipal User user,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("taskStatuses", Task.Status.values());
            model.addAttribute("taskPriorities", Task.Priority.values());
            return "tasks/form";
        }

        try {
            taskService.createTask(taskDto, user);
            redirectAttributes.addFlashAttribute("successMessage", "Tarefa criada com sucesso!");
            return "redirect:/tasks";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("taskStatuses", Task.Status.values());
            model.addAttribute("taskPriorities", Task.Priority.values());
            return "tasks/form";
        }
    }

    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id,
                          @AuthenticationPrincipal User user,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        
        Optional<Task> taskOpt = taskService.findTaskById(id, user);
        
        if (taskOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tarefa não encontrada!");
            return "redirect:/tasks";
        }
        
        model.addAttribute("task", taskOpt.get());
        return "tasks/view";
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id,
                              @AuthenticationPrincipal User user,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        Optional<Task> taskOpt = taskService.findTaskById(id, user);
        
        if (taskOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tarefa não encontrada!");
            return "redirect:/tasks";
        }
        
        Task task = taskOpt.get();
        TaskDto taskDto = new TaskDto(task);
        
        model.addAttribute("taskDto", taskDto);
        model.addAttribute("taskId", id);
        model.addAttribute("taskStatuses", Task.Status.values());
        model.addAttribute("taskPriorities", Task.Priority.values());
        
        return "tasks/form";
    }

    @PostMapping("/{id}/edit")
    public String updateTask(@PathVariable Long id,
                            @Valid @ModelAttribute("taskDto") TaskDto taskDto,
                            BindingResult result,
                            @AuthenticationPrincipal User user,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("taskId", id);
            model.addAttribute("taskStatuses", Task.Status.values());
            model.addAttribute("taskPriorities", Task.Priority.values());
            return "tasks/form";
        }

        try {
            taskService.updateTask(id, taskDto, user);
            redirectAttributes.addFlashAttribute("successMessage", "Tarefa atualizada com sucesso!");
            return "redirect:/tasks/" + id;
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("taskId", id);
            model.addAttribute("taskStatuses", Task.Status.values());
            model.addAttribute("taskPriorities", Task.Priority.values());
            return "tasks/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id,
                            @AuthenticationPrincipal User user,
                            RedirectAttributes redirectAttributes) {
        
        try {
            taskService.deleteTask(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Tarefa excluída com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/complete")
    public String completeTask(@PathVariable Long id,
                              @AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes) {
        
        try {
            taskService.markAsCompleted(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Tarefa marcada como concluída!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/progress")
    public String markInProgress(@PathVariable Long id,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {
        
        try {
            taskService.markAsInProgress(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "Tarefa marcada como em progresso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/tasks";
    }
}

