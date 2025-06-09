# Instalação e Configuração - Task Manager

## Guia de Instalação Completo

### 1. Pré-requisitos do Sistema

#### Java Development Kit (JDK)
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel

# macOS (usando Homebrew)
brew install openjdk@17

# Windows
# Baixar do site oficial da Oracle ou usar OpenJDK
```

#### Apache Maven
```bash
# Ubuntu/Debian
sudo apt install maven

# CentOS/RHEL
sudo yum install maven

# macOS
brew install maven

# Windows
# Baixar do site oficial do Maven
```

#### PostgreSQL (Produção)
```bash
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib

# CentOS/RHEL
sudo yum install postgresql-server postgresql-contrib

# macOS
brew install postgresql

# Windows
# Baixar do site oficial do PostgreSQL
```

### 2. Configuração do Banco de Dados

#### PostgreSQL Setup
```sql
-- Conectar como usuário postgres
sudo -u postgres psql

-- Criar banco de dados
CREATE DATABASE webapp_db;

-- Criar usuário
CREATE USER webapp_user WITH PASSWORD 'webapp_password';

-- Conceder privilégios
GRANT ALL PRIVILEGES ON DATABASE webapp_db TO webapp_user;

-- Sair do psql
\q
```

#### Configuração de Conexão
Edite o arquivo `src/main/resources/application.properties`:

```properties
# Configurações do banco PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/webapp_db
spring.datasource.username=webapp_user
spring.datasource.password=webapp_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Configurações JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Clonando e Configurando o Projeto

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/spring-boot-webapp.git
cd spring-boot-webapp

# Verificar versões
java -version
mvn -version

# Compilar o projeto
mvn clean compile

# Executar testes
mvn test

# Executar em modo desenvolvimento
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Configurações de Ambiente

#### Desenvolvimento (application-dev.properties)
```properties
# Banco H2 em memória
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Console H2 habilitado
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logs detalhados
logging.level.com.example.webapp=DEBUG
logging.level.org.springframework.security=DEBUG

# Hot reload
spring.thymeleaf.cache=false
spring.devtools.restart.enabled=true
```

#### Produção (application-prod.properties)
```properties
# PostgreSQL
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/webapp_db}
spring.datasource.username=${DATABASE_USERNAME:webapp_user}
spring.datasource.password=${DATABASE_PASSWORD:webapp_password}

# JPA otimizado para produção
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Logs de produção
logging.level.com.example.webapp=INFO
logging.level.org.springframework.security=WARN

# Cache habilitado
spring.thymeleaf.cache=true

# Configurações de segurança
jwt.secret=${JWT_SECRET:mySecretKey123456789012345678901234567890}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

### 5. Variáveis de Ambiente

#### Linux/macOS
```bash
# Adicionar ao ~/.bashrc ou ~/.zshrc
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://localhost:5432/webapp_db
export DATABASE_USERNAME=webapp_user
export DATABASE_PASSWORD=webapp_password
export JWT_SECRET=your-super-secret-jwt-key-here
export JWT_EXPIRATION=86400000

# Recarregar configurações
source ~/.bashrc
```

#### Windows
```cmd
# Configurar variáveis de ambiente
set SPRING_PROFILES_ACTIVE=prod
set DATABASE_URL=jdbc:postgresql://localhost:5432/webapp_db
set DATABASE_USERNAME=webapp_user
set DATABASE_PASSWORD=webapp_password
set JWT_SECRET=your-super-secret-jwt-key-here
set JWT_EXPIRATION=86400000
```

#### Docker Compose
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DATABASE_URL=jdbc:postgresql://db:5432/webapp_db
      - DATABASE_USERNAME=webapp_user
      - DATABASE_PASSWORD=webapp_password
      - JWT_SECRET=your-super-secret-jwt-key-here
    depends_on:
      - db

  db:
    image: postgres:16
    environment:
      - POSTGRES_DB=webapp_db
      - POSTGRES_USER=webapp_user
      - POSTGRES_PASSWORD=webapp_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgres_data:
```

### 6. Executando a Aplicação

#### Modo Desenvolvimento
```bash
# Com H2 em memória
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Acessar aplicação: http://localhost:8080
# Console H2: http://localhost:8080/h2-console
```

#### Modo Produção
```bash
# Compilar para produção
mvn clean package -DskipTests

# Executar JAR
java -jar target/spring-boot-webapp-1.0.0.jar

# Ou com perfil específico
java -jar target/spring-boot-webapp-1.0.0.jar --spring.profiles.active=prod
```

#### Com Docker
```bash
# Construir imagem
docker build -t task-manager .

# Executar com Docker Compose
docker-compose up -d

# Verificar logs
docker-compose logs -f app
```

### 7. Verificação da Instalação

#### Testes de Conectividade
```bash
# Verificar se a aplicação está rodando
curl http://localhost:8080

# Verificar health check
curl http://localhost:8080/actuator/health

# Verificar banco de dados
curl http://localhost:8080/h2-console  # Desenvolvimento
```

#### Testes Funcionais
```bash
# Executar todos os testes
mvn test

# Testes específicos
mvn test -Dtest=UserServiceTest
mvn test -Dtest=TaskControllerTest
mvn test -Dtest=SecurityConfigTest
```

### 8. Configurações Avançadas

#### SSL/HTTPS
```properties
# application-prod.properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
server.port=8443
```

#### Configurações de Performance
```properties
# Configurações do servidor
server.tomcat.max-threads=200
server.tomcat.min-spare-threads=10
server.tomcat.max-connections=8192

# Pool de conexões
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

#### Monitoramento
```properties
# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=when-authorized
management.metrics.export.prometheus.enabled=true
```

### 9. Troubleshooting

#### Problemas Comuns

**Erro de Conexão com Banco**
```bash
# Verificar se PostgreSQL está rodando
sudo systemctl status postgresql

# Verificar conectividade
psql -h localhost -U webapp_user -d webapp_db
```

**Porta 8080 em Uso**
```bash
# Verificar processo usando a porta
lsof -i :8080

# Matar processo
kill -9 <PID>

# Ou usar porta diferente
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

**Problemas de Memória**
```bash
# Aumentar heap size
export MAVEN_OPTS="-Xmx1024m -Xms512m"

# Ou para execução direta
java -Xmx1024m -Xms512m -jar target/spring-boot-webapp-1.0.0.jar
```

#### Logs de Debug
```properties
# Habilitar logs detalhados
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### 10. Próximos Passos

Após a instalação bem-sucedida:

1. **Criar primeiro usuário admin**
2. **Configurar backup do banco de dados**
3. **Configurar monitoramento**
4. **Implementar CI/CD**
5. **Configurar load balancer (se necessário)**

Para mais informações, consulte a documentação completa no README.md.

