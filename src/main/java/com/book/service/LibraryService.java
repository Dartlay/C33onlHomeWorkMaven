package com.book.service;

import com.book.model.Book;
import com.book.model.User;
import com.book.model.UserLibrary;
import com.book.repository.BookRepository;
import com.book.repository.UserLibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final UserLibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    public List<Book> getUserLibrary(User user) {
        return libraryRepository.findByUser(user).stream()
                .map(UserLibrary::getBook)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addToLibrary(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (libraryRepository.existsByUserAndBook(user, book)) {
            throw new RuntimeException("Book already in library");
        }

        UserLibrary libraryEntry = new UserLibrary();
        libraryEntry.setId(new UserLibrary.UserLibraryId(user.getId(), bookId));
        libraryEntry.setUser(user);
        libraryEntry.setBook(book);
        libraryRepository.save(libraryEntry);
    }

    public boolean isBookInUserLibrary(Long bookId, User user) {
        return libraryRepository.existsByUserAndBook(user,
                bookRepository.findById(bookId).orElse(null));
    }

    @Transactional
    public void removeFromLibrary(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        UserLibrary libraryEntry = libraryRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new RuntimeException("Книга не найдена в вашей библиотеке"));

        libraryRepository.delete(libraryEntry);
    }

    @Transactional
    public void updateReadingProgress(Long bookId, User user, int page) {
        UserLibrary libraryEntry = libraryRepository.findByUserAndBook(user,
                        bookRepository.findById(bookId)
                                .orElseThrow(() -> new RuntimeException("Book not found")))
                .orElseThrow(() -> new RuntimeException("Book not found in library"));

        libraryEntry.setLastReadPage(page);
        libraryRepository.save(libraryEntry);
    }
}