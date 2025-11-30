# ETAPA 1: Construcción (Usamos la imagen oficial de Maven con JDK 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos archivos de dependencias primero (Optimización de caché)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Corremos Maven para construir el archivo JAR
RUN mvn clean package -Dmaven.test.skip=true -Dfile.encoding=UTF-8

# ETAPA 2: Ejecución (Imagen ligera de Java)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el .jar
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8081

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
