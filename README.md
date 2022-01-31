Restaurant Voting REST API
===============================

#### This is graduation project for [topjava](https://topjava.ru/) internship program
The task was to build a voting system for users to decide where to have a lunch. Main points:

- Two types of users: ADMIN and USER;
- Admin can create a restaurant,and it's lunch menu for the day (2-5 positions, just dish name and price);
- Menu changes each day (admins do the updates);
- Users can vote on which restaurant they want to have lunch at;
- Only one vote is counted per user;
- If user votes again the same day:
  - if it's before 11 am it's assumed that he has changed his mind;
  - if it's after - then it's too late, vote can't be changed :)

Each restaurant provides new menu each day.

-------------------------------------------------------------
- Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 2.5, Lombok, H2, Caffeine Cache, Swagger/OpenAPI 3.0 
- Run: `mvn spring-boot:run` in root directory.
-----------------------------------------------------
[REST API documentation](http://localhost:8080/swagger-ui.html)  

Credentials:
```
User 1:  user1@gmail.com / user_1_password
User 2:  user2@yandex.ru / user_2_password
Admin:   admin@gmail.com / admin
```

For requests checking the restaurant's previous dishes (requests which using date) - please use ISO date format:
```
ISO date format: 2019-02-10
```