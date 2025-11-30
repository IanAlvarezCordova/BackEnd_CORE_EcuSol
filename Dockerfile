# Usar una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Copiar el archivo JAR construido
COPY target/tu-aplicacion.jar /app.jar

# Exponer el puerto 8081 (o el que uses)
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]
