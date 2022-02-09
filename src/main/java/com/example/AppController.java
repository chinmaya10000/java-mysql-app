package com.example;

import io.prometheus.client.CollectorRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

@RestController
public class AppController {

    private final Connection dbConnection;
    static final Counter totalRequests = Counter.build()
            .name("java_app_http_requests_total").help("Total requests.").register();
    static final Gauge inprogressRequests = Gauge.build()
            .name("java_app_inprogress_requests").help("Inprogress requests.").register();

    public AppController(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @GetMapping("/get-data")
    public ResponseEntity getData() {
        inprogressRequests.inc();
        totalRequests.inc();
        List<User> users = fetchDataFromDB();
        inprogressRequests.dec();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/update-roles")
    public ResponseEntity updateRoles(@RequestBody ArrayList<User> users) {
        inprogressRequests.inc();
        totalRequests.inc();
        updateDatabase(users);
        inprogressRequests.dec();
        return ResponseEntity.ok(users);
    }

    private void updateDatabase(ArrayList<User> users) {
        try {
            Statement stmt = dbConnection.createStatement();
            users.forEach(user -> {
                String sqlStatement = String.format("UPDATE team_members SET member_role='%s' WHERE member_name='%s'", user.role, user.name);
                try {
                    stmt.executeUpdate(sqlStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<User> fetchDataFromDB() {
        List<User> users = new ArrayList<>();
        try {
            String sqlStatement = "SELECT member_name, member_role FROM team_members";

            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStatement);
            while(rs.next()) {
                User user = new User();
                user.setName(rs.getString("member_name"));
                user.setRole(rs.getString("member_role"));
                users.add(user);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static class User {
        String name;
        String role;

        private User() {

        }

        public User(String name, String role) {
            this.name = name;
            this.role = role;
        }

        private void setName(String name) {
            this.name = name;
        }

        private void setRole(String role) {
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }
    }
}
