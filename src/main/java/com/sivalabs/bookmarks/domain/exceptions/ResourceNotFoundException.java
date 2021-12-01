package com.sivalabs.bookmarks.domain.exceptions;

public class ResourceNotFoundException extends BookmarksException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
