# Ajudante Familiar: API de Funções.

App para auxiliar nos cuidados de idosos. Este repositório contém a **API de funções**, responsável pelas operações HTTP de cuidador e paciente.

## Resumo do projeto

O app tem dois perfis de acesso:

- **Cuidador**: cadastra medicamentos e informações do idoso, registra atividades a serem feitas, marca lembretes de consultas futuras no calendário e registra a alimentação.
- **Familiar**: apenas visualiza tudo o que o cuidador registrar (medicamentos, alimentação, futuras consultas e atividades diárias).

## Tecnologias

Versões declaradas no `pom.xml`:

- **Java 21**
- **Spring Boot 4.1.2** (`spring-boot-starter-parent`)
- **Maven**
- **springdoc-openapi 3.1.0** (`springdoc-openapi-starter-webmvc-ui`): documentação Swagger / OpenAPI

Dependências com versão gerenciada pelo Spring Boot 4.1.2 (sem versão explícita no `pom.xml`):

- **Spring Web MVC** (`starter-webmvc`): API REST
- **Spring Data JPA** (`starter-data-jpa`): persistência e acesso ao banco
- **Spring Security** (`starter-security`) e **OAuth2 Resource Server** (`starter-security-oauth2-resource-server`): proteção dos endpoints e validação de tokens JWT emitidos pela API de autenticação
- **Spring AMQP** (`starter-amqp`): integração com o **RabbitMQ** para comunicação entre os microsserviços
- **Bean Validation** (`starter-validation`): validação dos dados de entrada
- **Spring Boot Actuator** (`starter-actuator`): monitoramento e health checks
- **Flyway** (`starter-flyway` e `flyway-database-oracle`): migrations e versionamento do schema
- **Oracle Database** (driver `ojdbc17`): banco de dados relacional
- **Lombok**: redução de código repetitivo
- **Spring Boot Docker Compose** (`spring-boot-docker-compose`): sobe os serviços de apoio em desenvolvimento

Testes:

- **Spring Boot Test** e starters de teste (JPA, Flyway, AMQP, Security, WebMVC, Validation, Actuator)
- **Testcontainers** (JUnit Jupiter, Oracle Free e RabbitMQ): testes de integração com banco e mensageria reais

Nas demais APIs do sistema:

- **JWT**: autenticação e permissões
- **RAG**: busca de informações do paciente e geração de relatórios

## Arquitetura

O sistema é composto por 3 microsserviços que se comunicam via RabbitMQ:

1. **API de funções**: funções HTTP do cuidador e do paciente
2. **API de autenticação**: validação de acessos e permissões via JWT
3. **API de RAG**: chat que busca informações do paciente e gera relatórios conforme o cuidador pedir

### Organização da API de funções

| Package | Responsabilidade |
|---|---|
| `models` | Entidades e relacionamentos de tabelas |
| `repositories` | Acesso ao banco de dados, consultas e alterações |
| `services` | Regras de negócio |
| `dto` | Objetos que definem o que entra e o que sai da API |
| `controller` | Recebe as requisições HTTP |

O schema do banco é gerenciado exclusivamente pelo Flyway. Migrations já mergeadas nunca são editadas: crie sempre uma nova.

## Como rodar

### Pré-requisitos

- **JDK 21** ou superior (`JAVA_HOME` apontando para ele)
- **Docker Desktop** aberto, com o motor rodando ("Engine running") e em modo **Linux containers** (requer WSL 2 no Windows)

Não é preciso instalar Maven, Oracle nem RabbitMQ: o projeto usa o Maven Wrapper (`mvnw`) e sobe os serviços pelo `compose.yaml`.

### Subindo a aplicação

```powershell
# Windows (PowerShell)
.\mvnw spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Ao iniciar, o Spring Boot executa o `docker compose up`, espera o Oracle ficar saudável (healthcheck), conecta usando as credenciais do `compose.yaml` e o Flyway cria as tabelas (`M_AJ_V01` a `M_AJ_V12`). Na primeira execução o download da imagem do Oracle pode levar alguns minutos.

Quando a aplicação é encerrada, os containers que ela subiu são parados (`docker compose stop`).

### Serviços e portas

| Serviço | Endereço | Credenciais |
|---|---|---|
| API | http://localhost:8080 | JWT (ver [Endpoints](#endpoints)) |
| Swagger UI | http://localhost:8080/swagger-ui.html | público |
| Oracle Free | `localhost:1521`, serviço `FREEPDB1` | `ajudante` / `secret` (admin: `system` / `secret`) |
| RabbitMQ (AMQP) | `localhost:5672` | `myuser` / `secret` |

As portas são fixas no `compose.yaml`. Se alguma já estiver em uso na sua máquina (`netstat -ano | findstr :1521`), troque o número da esquerda, por exemplo `'1522:1521'`.

### Acessando o banco (Oracle SQL Developer)

Nova conexão com: usuário `ajudante`, senha `secret`, tipo **Básico**, host `localhost`, porta `1521` e **Nome do serviço** `FREEPDB1` (não SID). As tabelas ficam no schema `AJUDANTE`, junto com a `flyway_schema_history`.

Para acessar o banco **sem a aplicação rodando**, suba só o Oracle:

```powershell
docker compose up -d oracle   # sobe o banco
docker compose stop           # desliga, mantendo os dados
```

Se o container já estiver rodando quando a aplicação iniciar, ela o reutiliza e não o desliga ao encerrar.

> **Atenção:** `docker compose down` remove os containers e, como não há volume configurado, **apaga os dados do banco**. Na próxima subida o Flyway recria as tabelas vazias.

### Dados iniciais para teste

O cadastro de cuidadores pertence à API de autenticação. Para testar localmente, crie um cuidador direto no banco:

```sql
INSERT INTO T_Ajudante_familiar_Cargo (nm_cargo, fg_permissao) VALUES ('Cuidador', 'S');
INSERT INTO T_Ajudante_familiar_Perfil (nm_perfil, id_cargo) VALUES ('Cuidador', 1);
INSERT INTO T_Ajudante_familiar_Cuidador (nm_cuidador, id_perfil) VALUES ('Maria', 1);
COMMIT;
```

### Token JWT para teste

Enquanto a API de autenticação não existe, gere um token em [jwt.io](https://jwt.io) com algoritmo **HS256**, o segredo de desenvolvimento `ajudante-familiar-segredo-dev-troque-em-producao` e o payload:

```json
{ "sub": "1", "roles": ["CUIDADOR"], "exp": 1893456000 }
```

Use `"roles": ["FAMILIAR"]` para testar o acesso somente leitura. No Swagger, cole o token em **Authorize** (sem o prefixo `Bearer`).

### Variáveis de ambiente

Em desenvolvimento nenhuma é obrigatória. Fora do Docker Compose (servidor ou `java -jar`, onde o `spring-boot-docker-compose` não é incluído), defina:

| Variável | Descrição |
|---|---|
| `JWT_SEGREDO` | Segredo HS256 dos tokens (mínimo 32 caracteres, igual ao da API de autenticação) |
| `SPRING_DATASOURCE_URL` | Ex.: `jdbc:oracle:thin:@//host:1521/FREEPDB1` |
| `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` | Usuário e senha do Oracle |
| `SPRING_RABBITMQ_HOST` / `SPRING_RABBITMQ_PORT` | Endereço e porta do RabbitMQ |
| `SPRING_RABBITMQ_USERNAME` / `SPRING_RABBITMQ_PASSWORD` | Usuário e senha do RabbitMQ |

### Problemas comuns

| Erro | Causa e solução |
|---|---|
| `mvnw` não é reconhecido | No PowerShell use `.\mvnw` |
| `failed to connect to the docker API ... dockerDesktopLinuxEngine` | Docker Desktop fechado ou ainda iniciando: abra e espere "Engine running" |
| `ORA-01017: invalid credential` | Container antigo, criado antes do `APP_USER`: rode `docker compose down` e suba de novo |
| `port is already allocated` | Porta 1521 ou 5672 em uso: altere o número da esquerda no `compose.yaml` |

## Endpoints

Documentação interativa (Swagger UI) em **http://localhost:8080/swagger-ui.html** e JSON OpenAPI em `/v3/api-docs`. Use o botão **Authorize** para informar o JWT.

Todas as rotas exigem `Authorization: Bearer <jwt>`. O token é assinado em HS256 com o segredo `jwt.segredo` do `application.yml` (variável de ambiente `JWT_SEGREDO`) e traz a claim `roles`:

- `CUIDADOR`: leitura e escrita (GET, POST, PUT, PATCH, DELETE)
- `FAMILIAR`: somente leitura (GET)

Datas no formato `aaaa-mm-dd` e horas no formato `hh:mm`.

| Recurso | Método e rota | Descrição |
|---|---|---|
| Idoso | `GET /idosos?idCuidador=` | Lista idosos (filtro opcional por cuidador) |
| | `GET /idosos/{id}` | Busca idoso |
| | `GET /idosos/{id}/resumo?data=` | Resumo do dia: doenças, medicamentos, atividades, alimentação e próximas consultas |
| | `POST /idosos` · `PUT /idosos/{id}` · `DELETE /idosos/{id}` | Cadastra, atualiza, remove |
| Doenças | `GET/POST /idosos/{idIdoso}/doencas` | Lista / cadastra |
| | `GET/PUT/DELETE /doencas/{id}` | Busca / atualiza / remove |
| Medicamentos | `GET/POST /idosos/{idIdoso}/medicamentos` | Lista / cadastra |
| | `GET/PUT/DELETE /medicamentos/{id}` | Busca / atualiza / remove |
| Atividades | `GET /idosos/{idIdoso}/atividades?data=` · `POST /idosos/{idIdoso}/atividades` | Lista (filtro por dia) / cadastra |
| | `GET/PUT/DELETE /atividades/{id}` | Busca / atualiza / remove |
| Alimentação | `GET /idosos/{idIdoso}/alimentacoes?data=&tipo=` · `POST /idosos/{idIdoso}/alimentacoes` | Lista (filtros por dia e tipo) / cadastra |
| | `GET/PUT/DELETE /alimentacoes/{id}` | Busca / atualiza / remove |
| Lembretes de consulta | `GET /idosos/{idIdoso}/lembretes-consulta?status=` · `POST /idosos/{idIdoso}/lembretes-consulta` | Lista (filtro por status) / cadastra |
| | `GET/PUT/DELETE /lembretes-consulta/{id}` | Busca / atualiza / remove |
| | `PATCH /lembretes-consulta/{id}/status` | Marca como `REALIZADO` ou `PENDENTE` |
| | `POST/DELETE /lembretes-consulta/{id}/medicamentos/{idMedicamento}` | Vincula / desvincula medicamento |

Respostas de erro: `400` dados inválidos, `401` sem token, `403` perfil sem permissão, `404` não encontrado, `409` conflito com o banco, `422` regra de negócio.
