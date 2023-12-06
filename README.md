# simples-dental-teste
Teste técnico para a empresa Simples Dental

## Técnologias Utilizadas

- [Spring Boot 3.2.0](https://docs.spring.io/spring-boot/docs/3.2.x/api/)
- [Java 17](https://docs.oracle.com/en/java/javase/17/docs/api/index.html)
- [Liquibase](https://docs.liquibase.com/home.html)
- PostgreSQL
- JUnit
- [Swagger 3.0](https://swagger.io/docs/specification/about/)
- Lombok
- Docker
- [Docker Compose](https://docs.docker.com/compose/install/)

## Como executar este projeto

Primeiramente é necessário criar o banco de dados para conseguir executar o projeto. Para isso
execute o comando `docker compose up -d` dentro da pasta `/database` e aguardar o processo finalizar.
Verificar o arquivo `application.properties` para adquirir as credenciais do banco.

Após o banco de dados criado, para subir esse projeto basta executar o método `main()` utilizando sua IDE ou
executar o comando `./mvn clean spring-boot:run`, o banco de dados será criado automaticamente enquanto o contexto do
Spring é criado.