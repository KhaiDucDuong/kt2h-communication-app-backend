package vn.khaiduong.comiclibrary.service.ComicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import vn.khaiduong.comiclibrary.Response.ResultPaginationResponse;
import vn.khaiduong.comiclibrary.domain.Comic;
import vn.khaiduong.comiclibrary.dto.ComicDTO;

public interface IComicService {
    Comic createComic(ComicDTO comicDTO);

    ResultPaginationResponse getAllComics(Pageable pageable) throws JsonProcessingException;

    Comic updateComic(Long id, ComicDTO comicDTO);

    boolean deleteComic(Long id);
}
