# IronMusic

## Description of the porject
It's a backend RESTful API application built with Java and Spring Boot, designed to create a database for a platform that stores audio files.
Spring Security has been implemented in the application to manage permissions for different types of users. There are three roles for users:
  - Admins: They accept new users and activate new artists.
  - Artists: They can add and remove content.
  - Standard users: They can browse the platform's content and create their own playlists. They can also share their playlists with other users.

The application have two classes of audio(inherited classes): songs and podcasts. Each audio has the following attributes:

  - Artist: This refers to the artist who added the audio to the platform.
  - Title: The title of the audio piece.
  - Duration: The length of the audio.
    
In addition to these shared attributes, songs have a genre and can be associated with an album. Albums, in turn, are collections of songs.

On the other hand, podcasts have a season and episode number and an attribute of genre.
    
## Class diagram 
![Entity-Relationship Model](https://raw.githubusercontent.com/JuliaORS/IronMusic/master/assets/Entity-Relationship%20Model.png)

## Setup
### 1 - Prerequisites
- IntelliJ IDEA [Download IntelliJ IDEA](https://java.tutorials24x7.com/blog/how-to-install-java-17-on-windows)
- Maven [Download Maven](https://maven.apache.org/download.cgi)
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
    
    [Link to API documentation](http://localhost:8080/swagger-ui/index.html) (while application is running)

## Controllers and Routes structure
Our backend serves as the backbone, handling incoming requests, processing data, and interacting with our database through defined endpoints.

### Architecture of application
At the core of backend lies the concept of controllers, services, and repositories, orchestrated to deliver seamless communication between application and external entities such as Postman or any other client.

Controllers: These act as intermediaries, receiving HTTP requests, processing them, and delegating the appropriate actions to the corresponding service.

Services: They encapsulate the business logic of the application, orchestrating various operations to fulfill the requests received from the controllers.

Repositories: Responsible for interacting with the database, repositories provide an abstraction layer enabling seamless data manipulation.

### Endpoints
The application exposes several endpoints, each serving a distinct purpose and catering to specific functionalities. 
These endpoints are defined to ensure proper authorization based on user roles:
  - api/admin: Endpoints that start with 'admin' are exclusively available to users with the ADMIN role.
  - api/artist: Endpoints that start with 'artist' are accessible only to users with the ARTIST role.
  - api/user: Endpoints that start with 'user' are available to all STANDARD users, including artists.
This structure ensures that sensitive functionalities are appropriately secured, enhancing the overall security of our application.

#### User controller
Manages the registration (sign up) of new users, login functionality, as well as the activation of users and artists by the administrator.
![user2](https://github.com/JuliaORS/IronMusic/assets/128370372/3215b44f-42d9-44e8-9978-cd5bb55131e9)

#### Audio controller
Manages the search for audio files by the users.
![audio controller](https://github.com/JuliaORS/IronMusic/assets/128370372/b5ff24f6-f666-49f4-8b92-7e779af35ab5)

#### Song controller
Manages the creation of new songs by artists and allows users to search for songs.
![song controller](https://github.com/JuliaORS/IronMusic/assets/128370372/3a26b2db-9997-401d-821b-1b60e97b0148)

#### Podcast controller
Manages the creation of new podcast by artists and allows users to search for podcasts.
![podcast controller](https://github.com/JuliaORS/IronMusic/assets/128370372/1ac55803-566c-47f9-954b-964152797767)

##### Album controller
Manages the creation of new albums, addition and removal of songs to albums by artists, and album search made by users.

![album controller](https://github.com/JuliaORS/IronMusic/assets/128370372/7ba91ca3-ca92-4383-b066-e3c5d88ddc5d)

#### Artist controller
Manages the search for the audio files owned by the artist.
![artist controller](https://github.com/JuliaORS/IronMusic/assets/128370372/491f04cf-c634-42a8-81d8-f51afcd2f03f)

#### Playlist controller
Manages the creation and deletion of playlists, addition and removal of songs in playlists, the ability to make playlists public for access by all users, and the addition of new users to private playlists for sharing audios.
![playlist controller](https://github.com/JuliaORS/IronMusic/assets/128370372/18a9d193-26e7-4929-af32-66372eb300fc)

## Extra links
  - [Diagram Gantt](https://www.canva.com/design/DAGClVOlTxA/YxPZe_Un7bHsZYI5ikFenQ/edit?utm_content=DAGClVOlTxA&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)
  - [Trello](https://trello.com/invite/b/zafobhol/ATTIfc0d130811c3c3ae09fa51690df352ffA47B32D2/ihfinalproject)
  - [Presentation](https://www.canva.com/design/DAGE6j70Ajo/eiBoWcGcLc5vi6VkmezEXw/edit?utm_content=DAGE6j70Ajo&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)
    
## Future work
  - Improve the content search functionality of my application.
  - Improve user management of playlists: add the possibility of having an admin who can add or remove users. Currently, all playlist users can add or remove users
  - Add a new model that consists of user groups where multiple playlists can be shared.

## Resources
  - [Open api documentation](https://www.baeldung.com/spring-rest-openapi-documentation)
  - [Relationships in Spring Data REST documentation](https://www.baeldung.com/spring-data-rest-relationships)
  - [Validation request params documentation](https://www.baeldung.com/spring-validate-requestparam-pathvariable)
  - [Jakarta validation constraints documentation](jakarta.validation.constraints)
