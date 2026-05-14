# Etapa de construcción
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY . .
# Asegurar permisos de ejecución para mvnw
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copiamos el jar desde la etapa de construcción
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Render asigna el puerto dinámico en la variable PORT
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

