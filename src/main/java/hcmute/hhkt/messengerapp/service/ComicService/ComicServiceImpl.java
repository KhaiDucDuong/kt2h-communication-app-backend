package hcmute.hhkt.messengerapp.service.ComicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import hcmute.hhkt.messengerapp.Response.Meta;
import hcmute.hhkt.messengerapp.Response.ResultPaginationResponse;
import hcmute.hhkt.messengerapp.domain.Comic;
import hcmute.hhkt.messengerapp.dto.ComicDTO;
import hcmute.hhkt.messengerapp.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hcmute.hhkt.messengerapp.service.BaseRedisService.BaseRedisServiceImpl;
import hcmute.hhkt.messengerapp.util.RedisUtil;

@RequiredArgsConstructor
@Service
public class ComicServiceImpl implements IComicService {
    private final ComicRepository comicRepository;
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
        String comicKey = RedisUtil.getKeyFrom(COMIC_HASH_KEY, pageable);
        String redisCachedData = (String) baseRedisService.get(comicKey);
        if(!StringUtils.isBlank(redisCachedData)){
            //reset data expiration in redis
            baseRedisService.setTimeToLiveInMinutes(comicKey, redisComicExpirationInMinutes);
            response = redisObjectMapper.readValue(redisCachedData, new TypeReference<ResultPaginationResponse>() {});
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

            //set data in redis and expiration
            baseRedisService.set(comicKey, response);
            baseRedisService.setTimeToLiveInMinutes(comicKey, redisComicExpirationInMinutes);
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
