package hcmute.hhkt.messengerapp.service.BaseRedisService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BaseRedisServiceImpl implements BaseRedisService{
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private final ObjectMapper redisObjectMapper;

    @Override
    public void set(String key, Object value) throws JsonProcessingException {
        String mappedValue = redisObjectMapper.writeValueAsString(value);
        redisTemplate.opsForValue().set(key, mappedValue);
    }

    @Override
    public void setTimeToLiveInSeconds(String key, long timeoutInSeconds) {
        redisTemplate.expire(key, timeoutInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setTimeToLiveInMinutes(String key, long timeoutInMinutes) {
        redisTemplate.expire(key, timeoutInMinutes, TimeUnit.MINUTES);

    }

    @Override
    public void setTimeToLiveInHours(String key, long timeoutInHours) {
        redisTemplate.expire(key, timeoutInHours, TimeUnit.HOURS);

    }

    @Override
    public void setTimeToLiveInDays(String key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }


    @Override
    public void hashSet(String key, String field, Object value) throws JsonProcessingException {
        String mappedValue = redisObjectMapper.writeValueAsString(value);
        hashOperations.put(key, field, mappedValue);
    }

    @Override
    public boolean hashExists(String key, String field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> getField(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Object hashGet(String key, String field) {
        return hashOperations.get(key, field);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(String key, String filedPrefix) {

        Map<String, Object> hashEntries = hashOperations.entries(key);

        return new ArrayList<>(hashEntries.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(filedPrefix)).toList());
    }

    @Override
    public Set<String> getFieldPrefixes(String key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String key, String field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void delete(String key, List<String> fields) {
        fields.forEach(field -> hashOperations.delete(key, field));
    }
}
