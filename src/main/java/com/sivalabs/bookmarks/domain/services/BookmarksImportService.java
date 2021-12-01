package com.sivalabs.bookmarks.domain.services;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.bookmarks.domain.models.BookmarkDTO;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookmarksImportService {
    private final BookmarkService bookmarkService;

    public void importBookmarks(String fileName) throws IOException, CsvValidationException {
        log.info("Importing bookmarks from file: {}", fileName);
        long count = 0L;

        ClassPathResource file = new ClassPathResource(fileName, this.getClass());
        try (InputStreamReader inputStreamReader =
                        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(inputStreamReader)) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();
                BookmarkDTO bookmarkDTO = new BookmarkDTO();
                bookmarkDTO.setUrl(nextLine[0]);
                bookmarkDTO.setTitle(nextLine[1]);
                bookmarkDTO.setCreatedAt(LocalDateTime.now());
                bookmarkService.createBookmark(bookmarkDTO);
                count++;
            }
        }
        log.info("Imported {} bookmarks from file {}", count, fileName);
    }
}
