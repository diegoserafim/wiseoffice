// Custom JavaScript for Task Manager

document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert && alert.parentNode) {
                alert.style.transition = 'opacity 0.5s ease';
                alert.style.opacity = '0';
                setTimeout(() => {
                    if (alert.parentNode) {
                        alert.parentNode.removeChild(alert);
                    }
                }, 500);
            }
        }, 5000);
    });

    // Add fade-in animation to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('fade-in-up');
    });

    // Form validation enhancement
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Processando...';
                
                // Re-enable button after 3 seconds in case of error
                setTimeout(() => {
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = submitBtn.getAttribute('data-original-text') || 'Enviar';
                }, 3000);
            }
        });
    });

    // Store original button text
    const submitButtons = document.querySelectorAll('button[type="submit"]');
    submitButtons.forEach(btn => {
        btn.setAttribute('data-original-text', btn.innerHTML);
    });

    // Task status color coding
    const statusBadges = document.querySelectorAll('.badge');
    statusBadges.forEach(badge => {
        const text = badge.textContent.toLowerCase();
        if (text.includes('pendente')) {
            badge.classList.add('badge-status-pending');
        } else if (text.includes('progresso')) {
            badge.classList.add('badge-status-in-progress');
        } else if (text.includes('concluída')) {
            badge.classList.add('badge-status-completed');
        } else if (text.includes('cancelada')) {
            badge.classList.add('badge-status-cancelled');
        }
        
        // Priority colors
        if (text.includes('baixa')) {
            badge.classList.add('badge-priority-low');
        } else if (text.includes('média')) {
            badge.classList.add('badge-priority-medium');
        } else if (text.includes('alta')) {
            badge.classList.add('badge-priority-high');
        } else if (text.includes('urgente')) {
            badge.classList.add('badge-priority-urgent');
        }
    });

    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('button[data-action="delete"], a[data-action="delete"]');
    deleteButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const itemName = this.getAttribute('data-item') || 'este item';
            
            if (confirm(`Tem certeza que deseja excluir ${itemName}? Esta ação não pode ser desfeita.`)) {
                if (this.tagName === 'BUTTON') {
                    this.closest('form').submit();
                } else {
                    window.location.href = this.href;
                }
            }
        });
    });

    // Search functionality
    const searchInput = document.querySelector('input[name="search"]');
    if (searchInput) {
        let searchTimeout;
        searchInput.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                if (this.value.length >= 3 || this.value.length === 0) {
                    this.closest('form').submit();
                }
            }, 500);
        });
    }

    // Task quick actions
    const quickActionButtons = document.querySelectorAll('.quick-action');
    quickActionButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const action = this.getAttribute('data-action');
            const taskId = this.getAttribute('data-task-id');
            
            if (action && taskId) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = `/tasks/${taskId}/${action}`;
                
                // Add CSRF token if available
                const csrfToken = document.querySelector('meta[name="_csrf"]');
                const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
                if (csrfToken && csrfHeader) {
                    const csrfInput = document.createElement('input');
                    csrfInput.type = 'hidden';
                    csrfInput.name = csrfHeader.getAttribute('content');
                    csrfInput.value = csrfToken.getAttribute('content');
                    form.appendChild(csrfInput);
                }
                
                document.body.appendChild(form);
                form.submit();
            }
        });
    });

    // Tooltip initialization
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Progress bar animation
    const progressBars = document.querySelectorAll('.progress-bar');
    progressBars.forEach(bar => {
        const width = bar.style.width || bar.getAttribute('aria-valuenow') + '%';
        bar.style.width = '0%';
        setTimeout(() => {
            bar.style.transition = 'width 1s ease-in-out';
            bar.style.width = width;
        }, 500);
    });

    // Real-time clock
    const clockElement = document.querySelector('.current-time');
    if (clockElement) {
        function updateClock() {
            const now = new Date();
            const timeString = now.toLocaleTimeString('pt-BR');
            clockElement.textContent = timeString;
        }
        
        updateClock();
        setInterval(updateClock, 1000);
    }

    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        // Ctrl/Cmd + N for new task
        if ((e.ctrlKey || e.metaKey) && e.key === 'n') {
            e.preventDefault();
            const newTaskLink = document.querySelector('a[href*="/tasks/new"]');
            if (newTaskLink) {
                window.location.href = newTaskLink.href;
            }
        }
        
        // Escape to close modals
        if (e.key === 'Escape') {
            const modals = document.querySelectorAll('.modal.show');
            modals.forEach(modal => {
                const modalInstance = bootstrap.Modal.getInstance(modal);
                if (modalInstance) {
                    modalInstance.hide();
                }
            });
        }
    });

    console.log('Task Manager JavaScript loaded successfully!');
});

