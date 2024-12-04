# Project Description   
RESTful API for polynomial processing: simplification & evaluation. The application has a Postgres relational DB for storing queries and second-level cache for the optimization of the processing time.


**Quick Start**

1. Clone the project [repository](https://github.com/VolodPol/polynomial):
```
git clone https://github.com/VolodPol/polynomial.git
```

2. Start a PostgreSQL service locally or within a Docker container. If locally, run a following command in a terminal:

```
sudo systemctl start postgresql.service
```
After running the command default 'postgres' database will be available.

3. Update DB credentials in the ***application.properties*** file with your username, password and url if the port is not default:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=postgres
spring.datasource.password=postgres
```

4. Run the application with maven command: 
```
mvn spring-boot:run
```