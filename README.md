# 📝 Acelera Maker - Blog Pessoal

Este projeto é uma API RestFul para um Blog Pessoal feita como projeto de fixação para Acelera Maker Montreal

---

## 🚀 Funcionalidades

A API oferece endpoints seguros para:

- 🔐 Autenticação de usuários via token JWT.
- 👥 Cadastro e gerenciamento de usuários.
- 📌 Criação, leitura, atualização e exclusão de postagens.
- 🏷️ Criação, leitura, atualização e exclusão de temas.

## Endpoints

### 🧑‍💻 Usuários

- `POST /api/usuarios` – Cadastro de usuário. - Liberado a todos
- `POST /api/usuarios/login` – Login de usuário. - Liberado a todos
- `PUT /api/usuarios/{id}` – Atualização de usuário. - Apenas usuário com token e/ou admin
- `DELETE /api/usuarios/{id}` – Exclusão de usuário. - Apenas usuário com token e/ou admin

### 🏷️ Temas

- `GET /api/temas` – Listar todos os temas. - Liberado a todos
- `POST /api/temas` – Criar um novo tema. - Apenas usuário com token e/ou admin
- `PUT /api/temas/{id}` – Atualizar tema existente. - Apenas usuário que criou usando seu token e/ou admin
- `DELETE /api/temas/{id}` – Deletar tema. - Apenas usuário que criou usando seu token e/ou admin

### 📌 Postagens

- `GET /api/postagens` – Listar todas as postagens. - Liberado a todos
- `GET /api/postagens/filtro?autor={id}&tema={id}` – Filtrar postagens por autor e tema. - Liberado a todos
- `POST /api/postagens` – Criar uma nova postagem. - Apenas usuário com token e/ou admin
- `PUT /api/postagens/{id}` – Atualizar postagem. - Apenas usuário que criou usando seu token e/ou admin
- `DELETE /api/postagens/{id}` – Deletar postagem. - Apenas usuário que criou usando seu token e/ou admin

---

## 🛠️ Tecnologias Utilizadas

O projeto foi desenvolvido com as seguintes tecnologias:

### 🔧 Backend
- **Java 17**
- **Spring Boot 3.4.2**

### 🗃️ Banco de Dados
- **PostgreSQL** – Banco de dados principal (produção).
- **H2 Database** – Banco de dados em memória (para testes locais).

### 🔐 Autenticação
- **JJWT 0.11.5** – Para criação e validação de tokens JWT.

### 📖 Documentação
- **SpringDoc OpenAPI (2.8.0)** – Geração automática da documentação da API com Swagger UI.

### 🧪 Testes
- `spring-boot-starter-test` – Framework de testes do Spring.
- `spring-security-test` – Testes com segurança Spring.

### 📊 Qualidade de Código
- **SonarQube** – Análise de qualidade de código e cobertura de testes com o plugin Maven.

---

## ▶️ Como executar o projeto localmente

### ✅ Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/get-started)
- [GitHub](https://docs.github.com/en/desktop/installing-and-authenticating-to-github-desktop/installing-github-desktop)

---

### ⬇️ 1. Clone o repositório

```bash
git clone (https://github.com/MarcoShvy/Blog-Pessoal.git)
cd Blog-Pessoal
```
### 🐘 2. Suba o banco de dados PostgreSQL
### ⚙️ Configure o banco de dados da aplicação
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sonarqube
spring.datasource.username=postgres
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```

### 🔍 3. Suba o SonarQube com Docker
```bash
docker run -d --name sonarqube \
  -p 9000:9000 \
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true \
  sonarqube:latest
```
### Acesse o SonarQube em http://localhost:9000

### Executar mvn clean install sonar:sonar para verificar projeto no Sonarqube

### 🚀 5. Execute o projeto
```bash
./mvnw spring-boot:run
```
### Documentação do Swagger disponível em: http://localhost:8080/swagger-ui/index.html 🚀
