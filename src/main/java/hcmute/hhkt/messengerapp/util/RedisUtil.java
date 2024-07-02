package hcmute.hhkt.messengerapp.util;

import org.springframework.data.domain.Pageable;

public final class RedisUtil {
    public static String getKeyFrom(String keyword, Pageable pageable){
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortType = pageable.getSort().toString();
        return String.format("%s:%d:%d:%s", keyword, pageNumber, pageSize, sortType);
    }
}
