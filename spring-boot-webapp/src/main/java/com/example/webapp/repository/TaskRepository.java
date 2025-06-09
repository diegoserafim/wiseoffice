package com.example.webapp.repository;

import com.example.webapp.entity.Task;
import com.example.webapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Busca todas as tarefas de um usuário
     */
    List<Task> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Busca tarefas por usuário e status
     */
    List<Task> findByUserAndStatusOrderByCreatedAtDesc(User user, Task.Status status);

    /**
     * Busca tarefas por usuário e prioridade
     */
    List<Task> findByUserAndPriorityOrderByDueDateAsc(User user, Task.Priority priority);

    /**
     * Busca tarefas pendentes de um usuário
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.status IN ('PENDING', 'IN_PROGRESS') ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findPendingTasksByUser(@Param("user") User user);

    /**
     * Busca tarefas com prazo vencido
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.dueDate < :now AND t.status IN ('PENDING', 'IN_PROGRESS')")
    List<Task> findOverdueTasksByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    /**
     * Busca tarefas por título (busca parcial)
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY t.createdAt DESC")
    List<Task> findByUserAndTitleContainingIgnoreCase(@Param("user") User user, @Param("title") String title);

    /**
     * Conta tarefas por status para um usuário
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.status = :status")
    Long countByUserAndStatus(@Param("user") User user, @Param("status") Task.Status status);

    /**
     * Busca tarefas criadas em um período
     */
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Task> findByUserAndCreatedAtBetween(@Param("user") User user, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
}

