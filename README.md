# API de Gerenciamento de Clientes

API RESTful para gerenciamento de clientes desenvolvida com Spring Boot 3.5.3, seguindo os princípios da Clean Architecture e SOLID.

## Tecnologias

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA
- PostgreSQL
- MapStruct
- Lombok
- OpenAPI (Swagger)
- JUnit 5
- Docker

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6.3 ou superior
- Docker e Docker Compose
- PostgreSQL (pode ser executado via Docker)

## Configuração do Ambiente

1. **Banco de Dados**
   ```bash
   # Iniciar o PostgreSQL com Docker
   docker-compose up -d
   ```

2. **Configuração da Aplicação**
   - O arquivo `application.properties` já está configurado para se conectar ao banco de dados PostgreSQL.
   - As credenciais padrão são:
     - Usuário: postgres
     - Senha: postgres
     - Banco de dados: clientes_db
     - Porta: 5432

## Executando a Aplicação

1. **Compilar o Projeto**
   ```bash
   mvn clean install
   ```

2. **Executar a Aplicação**
   ```bash
   mvn spring-boot:run
   ```

3. **Acessar a Documentação da API**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - OpenAPI JSON: http://localhost:8080/api-docs

## Endpoints da API

A API possui os seguintes endpoints:

- `GET /clientes` - Lista todos os clientes
- `GET /clientes/{id}` - Busca um cliente por ID
- `POST /clientes` - Cria um novo cliente
- `PUT /clientes/{id}` - Atualiza um cliente existente
- `DELETE /clientes/{id}` - Remove um cliente

## Executando os Testes

```bash
# Executar todos os testes
mvn test

# Executar testes de integração
mvn test -Dgroups=integration

# Executar testes unitários
mvn test -Dgroups=unit
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/agibank/clientes/
│   │       ├── api/                    # Camada de API (Controllers, DTOs, Mappers)
│   │       ├── config/                 # Configurações da aplicação
│   │       ├── domain/                 # Lógica de negócios
│   │       │   ├── model/              # Modelos de domínio
│   │       │   ├── repository/         # Interfaces de repositório
│   │       │   └── usecase/            # Casos de uso
│   │       └── infrastructure/         # Implementações de infraestrutura
│   │           └── persistence/        # JPA entities e repositórios
│   └── resources/                      # Arquivos de configuração
└── test/                               # Testes automatizados
```

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
