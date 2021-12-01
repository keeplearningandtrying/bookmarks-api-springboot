package com.sivalabs.bookmarks.domain.repositories;

import com.sivalabs.bookmarks.domain.entities.Bookmark;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select b.id from Bookmark b")
    Page<Long> fetchBookmarkIds(Pageable pageable);

    @Query("select b.id from Bookmark b where lower(b.title) like lower(concat('%', :query,'%'))")
    Page<Long> fetchBookmarkIdsByTitleContainingIgnoreCase(
            @Param("query") String query, Pageable pageable);

    @Query("select distinct b from Bookmark b where b.id in ?1")
    List<Bookmark> findBookmarks(List<Long> bookmarkIds, Sort sort);
}
