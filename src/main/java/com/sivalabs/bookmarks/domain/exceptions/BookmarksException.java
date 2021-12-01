package com.sivalabs.bookmarks.domain.exceptions;

public class BookmarksException extends RuntimeException {
    public BookmarksException(String message) {
        super(message);
    }

    public BookmarksException(Throwable cause) {
        super(cause);
    }
}
