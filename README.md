# 🏦 Invest Bank API

[<img align="right" width="120" src="https://imd.ufrn.br/portal/assets/images/nova-marca/1A-Primaria-Gradiente.svg">](https://www.metropoledigital.ufrn.br)

![License](https://img.shields.io/github/license/rarycoringa/invest-bank-api)

## About this application

 An API responsible to manage an Investment Bank resources built with Java and Spring Boot.

## Technologies

- **Java** *(11.0.17)*
- **Spring Boot** *(2.7.5)*
- **Gradle** *(7.5.1)*
- **Postgres** *(15.1)*
- **Docker** *(20.10.17)*

## How to run

First you need to make sure you have `Docker` and `docker-compose` installed on your machine. Please see the correct versions [here](#technologies).

After that, you will be able to run the command below to start the `PostgreSQL` database defined in `docker-compose.yml`:

```bash
$ docker-compose up --build
```

So you need to make sure you have `Java` and `Gradle` installed on your machine. Please see the correct versions [here](#technologies).

And then, you just need to run this command below to start the Spring Boot application by using gradle.

```bash
$ gradle bootRun
```

## Resources

### Index

- Metadata about this project

### Assets

- Create an asset
- Retrieve all assets
- Retrieve an asset
- Update an asset
- Remove an asset

### Cryptocurrencies

- Create a cryptocurrency
- Retrieve all cryptocurrencies
- Retrieve a cryptocurrency
- Update a cryptocurrency
- Remove a cryptocurrency

### Wallets

- Create a wallet
- Retrieve all wallets
- Retrieve a wallet
- Update a wallet
- Remove a wallet
- Deposit in a wallet
- Withdraw in a wallet
- Purchase, list and sell assets
- Purchase, list and sell cryptocurrencies