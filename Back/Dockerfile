# Utiliza una imagen base de Maven para compilar la aplicación
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copia los archivos de configuración y dependencias
COPY pom.xml .
COPY src ./src

# Compila la aplicación
RUN mvn clean package -DskipTests

# Utiliza una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia el jar generado desde la fase de compilación
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto 8080 (o el puerto que use tu aplicación)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
