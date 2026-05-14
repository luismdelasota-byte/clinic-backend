FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Render asigna el puerto dinámico en la variable PORT
EXPOSE 8080

# Usamos la variable PORT para que Spring Boot escuche correctamente
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
