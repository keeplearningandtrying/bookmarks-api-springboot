package com.sivalabs.bookmarks.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.bookmarks.common.AbstractIntegrationTest;
import com.sivalabs.bookmarks.domain.models.BookmarkDTO;
import com.sivalabs.bookmarks.domain.services.BookmarkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BookmarkControllerTest extends AbstractIntegrationTest {
    @Autowired private BookmarkService bookmarkService;

    @Test
    void shouldFetchBookmarksFirstPage() throws Exception {
        this.mockMvc.perform(get("/api/bookmarks")).andExpect(status().isOk());
    }

    @Test
    void shouldFetchBookmarksSecondPage() throws Exception {
        this.mockMvc.perform(get("/api/bookmarks?page=2")).andExpect(status().isOk());
    }

    @Test
    void shouldSearchBookmarks() throws Exception {
        this.mockMvc.perform(get("/api/bookmarks/search?query=spring")).andExpect(status().isOk());
    }

    @Test
    void shouldSearchBookmarksReturnAllBookmarksIfQueryIsEmpty() throws Exception {
        this.mockMvc.perform(get("/api/bookmarks/search?query=")).andExpect(status().isOk());
    }

    @Test
    void shouldDeleteBookmark() throws Exception {
        BookmarkDTO bookmark = new BookmarkDTO();
        bookmark.setUrl("https://google.com");
        BookmarkDTO savedBookmark = bookmarkService.createBookmark(bookmark);
        this.mockMvc
                .perform(delete("/api/bookmarks/" + savedBookmark.getId()))
                .andExpect(status().isOk());
    }
}
