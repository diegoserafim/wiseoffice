package com.example.webapp.service;

import com.example.webapp.dto.TaskDto;
import com.example.webapp.entity.Task;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(TaskDto taskDto, User user) {
        Task task = taskDto.toEntity();
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, TaskDto taskDto, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Verifica se a tarefa pertence ao usuário
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado à tarefa");
        }

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setDueDate(taskDto.getDueDate());

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Verifica se a tarefa pertence ao usuário
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado à tarefa");
        }

        taskRepository.delete(task);
    }

    public Optional<Task> findTaskById(Long taskId, User user) {
        Optional<Task> task = taskRepository.findById(taskId);
        
        if (task.isPresent() && !task.get().getUser().getId().equals(user.getId())) {
            return Optional.empty(); // Não retorna tarefa de outro usuário
        }
        
        return task;
    }

    public List<Task> findAllTasksByUser(User user) {
        return taskRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Task> findTasksByStatus(User user, Task.Status status) {
        return taskRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status);
    }

    public List<Task> findTasksByPriority(User user, Task.Priority priority) {
        return taskRepository.findByUserAndPriorityOrderByDueDateAsc(user, priority);
    }

    public List<Task> findPendingTasks(User user) {
        return taskRepository.findPendingTasksByUser(user);
    }

    public List<Task> findOverdueTasks(User user) {
        return taskRepository.findOverdueTasksByUser(user, LocalDateTime.now());
    }

    public List<Task> searchTasksByTitle(User user, String title) {
        return taskRepository.findByUserAndTitleContainingIgnoreCase(user, title);
    }

    public Task markAsCompleted(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado à tarefa");
        }

        task.setStatus(Task.Status.COMPLETED);
        return taskRepository.save(task);
    }

    public Task markAsInProgress(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado à tarefa");
        }

        task.setStatus(Task.Status.IN_PROGRESS);
        return taskRepository.save(task);
    }

    public Long countTasksByStatus(User user, Task.Status status) {
        return taskRepository.countByUserAndStatus(user, status);
    }

    public List<Task> findTasksInPeriod(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return taskRepository.findByUserAndCreatedAtBetween(user, startDate, endDate);
    }

    // Métodos para estatísticas
    public TaskStatistics getTaskStatistics(User user) {
        Long pending = countTasksByStatus(user, Task.Status.PENDING);
        Long inProgress = countTasksByStatus(user, Task.Status.IN_PROGRESS);
        Long completed = countTasksByStatus(user, Task.Status.COMPLETED);
        Long cancelled = countTasksByStatus(user, Task.Status.CANCELLED);
        Long overdue = (long) findOverdueTasks(user).size();

        return new TaskStatistics(pending, inProgress, completed, cancelled, overdue);
    }

    // Classe interna para estatísticas
    public static class TaskStatistics {
        private final Long pending;
        private final Long inProgress;
        private final Long completed;
        private final Long cancelled;
        private final Long overdue;

        public TaskStatistics(Long pending, Long inProgress, Long completed, Long cancelled, Long overdue) {
            this.pending = pending;
            this.inProgress = inProgress;
            this.completed = completed;
            this.cancelled = cancelled;
            this.overdue = overdue;
        }

        // Getters
        public Long getPending() { return pending; }
        public Long getInProgress() { return inProgress; }
        public Long getCompleted() { return completed; }
        public Long getCancelled() { return cancelled; }
        public Long getOverdue() { return overdue; }
        public Long getTotal() { return pending + inProgress + completed + cancelled; }
    }
}

