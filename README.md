#### This project is for the Devops bootcamp exercise for 
#### "Containers - Docker" 

# Solution
Start mysql container using docker
    docker run -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=rootpass \
    -e MYSQL_DATABASE=team-member-projects \
    -e MYSQL_USER=admin \
    -e MYSQL_PASSWORD=adminpass \
    -d mysql

Alternatively start with docker-compose
    `docker-compose -f docker-compose.yaml up`

Create java jar file 
    `./gradlew build`

Set env vars in Terminal and start the app from jar file
    `[\W (master)]$ export DB_USER=admin`
    `[\W (master)]$ export DB_PWD=adminpass`
    `[\W (master)]$ export DB_SERVER=localhost`
    `[\W (master)]$ export DB_NAME=team-member-projects`
    `[\W (master)]$ java -jar build/libs/bootcamp-java-mysql-project-1.0-SNAPSHOT.jar`

NOTE: this won't work if you set the env vars in terminal and then start the app in IntelliJ or start jar file from another terminal session 

# Alternative solution
Replace the hard-coded values in docker-compose with env vars
    `MYSQL_DATABASE: ${DB_NAME}`

Export env vars as shown above
    `export DB_NAME=team-member-projects`

Start docker-compose file in the same terminal session where you set env vars
    `docker-compose -f docker-compose.yaml up --detach`

NOTE: You can also start docker container with env var values
    docker run -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=${ROOT_PWD} \
    -e MYSQL_DATABASE=${DB_NAME} \
    -e MYSQL_USER=${DB_USER} \
    -e MYSQL_PASSWORD=${DB_PWD} \
    -d mysql

Build and start jar file (In the same terminal session where you set the env vars)
    `./gradlew build`
    `java -jar build/libs/bootcamp-java-mysql-project-1.0-SNAPSHOT.jar`
