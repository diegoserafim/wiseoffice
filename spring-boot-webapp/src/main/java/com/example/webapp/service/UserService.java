package com.example.webapp.service;

import com.example.webapp.dto.UserRegistrationDto;
import com.example.webapp.entity.User;
import com.example.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

 // Método para debug - adicionar temporariamente
    public void debugUser(String username) {
        Optional<User> user = userRepository.findByUsernameOrEmail(username);
        System.out.println("=== DEBUG USER ===");
        System.out.println("Username buscado: " + username);
        System.out.println("Usuário encontrado: " + user.isPresent());
        if (user.isPresent()) {
            User u = user.get();
            System.out.println("Username DB: '" + u.getUsername() + "'");
            System.out.println("Email DB: '" + u.getEmail() + "'");
            System.out.println("Enabled: " + u.isEnabled());
            System.out.println("Account Non Locked: " + u.isAccountNonLocked());
        }
        System.out.println("==================");
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        // Verifica se username já existe
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Nome de usuário já está em uso!");
        }

        // Verifica se email já existe
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email já está em uso!");
        }

        // Verifica se as senhas coincidem
        if (!registrationDto.isPasswordMatching()) {
            throw new RuntimeException("As senhas não coincidem!");
        }

        // Cria novo usuário
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setFullName(registrationDto.getFullName());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(User.Role.USER);

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllActiveUsers() {
        return userRepository.findActiveUsers();
    }

    public List<User> findUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }

    public User promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        user.setRole(User.Role.ADMIN);
        return userRepository.save(user);
    }
}

