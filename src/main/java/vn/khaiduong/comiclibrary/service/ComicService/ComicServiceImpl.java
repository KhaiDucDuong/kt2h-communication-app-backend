package vn.khaiduong.comiclibrary.service.ComicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import vn.khaiduong.comiclibrary.Response.Meta;
import vn.khaiduong.comiclibrary.Response.ResultPaginationResponse;
import vn.khaiduong.comiclibrary.domain.Comic;
import vn.khaiduong.comiclibrary.dto.ComicDTO;
import vn.khaiduong.comiclibrary.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.khaiduong.comiclibrary.service.BaseRedisService.BaseRedisServiceImpl;
import vn.khaiduong.comiclibrary.util.RedisUtil;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ComicServiceImpl implements IComicService {
    private final ComicRepository comicRepository;
    private final IComicRedisService comicRedisService;
    private final BaseRedisServiceImpl baseRedisService;
    private final ObjectMapper redisObjectMapper;

    public static String COMIC_HASH_KEY = "COMIC";
    private final String COMIC_FIELD_PREFIX = "of";
    private final long redisComicExpirationInMinutes = 10;

    @Override
    public Comic createComic(ComicDTO comicDTO) {
        Comic newComic = Comic.builder()
                .name(comicDTO.getName())
                .build();
        return comicRepository.save(newComic);
    }

    @Override
    public ResultPaginationResponse getAllComics(Pageable pageable) throws JsonProcessingException {
        ResultPaginationResponse response;
        String comicHashField = RedisUtil.getKeyFrom(COMIC_FIELD_PREFIX, pageable);
        if(baseRedisService.hashExists(COMIC_HASH_KEY, comicHashField)){
            String objectString = (String) baseRedisService.hashGet(COMIC_HASH_KEY, comicHashField);
            response = redisObjectMapper.readValue(objectString, new TypeReference<ResultPaginationResponse>() {});
        } else {
            Page<Comic> comicPage = comicRepository.findAll(pageable);
            Meta meta = Meta.builder()
                    .page(comicPage.getNumber() + 1)
                    .pageSize(comicPage.getSize())
                    .pages(comicPage.getTotalPages())
                    .total(comicPage.getTotalElements())
                    .build();
            response = ResultPaginationResponse.builder()
                    .meta(meta)
                    .result(comicPage.getContent())
                    .build();
            baseRedisService.hashSet(COMIC_HASH_KEY, comicHashField, response);
            baseRedisService.setTimeToLiveInMinutes(COMIC_HASH_KEY, redisComicExpirationInMinutes);
        }

        return response;
    }

    @Override
    public Comic updateComic(Long id, ComicDTO comicDTO) {
        return null;
    }

    @Override
    public boolean deleteComic(Long id) {
        return false;
    }
}
