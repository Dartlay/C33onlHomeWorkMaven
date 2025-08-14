package com.book.repository;


import com.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b ORDER BY b.uploadedAt DESC LIMIT 10")
    List<Book> findByAuthorNameContaining(String name);

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(b.author.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(b.year AS string) LIKE CONCAT('%', :query, '%')")
    List<Book> searchBooks(@Param("query") String query);

    // Фильтрация по жанру и дате добавления
    @Query("SELECT b FROM Book b WHERE " +
            "(:genreId IS NULL OR b.genre.id = :genreId) " +
            "ORDER BY b.title ASC")
    List<Book> filterBooks(
            @Param("genreId") Long genreId);


    @Query("SELECT b FROM Book b ORDER BY b.uploadedAt DESC LIMIT 10")
    List<Book> findTop10Newest();

    @Query("SELECT b FROM Book b WHERE b.uploadedAt >= :cutoffDate ORDER BY b.uploadedAt DESC")
    List<Book> findNewestBooks(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query(value = "SELECT * FROM books ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Book> findRandom10();
}

