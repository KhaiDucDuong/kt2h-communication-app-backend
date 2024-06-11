package vn.khaiduong.comiclibrary.service.ComicService;

import org.springframework.data.domain.Pageable;
import vn.khaiduong.comiclibrary.Response.ResultPaginationResponse;
import vn.khaiduong.comiclibrary.domain.Comic;
import vn.khaiduong.comiclibrary.dto.ComicDTO;

import java.util.List;

public interface ComicService {
    Comic createComic(ComicDTO comicDTO);

    ResultPaginationResponse getAllComics(Pageable pageable);

    Comic updateComic(Long id, ComicDTO comicDTO);

    boolean deleteComic(Long id);
}
