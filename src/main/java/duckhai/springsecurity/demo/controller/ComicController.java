package duckhai.springsecurity.demo.controller;

import duckhai.springsecurity.demo.domain.Comic;
import duckhai.springsecurity.demo.dto.ComicDTO;
import duckhai.springsecurity.demo.service.ComicService.ComicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comics")
@RequiredArgsConstructor
public class ComicController {
    private final ComicService comicService;

    @GetMapping("")
    public ResponseEntity<?> getAllComics(){
        List<Comic> comicList = comicService.getAllComics();
        return ResponseEntity.ok().body(comicList);
    }

    @PostMapping("")
    public ResponseEntity<?> createComic(@Valid @RequestBody ComicDTO comicDTO){
        Comic newComic = comicService.createComic(comicDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComic);
    }
}
