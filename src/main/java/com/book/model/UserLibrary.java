package com.book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_library")
@Data
@NoArgsConstructor
public class UserLibrary {
    @EmbeddedId
    private UserLibraryId id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @Column(name = "last_read_page")
    private Integer lastReadPage = 1;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLibraryId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "book_id")
        private Long bookId;
    }
}