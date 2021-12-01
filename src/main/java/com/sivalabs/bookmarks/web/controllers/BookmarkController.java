package com.sivalabs.bookmarks.web.controllers;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.bookmarks.domain.models.BookmarkDTO;
import com.sivalabs.bookmarks.domain.models.BookmarksDTO;
import com.sivalabs.bookmarks.domain.services.BookmarkService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/bookmarks")
    public BookmarksDTO getBookmarks(
            @PageableDefault(size = 15)
                    @SortDefault.SortDefaults({@SortDefault(sort = "createdAt", direction = DESC)})
                    Pageable pageable) {
        log.info("Fetching bookmarks with page: {}", pageable.getPageNumber());
        return bookmarkService.getAllBookmarks(pageable);
    }

    @GetMapping("/bookmarks/search")
    public BookmarksDTO search(
            @RequestParam(name = "query", required = false) String query,
            @PageableDefault(size = 15)
                    @SortDefault.SortDefaults({@SortDefault(sort = "createdAt", direction = DESC)})
                    Pageable pageable) {
        BookmarksDTO data;
        if (StringUtils.isNotEmpty(query)) {
            log.info("Searching bookmarks for {} with page: {}", query, pageable.getPageNumber());
            data = bookmarkService.searchBookmarks(query, pageable);
        } else {
            log.info("Fetching bookmarks with page: {}", pageable.getPageNumber());
            data = bookmarkService.getAllBookmarks(pageable);
        }
        return data;
    }

    @PostMapping("/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBookmark(@Valid @RequestBody BookmarkDTO bookmark) {
        bookmarkService.createBookmark(bookmark);
    }

    @DeleteMapping("/bookmarks/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        bookmarkService.deleteBookmark(id);
        return ResponseEntity.ok().build();
    }
}
