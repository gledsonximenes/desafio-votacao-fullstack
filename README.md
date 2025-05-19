# Desafio Votação - Cooperativa

Este é um sistema completo de votação online desenvolvido para atender ao desafio Fullstack da DBServer, utilizando **Java (Spring Boot)** no backend e **React (TypeScript + Material UI)** no frontend.

## Funcionalidades

* Cadastro de pauta
* Abertura de sessão de votação com tempo configurável ou padrão de 1 minuto
* Registro de votos com restrição de 1 voto por associado por pauta
* Bloqueio de votos após fim da sessão
* Apuração de votos
* Validação fake de CPF (ABLE\_TO\_VOTE / UNABLE\_TO\_VOTE)
* Interface responsiva com feedback visual
* Swagger para documentação da API

---

## Tecnologias Utilizadas

### Backend

* Java 17
* Spring Boot 3
* Spring Data JPA
* H2 Database (persistente)
* Lombok
* MapStruct
* Swagger/OpenAPI
* JUnit + Mockito

### Frontend

* React 18 (TypeScript)
* Material UI (MUI)
* Axios
* React Router DOM

---

## Instruções de Execução

### Requisitos:

* Java 17+
* Node.js 18+
* Maven

### Clonando o Projeto

```bash
git clone https://github.com/gledsonximenes/desafio-votacao-fullstack.git
cd desafio-votacao-fullstack
```

### Rodar o docker para criação da base de dados local Postgres

```bash
docker-compose up --build
```

### Rodando Manualmente

#### Backend

```bash
cd backend
mvn spring-boot:run
```

O backend será iniciado em `http://localhost:8080`

Acesse a documentação da API via Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

Ou diretamente pelos dados da especificação:

```
/v3/api-docs
```

#### Frontend

```bash
cd frontend
npm install
npm start
```

O frontend será iniciado em `http://localhost:3000`

---

## Endpoints Principais

| Método | Endpoint                            | Função                                     |
| ------ | ----------------------------------- | ------------------------------------------ |
| POST   | /api/v1/pautas                      | Cria nova pauta                            |
| GET    | /api/v1/pautas                      | Lista pautas                               |
| DELETE | /api/v1/pautas/{id}                 | Exclui uma pauta e seus dados relacionados |
| POST   | /api/v1/sessoes                     | Abre sessão para uma pauta                 |
| GET    | /api/v1/sessoes                     | Lista todas as sessões de votação          |
| GET    | /api/v1/sessoes/{pautaId}/resultado | Obtém resultado da sessão                  |
| POST   | /api/v1/votos                       | Registra voto                              |
| GET    | /api/v1/votos/{pautaId}/resultado   | Reconta votos da pauta                     |

### Schemas

* `PautaDTO`
* `SessaoDTO`
* `SessaoResultadoDTO`
* `VotoDTO`

---

## Bônus Implementados

* [x] Verificação fake de CPF (able/unable)
* [x] Bloqueio de votos após fim da sessão
* [x] Impedimento de votos duplicados por associado

---

## Observações Importantes

* Todo o sistema está funcionando 100% local
* Sessões expiram automaticamente após tempo limite (validação no voto)
* Respostas de erro estão padronizadas com mensagens claras
* O frontend exibe feedbacks para todas as ações
* Logs de operações principais estão registrados via `logger.info`
* O Swagger está disponível no backend para documentação e testes

---

## Testes

### Backend

```bash
cd backend
mvn test
```

---

## Autor

**Gledson Yuri Klein Ximenes**

---

## Licença

MIT
