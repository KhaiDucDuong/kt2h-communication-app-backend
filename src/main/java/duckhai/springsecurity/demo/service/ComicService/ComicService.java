package duckhai.springsecurity.demo.service.ComicService;

import duckhai.springsecurity.demo.domain.Comic;
import duckhai.springsecurity.demo.dto.ComicDTO;

import java.util.List;

public interface ComicService {
    Comic createComic(ComicDTO comicDTO);

    List<Comic> getAllComics();

    Comic updateComic(Long id, ComicDTO comicDTO);

    boolean deleteComic(Long id);
}
