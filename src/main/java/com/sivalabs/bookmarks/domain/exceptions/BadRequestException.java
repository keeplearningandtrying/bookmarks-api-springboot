package com.sivalabs.bookmarks.domain.exceptions;

public class BadRequestException extends BookmarksException {
    public BadRequestException(String message) {
        super(message);
    }
}
