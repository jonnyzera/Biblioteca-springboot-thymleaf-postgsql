# Build (Onde o JAR Executável é criado)
FROM maven:3.9.1-jdk-17 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo de configuração e faz o download das dependências
COPY pom.xml .

# Compila o projeto e empacota o JAR (ignorando os teste para ser mais rápido)
RUN mvn clean package -DskipTests

# Define o nome do JAR que será gerado, baseado no seu pom.xml
#ArtifactId: projeto, Version: 1.0-SNAPSHOT
ARG JAR_FILE=target/projeto-1.0-SNAPSHOT.jar

# Runtime (A Imagem Final de Produção)

# Use apenas o JRE (ambiente de exceução) para reduzir o tamanho da imagem
FROM eclipse-eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR do estágio de build para o estágio de runtime
COPY --from=build /app/${JAR_FILE} app.jar

#Expõe a porta que o Spring Boot usará (8080, conforme seu application.properties)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]