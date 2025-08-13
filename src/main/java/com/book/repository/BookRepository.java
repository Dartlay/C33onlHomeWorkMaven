package com.book.repository;


import com.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorNameContaining(String name);

    @Query("SELECT b FROM Book b ORDER BY SIZE(b.favorites) DESC LIMIT 10")
    List<Book> findTop10ByFavorites();

    List<Book> findByGenreName(String genre);

    List<Book> findByYearBetween(Integer start, Integer end);

    List<Book> findByTitleContainingIgnoreCase(String query);
}