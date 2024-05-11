# IronMusic

## Description of the porject
It's a backend RESTful API application built with Java and Spring Boot, designed to create a database for a platform that stores audio files.
Spring Security has been implemented in the application to manage permissions for different types of users. There are three types of users:
  - Admins: They accept new users and activate new artists.
  - Artists: They can add and remove content.
  - Standard users: They can browse the platform's content and create their own playlists. They can also share their playlists with other users.
    
The application handles two classes of audio: songs and podcasts, and songs can be grouped into albums.

## Class diagram 
![Entity-Relationship Model](https://raw.githubusercontent.com/JuliaORS/IronMusic/master/assets/Entity-Relationship%20Model.png)

## Setup
### 1 - Prerequisites
- IntelliJ IDEA [https://www.jetbrains.com/es-es/idea/download/?section=windows](https://java.tutorials24x7.com/blog/how-to-install-java-17-on-windows)
- Maven https://maven.apache.org/download.cgi
- Postman or other client-rest.

### 2 - Clone repo
```
  git clone https://github.com/JuliaORS/IronMusic.git
```
## Technologies Used
  - Java
  - Spring Boot: Used to create Java applications with minimal code, providing a ready-to-use execution environment.
  - Spring Security: Used for authentication and authorization in the application.
  - Hibernate: Framework that simplifies the mapping of Java objects to relational databases.
  - IntelliJ IDEA: Integrated development environment (IDE) used to develop the application.
  - Postman:  Used for testing and making HTTP requests to application's API.
  - MySQL Workbench: Used for managing and querying the MySQL database.
  - Swagger: Used to generate interactive API documentation for testing and utilizing the provided APIs.

## Controllers and Routes structure

![user_controller](https://github.com/JuliaORS/IronMusic/assets/128370372/6fcaa8cb-3453-4edc-b3b8-d0c324105622)

## Extra links
  - Canva: https://www.canva.com/design/DAGClVOlTxA/YxPZe_Un7bHsZYI5ikFenQ/edit?utm_content=DAGClVOlTxA&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton
  - Trello: https://trello.com/invite/b/zafobhol/ATTIfc0d130811c3c3ae09fa51690df352ffA47B32D2/ihfinalproject
  - Presentation: ***PENDING
    
## Future work
  - Improve the content search functionality of my application.
  - Improve user management of playlists: add the possibility of having an admin who can add or remove users. Currently, all playlist users can add or remove users
  - Add a new model that consists of user groups where multiple playlists can be shared.

## Resources
  - https://www.baeldung.com/spring-rest-openapi-documentation - open api
  - https://www.baeldung.com/spring-data-rest-relationships - relationships in Spring Data REST between models

