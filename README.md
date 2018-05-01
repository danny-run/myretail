# My Retail

A simple project that demo's service APIs for products. It is coded with Java/Spring Boot.

## Getting Started

The service is compiled using Maven, and runs on Spring Boot with MongoDB.

### Prerequisites

Things to install.

```
JDK 8
MongoDB
Maven
SpringBoot
Docker
Jenkins Container
```
## Endpoint

* # myretail
A demo project of myretail service

# endpoints
* GET /products/{id} provides details of product id, including id, current price, currency code and name. The product name is retrieved from an external URL, which requires an HTTP to HTTPS redirect. In case that the product is not found or the product name is not provided by the external URL. An corresponding error message is return.
* PUT /products/{id} provides an endpoint to update product current price.
* GET /products, POST /products, DELETE /products/{id} provides features that help to test service APIs.
## Running the tests

Both Integration test and unit test cases are provided. Also, Jacoco plugin is added to generate a coverage report.

```
mvn clean verify
```

## Deployment

Add additional notes about how to deploy this on a live system




## Authors

* **Danny Liu** - *Initial work* - (https://github.com/PurpleBooth)





