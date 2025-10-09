# ----------------------------------------------------------------------------
# ESTÁGIO 1: BUILD (Compilação)
# ----------------------------------------------------------------------------
FROM maven:3-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src /app/src
RUN mvn clean package -DskipTests

# Define a variável ARG (para uso posterior, se necessário, e para clareza)
ARG JAR_FILE=target/projeto-0.0.1-SNAPSHOT.jar 

# ----------------------------------------------------------------------------
# ESTÁGIO 2: RUNTIME (Execução)
# ----------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# CORREÇÃO: Redefinir a variável ARG. 
# Isso permite que a próxima linha de COPY a utilize sem gerar o UndefinedVar warning.
ARG JAR_FILE=target/projeto-0.0.1-SNAPSHOT.jar 

# Copia o JAR compilado, agora a variável é reconhecida no contexto deste estágio
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]