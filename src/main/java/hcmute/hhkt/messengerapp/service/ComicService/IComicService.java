package hcmute.hhkt.messengerapp.service.ComicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.Comic;
import hcmute.hhkt.messengerapp.dto.ComicDTO;

public interface IComicService {
    Comic createComic(ComicDTO comicDTO);

    ResultPaginationResponse getAllComics(Pageable pageable) throws JsonProcessingException;

    Comic updateComic(Long id, ComicDTO comicDTO);

    boolean deleteComic(Long id);
}
