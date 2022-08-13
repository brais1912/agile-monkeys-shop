Agile monkeys shop management
==============================

#### Table of contents
1. [Project description](#project-description)
2. [Used technologies](#used-technologies)
3. [Use cases](#use-cases)
4. [How to install and run the project](#how-to-install-and-run-the-project)
5. [REST API documentation](#rest-api-documentation)
6. [How to test the application](#how-to-test-the-application)

<a name="project-description"></a>
# Project description
Currently in Agile Monkeys, we need to build a REST API service to manage customer data for a small shop. It
will work as the backend side for a CRM interface that is being developed by a different team.

The API should be only accessible by a registered user by providing an authentication mechanism.

<a name="used-technologies"></a>
# Used technologies
The following technologies were used to develop this service:
* Java 17
* Spring boot
* Spring security and JWT
* Swagger (OpenAPI)
* Docker, docker-compose
* MongoDB

<a name="use-cases"></a>
# Use cases

## Customer
A non-admin user can only perform the following actions:

### List all customers in the database
Retrieve all customers in the database in a lite format

### Get full customer information
Retrieve all the information for a specific customer, including a photo URL (full format)

### Create a new customer
Add a new customer to our system. A customer may contain the following information:
* Name (required)
* Surname (required)
* ID (required)
* Photo
* Created by
* Updated by (Last user who modified it)

Image uploads should be able to be managed.

Images are saved by default in ./photo/ path. You can configure this path by modifying the src/main/resources/application.yml file located in boot module.

### Update an existing customer
The customer should hold a reference to the last user who modified it

### Delete an existing customer
Delete an existing customer

## User
An admin user can also manage users. Therefore, this type of user can perform the following actions:

### List all users in the database
Retrieve all users in the database

### Create users
Add a new user to our system.

### Update an existing user
Update an existing user and even modify its status

### Delete users
Delete an existing user

<a name="how-to-install-and-run-the-project"></a>
# How to install and run the project
1. First, it is necessary to build the project so that the DTOs related to the API Rest are autogenerated.
These classes are autogenerated through the openapi-rest.yml file located in the application module and are necessary to run the project.

You need to have at least Java version 17 installed on your computer if you want to run this project locally.

You must open the Command prompt and go to the root path of the project. Once here, if you have Maven installed and configured on your computer, you can run the following command:
* mvn clean install

If you do not have Maven installed, run the following command:
* .\mvnw.cmd clean install (Windows) or ./mvnw.cmd clean install (Unix)

2. After having executed the Maven command "clean install", we will have the project built and the DTOs generated.

The next step is to run Docker desktop, where we will deploy the container with a MongoDB image to work with the database.

To do this, located in the root of the project and using the Command prompt, execute the following command:
* docker-compose up -d

This command, besides deploying a container with a MongoDB image in Docker, will execute a .js script that will create the necessary collections to be able to work with this service.

In addition, it will create a single admin user, whose credentials are as follows:
* Username: admin
* Password: agilemonkeysshop

3. Finally, we will have to run our application.

This can be done either by using an IDE, such as IntelliJ, or through the Command prompt.

If we want to do it using the Command prompt, we must go to the path ./agile-monkeys-shop-boot/target, which is where the executable JAR named "agile-monkeys-shop-boot-0.0.1-SNAPSHOT.jar" will be generated.

Once here, we will execute the following command:
* java -jar agile-monkeys-shop-boot-0.0.1-SNAPSHOT.jar

After this, we will have everything ready to be able to work with this service.

<a name="rest-api-documentation"></a>
# REST API documentation
You can access the URL /swagger-ui.html or /swagger-ui/index.html to view the REST API documentation.
* http://localhost:8080/swagger-ui.html

<a name="how-to-test-the-application"></a>
# How to test the application
Postman collections are provided in order to test the different REST API endpoints. These collections are located in ./src/test/
* Agile Monkeys Shop.postman_collection.json
* Local.postman_environment.json (Local environment)

These postman collections already set both the environment (localhost) and the token obtained each time a request is sent to the authentication endpoint. Therefore, there is no need to copy and paste the token to test each endpoint.

In order to test the endpoints, we must first obtain a token for the admin user (credentials informed in the section [How to install and run the project](#how-to-install-and-run-the-project)).

For this purpose, there is a public endpoint whose URL is: /agile-monkeys/v1/public/authenticate ("Authenticate - Get Jwt Token" in Postman collection)

We must execute a POST against this URL with the username and password of the admin and it will return the JWT Token, necessary to be able to test the endpoints.

From here, we can test any endpoint.