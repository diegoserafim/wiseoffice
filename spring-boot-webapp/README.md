# Spring Boot Web Application - Task Manager

## Visão Geral

Este é um projeto completo de aplicação web desenvolvido com Spring Boot, demonstrando as melhores práticas de desenvolvimento Java moderno. A aplicação implementa um sistema de gerenciamento de tarefas com autenticação segura, interface web responsiva e arquitetura robusta.

## Tecnologias Utilizadas

### Backend
- **Java 17** - Versão LTS com compatibilidade Java 8
- **Spring Boot 3.2.0** - Framework principal
- **Spring Security 6** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **Maven** - Gerenciamento de dependências

### Frontend
- **Thymeleaf** - Template engine
- **Bootstrap 5** - Framework CSS
- **Bootstrap Icons** - Ícones
- **JavaScript ES6+** - Interatividade

### Banco de Dados
- **PostgreSQL** - Banco principal (produção)
- **H2 Database** - Banco em memória (desenvolvimento)

## Funcionalidades Principais

### Sistema de Autenticação
- Registro de usuários com validação
- Login seguro com Spring Security
- Autenticação JWT para APIs
- Controle de acesso baseado em roles (USER, ADMIN, MODERATOR)
- Logout seguro com invalidação de sessão

### Gerenciamento de Tarefas
- Criação, edição e exclusão de tarefas
- Status de tarefas (Pendente, Em Progresso, Concluída, Cancelada)
- Prioridades (Baixa, Média, Alta, Urgente)
- Definição de prazos
- Busca e filtros avançados
- Estatísticas e relatórios

### Interface Web
- Design responsivo e moderno
- Dashboard com estatísticas
- Navegação intuitiva
- Mensagens de feedback
- Animações e transições suaves
- Suporte a dispositivos móveis

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/example/webapp/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # Controllers web e REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── repository/     # Repositórios
│   │   ├── security/       # Configurações de segurança
│   │   ├── service/        # Serviços de negócio
│   │   └── WebAppApplication.java
│   └── resources/
│       ├── static/         # CSS, JS, imagens
│       ├── templates/      # Templates Thymeleaf
│       ├── application.properties
│       └── application-dev.properties
└── test/                   # Testes unitários
```

## Configuração e Execução

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 16 (para produção)

### Configuração do Banco de Dados

#### Desenvolvimento (H2)
```properties
spring.profiles.active=dev
```

#### Produção (PostgreSQL)
```sql
CREATE DATABASE webapp_db;
CREATE USER webapp_user WITH PASSWORD 'webapp_password';
GRANT ALL PRIVILEGES ON DATABASE webapp_db TO webapp_user;
```

### Executando a Aplicação

#### Modo Desenvolvimento
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Modo Produção
```bash
mvn clean package
java -jar target/spring-boot-webapp-1.0.0.jar
```

### Acessando a Aplicação
- URL: http://localhost:8080
- Console H2 (dev): http://localhost:8080/h2-console

## Endpoints da API

### Autenticação
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Registro

### Tarefas
- `GET /api/tasks` - Listar tarefas
- `POST /api/tasks` - Criar tarefa
- `GET /api/tasks/{id}` - Obter tarefa
- `PUT /api/tasks/{id}` - Atualizar tarefa
- `DELETE /api/tasks/{id}` - Excluir tarefa

### Páginas Web
- `/` - Página inicial
- `/login` - Login
- `/register` - Cadastro
- `/dashboard` - Dashboard principal
- `/tasks` - Gerenciamento de tarefas

## Recursos Java 17 Utilizados

### Compatibilidade com Java 8
O projeto mantém 100% de compatibilidade com Java 8, utilizando apenas recursos que funcionam em ambas as versões:

- Streams API
- Optional
- Lambda expressions
- Method references
- Interface default methods

### Recursos Java 17 Disponíveis
Para desenvolvedores que desejam aproveitar recursos mais modernos:

```java
// Text Blocks (Java 15+)
String sql = """
    SELECT t.* FROM tasks t 
    WHERE t.user_id = ? 
    AND t.status = 'PENDING'
    ORDER BY t.priority DESC
    """;

// Records (Java 14+)
public record TaskSummary(String title, String status, LocalDateTime dueDate) {}

// Pattern Matching for instanceof (Java 16+)
if (obj instanceof Task task) {
    return task.getTitle();
}

// Switch Expressions (Java 14+)
String statusMessage = switch (task.getStatus()) {
    case PENDING -> "Aguardando início";
    case IN_PROGRESS -> "Em andamento";
    case COMPLETED -> "Finalizada";
    case CANCELLED -> "Cancelada";
};
```

## Segurança

### Autenticação
- Senhas criptografadas com BCrypt
- Tokens JWT com expiração configurável
- Proteção contra ataques de força bruta

### Autorização
- Controle de acesso baseado em roles
- Proteção de endpoints sensíveis
- Validação de propriedade de recursos

### Proteções Implementadas
- CSRF protection
- XSS prevention
- SQL injection prevention (JPA/Hibernate)
- Session fixation protection

## Testes

### Estrutura de Testes
```bash
mvn test                    # Executar todos os testes
mvn test -Dtest=UserServiceTest  # Teste específico
```

### Cobertura
- Testes unitários para serviços
- Testes de integração para controllers
- Testes de segurança

## Deploy

### Perfis de Ambiente
- `dev` - Desenvolvimento local
- `prod` - Produção

### Variáveis de Ambiente
```bash
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://localhost:5432/webapp_db
export DATABASE_USERNAME=webapp_user
export DATABASE_PASSWORD=webapp_password
export JWT_SECRET=your-secret-key
```

## Monitoramento

### Actuator Endpoints
- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Métricas
- `/actuator/info` - Informações da aplicação

### Logs
- Configuração de logging por ambiente
- Logs estruturados para produção
- Monitoramento de performance

## Contribuição

### Padrões de Código
- Seguir convenções Java
- Documentação JavaDoc
- Testes para novas funcionalidades
- Code review obrigatório

### Git Workflow
```bash
git checkout -b feature/nova-funcionalidade
git commit -m "feat: adicionar nova funcionalidade"
git push origin feature/nova-funcionalidade
```

## Licença

Este projeto é licenciado sob a MIT License - veja o arquivo LICENSE para detalhes.

## Suporte

Para dúvidas e suporte:
- Documentação: README.md
- Issues: GitHub Issues
- Email: suporte@taskmanager.com

---

**Desenvolvido com ❤️ usando Spring Boot e Java 17**

