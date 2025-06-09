# Dockerfile para Spring Boot Task Manager
FROM openjdk:17-jdk-slim

# Metadados
LABEL maintainer="Task Manager Team"
LABEL description="Spring Boot Task Manager Application"
LABEL version="1.0.0"

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Criar usuário não-root
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Diretório de trabalho
WORKDIR /app

# Copiar arquivo JAR
COPY target/spring-boot-webapp-1.0.0.jar app.jar

# Alterar proprietário
RUN chown appuser:appuser app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de execução
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

