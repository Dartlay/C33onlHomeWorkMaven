package com.book.repository;

import com.book.model.Book;
import com.book.model.User;
import com.book.model.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLibraryRepository extends JpaRepository<UserLibrary, UserLibrary.UserLibraryId> {
    List<UserLibrary> findByUser(User user);

    Optional<UserLibrary> findByUserAndBook(User user, Book book);

    boolean existsByUserAndBook(User user, Book book);

    void deleteByUserAndBook(User user, Book book);


}