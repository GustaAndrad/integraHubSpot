# Integração com HubSpot - API de Contatos

Um projeto em **Spring Boot** para integração com a API de contatos do HubSpot. Este projeto permite criar contatos no HubSpot utilizando a API REST.

## OAuth 2.0 e Autenticação

Este projeto implementa autenticação via **OAuth 2.0** para interação com o HubSpot. O fluxo de autenticação inclui:

1. **Gerar a URL de autorização:**
   - O usuário é redirecionado para o HubSpot para conceder permissões ao aplicativo.
2. **Trocar código de autorização por um token de acesso:**

   - Após a autorização, o código retornado é trocado por um token de acesso.

3. **Utilização do token de acesso:**
   - O token de acesso é utilizado para fazer chamadas autenticadas na API do HubSpot.

A implementação do serviço `HubspotServiceImpl.java` inclui métodos para:

- **Gerar a URL de autorização:** `getAuthorizationUrl()`
- **Trocar código por token:** `exchangeCodeForToken(String code)`
- **Criar contatos:** `createContact(Contact contactData)`

## O que o projeto faz?

1. **Recebe os dados de um contato via API**

   O projeto expõe um endpoint `POST /create-contact`, onde um contato é enviado no corpo da requisição.

2. **Converte os dados para o formato esperado pelo HubSpot**

   O `Service` do projeto transforma o contato recebido no formato exigido pela API do HubSpot.

3. **Envia a requisição para o HubSpot**

   Utilizando `RestTemplate`, o projeto faz uma chamada `POST` para a API do HubSpot com os dados formatados.

4. **Retorna a resposta da API do HubSpot**

   O sistema recebe e repassa a resposta da API do HubSpot ao cliente que fez a requisição inicial.

## Requisitos

Antes de executar o projeto, certifique-se de ter:

1. **Java 17** ou superior instalado ([Baixar Java](https://www.oracle.com/br/java/technologies/downloads/))
2. **Spring Boot 3.x** configurado
3. Uma conta no **HubSpot** ([Criar conta](https://app.hubspot.com/signup/developers))
4. Seu 'client id' e 'client secret' do HubSpot

## Como executar o projeto

### 1. Clonar o repositório

```bash
$ git clone git@https://github.com/GustaAndrad/integraHubSpot.git
```

### 2. Configurar as credenciais do HubSpot

Entre no arquivo `src/main/resources/application.properties` e adicione:

```properties
hubspot.client-id=YOUR_ID_CLIENT
hubspot.client-secret=YOUR_SECRET_CLIENT
```

> **Nota:** Utilize um token de acesso válido do HubSpot.

### 3. Compilar e executar o projeto

Na raiz do repositório, execute:

```bash
$ mvn clean install
$ mvn spring-boot:run
```

### 4. Testar a API

Realize a autenticação:

```sh
curl --request GET \
  --url http://localhost:8080/integraHubSpot/hubspot/auth-url
```

Envie uma requisição `POST` para criar um contato:

```sh
curl --request POST \
  --url http://localhost:8080/integraHubSpot/hubspot/create-contact \
  --header 'Content-Type: application/json' \
  --data '{
	"email": "example@hubspot.com",
	"firstname": "Jane",
	"lastname": "Doe",
	"phone": "(555) 555-5555",
	"company": "HubSpot",
	"website": "hubspot.com",
	"lifecyclestage": "marketingqualifiedlead"
}'
```

A API responderá com o status e a mensagem da API do HubSpot.

## Webhook

Para receber o retorno do webhook da hubspot, é necessario cadastrar a url em seu app.hubspot.com

```
https://URL-DA-API/integraHubSpot/hubspot/webhook
```
