package com.example;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class DatabaseConfig {

    // private final Environment env;
    private String user = System.getenv("DB_USER");
    private String password = System.getenv("DB_PWD");
    private String serverName = System.getenv("DB_SERVER");
    private String dbName = "team-member-projects";
    private MysqlDataSource datasource = new MysqlDataSource();

    public DatabaseConfig(Environment env) {
        datasource.setPassword(password);
        datasource.setUser(user);
        datasource.setServerName(serverName);
        datasource.setDatabaseName(dbName);
        //datasource.setURL("jdbc:mysql://<localhost>:<3306>/");

    }

    @Bean
    public Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }
}
