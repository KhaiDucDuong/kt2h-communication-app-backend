package vn.khaiduong.comiclibrary.service.ComicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.khaiduong.comiclibrary.configuration.RedisConfiguration;
import vn.khaiduong.comiclibrary.domain.Comic;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComicRedisServiceImpl implements IComicRedisService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    private String getKeyFrom(String keyword, Pageable pageable){
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortType = pageable.getSort().toString();
        return String.format("%s:%d:%d:%s", keyword, pageNumber, pageSize, sortType);
    }
    @Override
    public void clear() {

    }

    @Override
    public Page<Comic> getAllComics(String keyword, Pageable pageable) throws JsonProcessingException {
        String key = getKeyFrom(keyword, pageable);
        String json = (String) redisTemplate.opsForValue().get(key);
        Page<Comic> comicPage = json != null ?
                redisObjectMapper.readValue(json, new TypeReference<Page<Comic>>() {
                }) : null;
        return comicPage;
    }

    @Override
    public void saveAllProducts(Page<Comic> comics, String keyword, Pageable pageable) throws JsonProcessingException {
        String key = getKeyFrom(keyword, pageable);
        String json = redisObjectMapper.writeValueAsString(comics);
        redisTemplate.opsForValue().set(key, json);
    }
}
