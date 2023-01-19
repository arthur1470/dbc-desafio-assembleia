<center>
  <h1 align="center">Desafio DBC Company</h1>
  <p align="center">
    API Rest de assembleias, gerenciamento de Associados, Pautas e Sessões de votação <br />
    Neste projeto, foi utilizado conceitos de Clean Architecture, DDD, TDD e as boas práticas atuais de mercado
  </p>
</center>
<br />

## Ferramentas necessárias

- JDK 17
- IDE de sua preferência
- Docker

## Como executar?

1. Clonar o repositório:
```sh
git clone https://github.com/arthur1470/dbc-desafio-assembleia.git
```

2. Subir o banco de dados MySQL com Docker:
```shell
docker-compose up -d
```

3. Executar a aplicação como SpringBoot app:
```shell
./gradlew bootRun
``` 

> Também é possível executar como uma aplicação Java através do
> método main() na classe Main.java do Modulo Infrastructure
## Documentação

Consulte a documentação dos endpoints com o swagger. Após executar a aplicação, acesse a URL abaixo:
```
http://localhost:8080/api/swagger-ui/index.html
``` 

## Banco de dados

O banco de dados principal é um MySQL e para subir localmente vamos utilizar o
Docker. Execute o comando a seguir para subir o MySQL:

```shell
docker-compose up -d
```

Pronto! Aguarde que em instantes o MySQL irá estar pronto para ser consumido
na porta 3306.

### Migrações do banco de dados com Flyway

#### Executar as migrações

Caso seja a primeira vez que esteja subindo o banco de dados, é necessário
executar as migrações SQL com a ferramenta `flyway`.
Execute o comando a seguir para executar as migrações:

```shell
./gradlew flywayMigrate
```

Pronto! Agora sim o banco de dados MySQL está pronto para ser utilizado.

<br/>

### Collection Postman

A collection para importar no postman, se encontra na raíz do projeto, na pasta postman.

###	Tarefa Bônus 1 - Integração com sistemas externos

Para atender a esse requisito, foi implementado em Infrastructure > api > V1 > Clients > DefaultUsersClient.
Um client que consulta uma API externa(usei outra api que encontrei na internet, 
pois a mencionada no documento "GET https://user-info.herokuapp.com/users/{cpf}" não está disponível) e retorna se o CPF é válido.
Nesta mesma função, de maneira aleatória, retorna se o Associado está "ABLE_TO_VOTE" ou "UNABLE_TO_VOTE"

### Tarefa Bônus 2 - Performance

Este requisito pode ser resolvido utilizando algum sistema de filas, como RabbitMQ ou ApacheKafka, para poder processar os dados
da requisição de maneira assíncrona

### Tarefa Bônus 3 - Versionamento da API

O versionamento foi feito na estrutura de Infrastructure > api, através da URL adicionando o prefixo da versão, exemplo: /api/v2/associates
