package com.example;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class Application {

    private final Connection dbConnection;

    public Application(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init()
    {
        Logger log = LoggerFactory.getLogger(Application.class);
        log.info("Java app started");

        log.info("Connecting to the Mysql database");
        try {
            String sqlStatement = "CREATE TABLE IF NOT EXISTS team_members(" +
                    "member_id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "member_name VARCHAR(255),\n" +
                    "member_role VARCHAR(255),\n" +
                    "member_projects VARCHAR(255)" +
                    ")";
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate(sqlStatement);
            stmt.close();
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return "OK";
    }
}
