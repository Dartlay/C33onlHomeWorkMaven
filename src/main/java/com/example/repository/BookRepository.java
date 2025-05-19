package com.example.repository;

import com.example.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final String url = "jdbc:postgresql://localhost:5432/your_db";
    private final String user = "your_user";
    private final String password = "your_password";

    public void saveBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, file_path, uploaded_by, author, description, publication_year, language, cover_image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getFilePath());
            stmt.setInt(3, book.getUploadedBy());
            stmt.setString(4, book.getAuthor());
            stmt.setString(5, book.getDescription());
            stmt.setInt(6, book.getPublicationYear());
            stmt.setString(7, book.getLanguage());
            stmt.setString(8, book.getCoverImageUrl());
            stmt.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setFilePath(rs.getString("file_path"));
                book.setAuthor(rs.getString("author"));
                book.setDescription(rs.getString("description"));
                books.add(book);
            }
        }
        return books;
    }
}