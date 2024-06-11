package vn.khaiduong.comiclibrary.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.khaiduong.comiclibrary.Response.ResultPaginationResponse;
import vn.khaiduong.comiclibrary.domain.Comic;
import vn.khaiduong.comiclibrary.dto.ComicDTO;
import vn.khaiduong.comiclibrary.service.ComicService.ComicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comics")
@RequiredArgsConstructor
public class ComicController {
    private final ComicService comicService;
    private final int DEFAULT_PAGE_SIZE = 20;

    @GetMapping("")
    public ResponseEntity<?> getAllComics(@RequestParam("current") Optional<String> currentOptional,
                                          @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.orElse("1");
        String sPageSize = pageSizeOptional.orElse(String.valueOf(DEFAULT_PAGE_SIZE));
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));

        ResultPaginationResponse resultPaginationResponse = comicService.getAllComics(pageable);
        return ResponseEntity.ok().body(resultPaginationResponse);
    }

    @PostMapping("")
    public ResponseEntity<?> createComic(@Valid @RequestBody ComicDTO comicDTO) {
        Comic newComic = comicService.createComic(comicDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComic);
    }
}
