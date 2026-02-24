# Industrial Inventory (Autoflex Practical Test)

Backend (Spring Boot 3) + simple Front-End (SPA com HTML/Bootstrap/JS) para controle de produtos, matérias-primas, associações e sugestão de produção com priorização por valor.

## Tecnologias
- Java 17+, Spring Boot 3, Spring Web, Spring Data JPA, Validation
- Banco: PostgreSQL (produção) / H2 (dev e testes)
- Swagger/OpenAPI (springdoc)
- Lombok
- Front-End: HTML + Bootstrap 5 + JavaScript (fetch API)

## Como rodar

### 1) Perfil de desenvolvimento (H2 + Front-End estático servido pelo Spring)
- Compilar e subir:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

- Acessos:
  - Front-End: http://localhost:8080/index.html
  - Swagger UI: http://localhost:8080/swagger
  - OpenAPI JSON: http://localhost:8080/api-docs

### 2) Perfil padrão (PostgreSQL)
- Configure variáveis de ambiente (ou edite `application.yml`):
  - `DB_URL` (padrão `jdbc:postgresql://localhost:5432/inventory`)
  - `DB_USERNAME` (padrão `postgres`)
  - `DB_PASSWORD` (padrão `postgres`)

- Subir:

```bash
mvn spring-boot:run
```

Front-End alternativo (fora do Spring): abra o arquivo `frontend/index.html` no navegador e, na barra superior, defina a “API Base URL” como `http://localhost:8080` e clique em Save.

## Endpoints principais
- `GET /products` | `POST /products` | `PUT /products/{id}` | `DELETE /products/{id}`
- `GET /raw-materials` | `POST /raw-materials` | `PUT /raw-materials/{id}` | `DELETE /raw-materials/{id}`
- `POST /product-raw-materials` | `DELETE /product-raw-materials/{id}`
- `GET /production/suggestions`

## Regras de negócio da produção
- Ordena produtos por preço (maior para menor).
- Para cada produto, calcula o mínimo de `floor(stockQuantity / quantityRequired)` entre todas as matérias-primas necessárias.
- Consome estoque temporariamente conforme cada produto sugerido.
- Resultado: `productId`, `productName`, `quantityToProduce`, `totalValue`.

## Estrutura de pastas (resumo)
- `src/main/java/com/example/inventory`
  - `controller/` – Controllers REST
  - `service/` – Regras de negócio e mapeamento DTO
  - `repository/` – Repositórios JPA
  - `entity/` – Entidades JPA
  - `dto/` – DTOs de entrada/saída
  - `exception/` – Exceções e handler global
  - `config/` – Swagger e CORS
- `src/main/resources/static/` – Front-End (index.html, app.js, styles.css)
- `frontend/` – Versão alternativa do front (mesmos arquivos), caso queira abrir fora do Spring

## Testes
- Integração com H2:

```bash
mvn test
```

Inclui teste de fluxo de Produtos e de sugestões de produção.

## Git e Deploy no GitHub
Este repositório já está versionado em Git e com commits por etapa (init, entidades, serviços, controllers, testes, front, etc.). Para publicar no GitHub:

1. Crie um repositório vazio no GitHub (sem README inicial).
2. Configure o remoto e envie:

```bash
git remote add origin https://github.com/SEU_USUARIO/SEU_REPO.git
git branch -M main
git push -u origin main
```

Se já existir um remoto, verifique com:

```bash
git remote -v
```

Caso necessário, troque o nome do remoto/URL:

```bash
git remote set-url origin https://github.com/SEU_USUARIO/SEU_REPO.git
```

## Observações
- CORS habilitado globalmente para facilitar integração do front: `config/CorsConfig.java`.
- RNFs atendidos: API separada do front, responsividade, persistência em SGBD, uso de framework moderno, código em inglês (classes, tabelas, colunas), e documentação com Swagger.
- Melhorias sugeridas: paginação/ordenação nos GETs, listagem de associações, autenticação, testes e2e (Cypress), UI em React + Redux.

