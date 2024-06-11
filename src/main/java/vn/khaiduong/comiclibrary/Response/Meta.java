package vn.khaiduong.comiclibrary.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Meta {
    @JsonProperty("page")
    private int page;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("pages")
    private int pages;
    @JsonProperty("total")
    private long total;
}
