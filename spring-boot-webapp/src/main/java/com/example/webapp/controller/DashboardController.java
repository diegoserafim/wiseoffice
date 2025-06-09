package com.example.webapp.controller;

import com.example.webapp.entity.User;
import com.example.webapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        // Estatísticas das tarefas
        TaskService.TaskStatistics stats = taskService.getTaskStatistics(user);
        model.addAttribute("stats", stats);
        
        // Tarefas recentes
        model.addAttribute("recentTasks", taskService.findAllTasksByUser(user).stream().limit(5).toList());
        
        // Tarefas pendentes
        model.addAttribute("pendingTasks", taskService.findPendingTasks(user).stream().limit(5).toList());
        
        // Tarefas em atraso
        model.addAttribute("overdueTasks", taskService.findOverdueTasks(user));
        
        // Informações do usuário
        model.addAttribute("user", user);
        
        return "dashboard/index";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "profile/index";
    }
}

