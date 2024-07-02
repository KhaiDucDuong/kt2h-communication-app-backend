package hcmute.hhkt.messengerapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.Comic;
import hcmute.hhkt.messengerapp.dto.ComicDTO;
import hcmute.hhkt.messengerapp.service.ComicService.IComicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hcmute.hhkt.messengerapp.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/comics")
@RequiredArgsConstructor
public class ComicController {
    private final IComicService comicService;

    @GetMapping("")
    @ApiMessage("Fetched all comics")
    public ResponseEntity<?> getAllComics(Pageable pageable) throws JsonProcessingException {
        ResultPaginationResponse resultPaginationResponse = comicService.getAllComics(pageable);
        return ResponseEntity.ok().body(resultPaginationResponse);
    }

    @PostMapping("")
    @ApiMessage("Created comic")
    @PreAuthorize("hasAnyAuthority('WRITE_COMIC', 'ADMIN_AUTHORITY')")
    public ResponseEntity<?> createComic(@Valid @RequestBody ComicDTO comicDTO) {
        Comic newComic = comicService.createComic(comicDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComic);
    }
}
