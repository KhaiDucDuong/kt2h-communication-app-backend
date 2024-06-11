package vn.khaiduong.comiclibrary.service.ComicService;

import vn.khaiduong.comiclibrary.domain.Comic;
import vn.khaiduong.comiclibrary.dto.ComicDTO;

import java.util.List;

public interface ComicService {
    Comic createComic(ComicDTO comicDTO);

    List<Comic> getAllComics();

    Comic updateComic(Long id, ComicDTO comicDTO);

    boolean deleteComic(Long id);
}
