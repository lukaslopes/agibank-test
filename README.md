# Sistema de Gerenciamento de Clientes e Seguros

Lukas Antunes Lopes

## Visão Geral

Este projeto é composto por dois microsserviços que trabalham em conjunto para fornecer um sistema de gerenciamento de clientes e seguros:

1. **Serviço de Clientes (porta 8080)**: Gerencia o cadastro e consulta de clientes.
2. **Serviço de Seguros (porta 8081)**: Gerencia a simulação e contratação de seguros, integrando-se ao serviço de clientes.

## Tecnologias

### Comuns a ambos os serviços

- Java 21
- Spring Boot 3.5.3
- Spring Data JPA
- PostgreSQL
- Lombok
- OpenAPI (Swagger)
- JUnit 5
- Docker e Docker Compose

## Pré-requisitos

- Java 21 ou superior
- Maven 3.6.3 ou superior
- Docker e Docker Compose

## Configuração do Ambiente

### 1. Bancos de Dados

O projeto utiliza dois bancos de dados PostgreSQL em contêineres Docker:

```bash
# Iniciar os contêineres do PostgreSQL
# clientes-db na porta 5432
# seguros-db na porta 5433
docker-compose up -d
```

#### Configuração dos Bancos:

**Serviço de Clientes**

- Host: localhost
- Porta: 5432
- Banco: clientes_db
- Usuário: postgres
- Senha: postgres

**Serviço de Seguros**

- Host: localhost
- Porta: 5433
- Banco: seguros_db
- Usuário: postgres
- Senha: postgres

## Executando os Serviços

### 1. Serviço de Clientes

```bash
cd clientes
mvn spring-boot:run
```

**Documentação da API**:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

### 2. Serviço de Seguros

```bash
cd ../seguros
mvn spring-boot:run
```

**Documentação da API**:

- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/api-docs

## Endpoints da API

### Serviço de Clientes (8080)

- `GET /clientes` - Lista todos os clientes
- `GET /clientes/{id}` - Busca um cliente por ID
- `POST /clientes` - Cria um novo cliente
- `PUT /clientes/{id}` - Atualiza um cliente existente
- `DELETE /clientes/{id}` - Remove um cliente

### Serviço de Seguros (8081)

- `POST /seguros/simular` - Simula todos os tipos de seguros para um cliente
- `POST /seguros/contratar` - Contrata um seguro para um cliente

## Executando os Testes

### Para cada serviço (dentro do diretório do serviço):

```bash
# Executar todos os testes
mvn test

# Executar testes de integração
mvn test -Dgroups=integration

# Executar testes unitários
mvn test -Dgroups=unit
```

## Estrutura dos Projetos

### Serviço de Clientes

```
clientes/
├── src/
│   ├── main/
│   │   ├── java/br/com/agibank/clientes/
│   │   │   ├── api/                    # Controladores e DTOs
│   │   │   ├── config/                 # Configurações
│   │   │   ├── domain/                 # Lógica de negócios
│   │   │   │   ├── model/              # Entidades
│   │   │   │   ├── repository/         # Repositórios
│   │   │   │   └── usecase/            # Casos de uso
│   │   │   └── infrastructure/         # Implementações de infraestrutura
│   │   └── resources/                  # Arquivos de configuração
│   └── test/                           # Testes
└── pom.xml
```

### Serviço de Seguros

```
seguros/
├── src/
│   ├── main/
│   │   ├── java/br/com/agibank/seguros/
│   │   │   ├── api/                    # Controladores e DTOs
│   │   │   ├── config/                 # Configurações
│   │   │   ├── domain/                 # Lógica de negócios
│   │   │   │   ├── model/              # Entidades
│   │   │   │   ├── repository/         # Repositórios
│   │   │   │   └── usecase/            # Casos de uso
│   │   │   └── infrastructure/         # Clientes API Client e outras implementações
│   │   └── resources/                  # Arquivos de configuração
│   └── test/                           # Testes
└── pom.xml
```

## Comunicação entre Serviços

O Serviço de Seguros se comunica com o Serviço de Clientes através de uma API REST. A comunicação é feita usando RestTemplate

## Licença

Este projeto é um teste técnico para o processo seletivo do Agibank.
