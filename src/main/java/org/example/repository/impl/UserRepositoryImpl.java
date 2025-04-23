package org.example.repository.impl;


import com.zaxxer.hikari.HikariDataSource;
import org.example.config.DatabaseConfig;
import org.example.model.User;
import org.example.repository.UserRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final HikariDataSource dataSource = DatabaseConfig.getDataSource();

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapUser(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error finding user by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error finding all users", e);
        }
        return users;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (login, name, email) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            handleSQLException("Error saving user: " + user.getLogin(), e);
            throw new RuntimeException("Failed to save user", e);
        }
        return user;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET login = ?, name = ?, email = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setLong(4, user.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("User not found with id: " + user.getId());
            }
        } catch (SQLException e) {
            handleSQLException("Error updating user: " + user.getId(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("User not found with id: " + id);
            }
        } catch (SQLException e) {
            handleSQLException("Error deleting user: " + id, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        String sql = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            handleSQLException("Error checking login existence: " + login, e);
        }
        return false;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getString("email")
        );
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message);
        e.printStackTrace();

        if (e.getSQLState().equals("23505")) {
            throw new RuntimeException("Login already exists", e);
        }
    }
}