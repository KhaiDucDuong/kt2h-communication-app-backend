package hcmute.hhkt.messengerapp.service.BaseRedisService;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService {
    void set(String key, Object value) throws JsonProcessingException;

    void setTimeToLiveInSeconds(String key, long timeoutInSeconds);
    void setTimeToLiveInMinutes(String key, long timeoutInMinutes);
    void setTimeToLiveInHours(String key, long timeoutInHours);
    void setTimeToLiveInDays(String key, long timeoutInDays);

    void hashSet(String key, String field, Object value) throws JsonProcessingException;
    boolean hashExists(String key, String field);

    Object get(String key);

    public Map<String, Object> getField(String key);

    Object hashGet(String key, String field);

    List<Object> hashGetByFieldPrefix(String key, String filedPrefix);

    Set<String> getFieldPrefixes(String key);

    void delete(String key);

    void delete(String key, String field);

    void delete(String key, List<String> fields);
}
