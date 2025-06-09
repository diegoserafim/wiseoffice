package com.example.webapp.controller;

import com.example.webapp.dto.UserRegistrationDto;
import com.example.webapp.entity.User;
import com.example.webapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "unauthorized", required = false) String unauthorized,
                           Model model) {
        
        if (error != null) {
            model.addAttribute("errorMessage", "Usuário ou senha inválidos!");
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Logout realizado com sucesso!");
        }
        
        if (unauthorized != null) {
            model.addAttribute("errorMessage", "Acesso negado. Faça login para continuar.");
        }
        
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRegistration", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userRegistration") UserRegistrationDto registrationDto,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            User user = userService.registerUser(registrationDto);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}

