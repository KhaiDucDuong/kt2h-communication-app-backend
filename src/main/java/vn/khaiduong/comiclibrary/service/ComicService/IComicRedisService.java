package vn.khaiduong.comiclibrary.service.ComicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.khaiduong.comiclibrary.domain.Comic;

import java.util.List;

public interface IComicRedisService {
    void clear();
    Page<Comic> getAllComics(String keyword, Pageable pageable) throws JsonProcessingException;
    void saveAllProducts(Page<Comic> comics, String keyword, Pageable pageable) throws JsonProcessingException;
}
