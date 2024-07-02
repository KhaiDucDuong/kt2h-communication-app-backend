package hcmute.hhkt.messengerapp.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
