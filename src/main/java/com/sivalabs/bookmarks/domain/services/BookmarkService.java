package com.sivalabs.bookmarks.domain.services;

import com.sivalabs.bookmarks.domain.entities.Bookmark;
import com.sivalabs.bookmarks.domain.mappers.BookmarkMapper;
import com.sivalabs.bookmarks.domain.models.BookmarkDTO;
import com.sivalabs.bookmarks.domain.models.BookmarksDTO;
import com.sivalabs.bookmarks.domain.repositories.BookmarkRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;

    @Transactional(readOnly = true)
    public BookmarksDTO getAllBookmarks(Pageable pageable) {
        Page<Long> pageOfBookmarkIds = bookmarkRepository.fetchBookmarkIds(pageable);
        List<Bookmark> bookmarks =
                bookmarkRepository.findBookmarks(
                        pageOfBookmarkIds.getContent(), pageable.getSort());
        Page<Bookmark> pageOfAuthors =
                new PageImpl<>(bookmarks, pageable, pageOfBookmarkIds.getTotalElements());
        return buildBookmarksResult(pageOfAuthors);
    }

    @Transactional(readOnly = true)
    public BookmarksDTO searchBookmarks(String query, Pageable pageable) {
        Page<Long> pageOfBookmarkIds =
                bookmarkRepository.fetchBookmarkIdsByTitleContainingIgnoreCase(query, pageable);
        List<Bookmark> bookmarks =
                bookmarkRepository.findBookmarks(
                        pageOfBookmarkIds.getContent(), pageable.getSort());
        Page<Bookmark> pageOfAuthors =
                new PageImpl<>(bookmarks, pageable, pageOfBookmarkIds.getTotalElements());
        return buildBookmarksResult(pageOfAuthors);
    }

    public BookmarkDTO createBookmark(BookmarkDTO bookmark) {
        bookmark.setId(null);
        log.debug("process=create_bookmark, url={}", bookmark.getUrl());
        return bookmarkMapper.toDTO(saveBookmark(bookmark));
    }

    public void deleteBookmark(Long id) {
        log.debug("process=delete_bookmark_by_id, id={}", id);
        bookmarkRepository.deleteById(id);
    }

    public void deleteAllBookmarks() {
        log.debug("process=delete_all_bookmarks");
        bookmarkRepository.deleteAllInBatch();
    }

    private BookmarksDTO buildBookmarksResult(Page<Bookmark> bookmarks) {
        log.trace("Found {} bookmarks in page", bookmarks.getNumberOfElements());
        return new BookmarksDTO(bookmarks.map(bookmarkMapper::toDTO));
    }

    private Bookmark saveBookmark(BookmarkDTO bookmarkDTO) {
        Bookmark bookmark = new Bookmark();
        if (bookmarkDTO.getId() != null) {
            bookmark = bookmarkRepository.findById(bookmarkDTO.getId()).orElse(bookmark);
        }
        bookmark.setUrl(bookmarkDTO.getUrl());
        bookmark.setTitle(getTitle(bookmarkDTO));
        bookmark.setCreatedAt(LocalDateTime.now());
        return bookmarkRepository.save(bookmark);
    }

    private String getTitle(BookmarkDTO bookmark) {
        if (StringUtils.isNotEmpty(bookmark.getTitle())) {
            return bookmark.getTitle();
        }
        try {
            Document doc = Jsoup.connect(bookmark.getUrl()).get();
            return doc.title();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return bookmark.getUrl();
    }
}
