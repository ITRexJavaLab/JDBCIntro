package com.itrex.java.lab;

import static com.itrex.java.lab.properties.Properties.H2_PASSWORD;
import static com.itrex.java.lab.properties.Properties.H2_URL;
import static com.itrex.java.lab.properties.Properties.H2_USER;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.JDBCUserRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.h2.jdbcx.JdbcConnectionPool;

public class Runner {

    public static void main(String[] args) {
        System.out.println("===================START APP======================");
        System.out.println("================START MIGRATION===================");
        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        System.out.println("============CREATE CONNECTION POOL================");
        JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);

        System.out.println("=============CREATE UserRepository================");
        UserRepository userRepository = new JDBCUserRepositoryImpl(jdbcConnectionPool);
        List<User> users = userRepository.selectAll();
        System.out.println("Step 1 select all users:\n" + users);
        User firstAddedUser = new User();
        firstAddedUser.setName("some name");
        firstAddedUser.setEmail("some email");
        firstAddedUser.setDateOfBirth(Timestamp.valueOf(LocalDateTime.now()));

        userRepository.add(firstAddedUser);
        System.out.println("Step 2 add user:\n" + firstAddedUser);

        users = userRepository.selectAll();
        System.out.println("Step 3 select all users:\n" + users);
        List<User> newUsers = new ArrayList<>();
        User user = new User();
        user.setName("some name");
        user.setEmail("some email 2");
        user.setDateOfBirth(Timestamp.valueOf(LocalDateTime.now()));

        newUsers.add(user);
        User user2 = new User();
        user2.setName("some name");
        user2.setEmail("some email 2");
        user2.setDateOfBirth(Timestamp.valueOf(LocalDateTime.now()));
        newUsers.add(user2);

        userRepository.addAll(newUsers);
        System.out.println("Step 4 add all users:\n" + newUsers);

        users = userRepository.selectAll();
        System.out.println("Step 5 select all users:\n" + users);

        System.out.println("=========CLOSE ALL UNUSED CONNECTIONS=============");
        jdbcConnectionPool.dispose();
        System.out.println("=================SHUT DOWN APP====================");
    }

}
