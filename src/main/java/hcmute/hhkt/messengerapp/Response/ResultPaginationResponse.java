package hcmute.hhkt.messengerapp.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultPaginationResponse {
    private Meta meta;
    private Object result;
}
