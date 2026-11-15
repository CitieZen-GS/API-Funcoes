# Ajudante Familiar: API de Funções

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

## Endpoints

Todas as rotas exigem `Authorization: Bearer <jwt>`. O token é assinado em HS256 com o segredo `jwt.segredo` (variável `JWT_SEGREDO`) e traz a claim `roles`:

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
