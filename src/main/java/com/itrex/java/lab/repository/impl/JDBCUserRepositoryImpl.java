package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class JDBCUserRepositoryImpl implements UserRepository {

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String EMAIL_COLUMN = "email";
    private static final String DATE_OF_BIRTH_COLUMN = "date_of_birth";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM user";
    private static final String INSERT_USER_QUERY = "INSERT INTO user(name, email, date_of_birth) VALUES (?, ?, ?)";

    private final DataSource dataSource;

    public JDBCUserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> selectAll() {
        List<User> users = new ArrayList<>();
        try(Connection con = dataSource.getConnection(); Statement stm = con.createStatement();
            ResultSet resultSet = stm.executeQuery(SELECT_ALL_QUERY)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(ID_COLUMN));
                user.setName(resultSet.getString(NAME_COLUMN));
                user.setEmail(resultSet.getString(EMAIL_COLUMN));
                user.setDateOfBirth(resultSet.getTimestamp(DATE_OF_BIRTH_COLUMN));

                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }

    @Override
    public void add(User user) {
        try (Connection con = dataSource.getConnection()) {
            insertUser(user, con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addAll(List<User> users) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (User user : users) {
                    insertUser(user, con);
                }

                con.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                con.rollback();
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }

    private void insertUser(User user, Connection con) throws SQLException {
        try (PreparedStatement preparedStatement = con.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setTimestamp(3, user.getDateOfBirth());

            final int effectiveRows = preparedStatement.executeUpdate();

            if (effectiveRows == 1) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(ID_COLUMN));
                    }
                }
            }
        }
    }
}
