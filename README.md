# My Retail

A simple project that demo's service APIs for products. It is coded with Java/Spring Boot.

## Getting Started

The service is compiled using Maven, and runs on Spring Boot with MongoDB.

### Prerequisites

Things to install.

```
JDK 8
MongoDB (brew install mongodb)
Maven (https://maven.apache.org/install.html)do
SpringBoot
Docker (https://store.docker.com/editions/community/docker-ce-desktop-mac)
Jenkins Container (https://jenkins.io/doc/book/installing/)
```
## Endpoint

* GET /products/{id} provides details of product id, including id, current price, currency code and name. The product name is retrieved from an external URL, which requires an HTTP to HTTPS redirect. In case that the product is not found or the product name is not provided by the external URL. An corresponding error message is return.
* PUT /products/{id} provides an endpoint to update product current price.
* GET /products, POST /products, DELETE /products/{id} provides features that help to test service APIs.

## Running the tests

Both Integration test and unit test cases are provided. Also, Jacoco plugin is added to generate a coverage report.

```
mvn clean verify
```
## Running the service locally
* Download the source code from https://github.com/danny-run/myretail.git, new_branch.
* Run mvn clean install.
* Run mongod to launch a local mongo instance.
* Run java -jar -Dspring.profiles.active=dev target/myretail-0.0.1-SNAPSHOT.jar to start the service APIs.
* A Data Feeder runs for dev environment and populates data to MongoDB (such as product 13860428).
* Try testing the service API using one preinstalled product id, http://localhost:8080/products/13860428

## Production Readies
* Exception handling is provided at the front layer. An meaningful error message will be generated based on error.
* Muliple profiles (dev, prod, test) are provided to run the app in stages or testing environment.
* CI/CD is supported by a jenkins pipeline and a jenkins file is created to manage the pipeline. The goal is to run various checks, compile and test on the service APIs and create a docker image/file of the product. Later, DevOps are able to deploy the docker image to any environment (such as prod).
. Spring Boot Actuator and Swagger are introduced to the app thus the service APIs' information and health conditions can be viewed on-the-fly.

## Deployment

When the jenkins pipeline promotes code changes to the last stage and produces a docker image/file, a deployment could be processed using Kubernetes or OpenShift.

## Authors

* **Danny Liu** - *Initial work* - (https://github.com/danny-run/myretail.git, new_branch)





